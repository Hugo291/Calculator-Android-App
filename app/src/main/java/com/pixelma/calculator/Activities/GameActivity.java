package com.pixelma.calculator.Activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.pixelma.calculator.Dialogs.GameOverDialog;
import com.pixelma.calculator.Dialogs.PauseDialog;
import com.pixelma.calculator.Dialogs.VictoryDialog;
import com.pixelma.calculator.Interfaces.GameOverDialogListener;
import com.pixelma.calculator.Interfaces.PauseDialogListener;
import com.pixelma.calculator.Interfaces.TimerActions;
import com.pixelma.calculator.Interfaces.VictoryDialogListener;
import com.pixelma.calculator.Models.Calculation;
import com.pixelma.calculator.Models.Timer;
import com.pixelma.calculator.R;
import com.pixelma.calculator.Utils.ButtonAnimationHelper;
import com.pixelma.calculator.Utils.GameConfig;

/**
 * The main activity for the game screen. It handles the game logic, UI updates, and user interactions.
 */
public class GameActivity extends AppCompatActivity implements TimerActions, PauseDialogListener, GameOverDialogListener, VictoryDialogListener {

    public static final String OPERATOR = "OPERATOR";

    private ButtonAnimationHelper animationHelper;
    private Calculation calculation;
    private Timer timer;
    private Vibrator vibrator;

    private int currentRound = 0;
    private int rightAnswers = 0;
    private int wrongAnswers = 0;
    private int gameOperator;

    // UI Elements
    private TextView tvDigit1, tvOperator, tvDigit2, tvResult, tvTimer, tvRightAnswer, tvWrongAnswer;
    private FrameLayout btnPause;

    /**
     * Called when the activity is first created.
     * Initializes the UI, sets up window insets for edge-to-edge display,
     * retrieves intent parameters, sets up button listeners, and starts the game.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_game);

        setupWindowInsets();
        getIntentParams();
        initViews();
        setupButtons();
        startGame();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * Sets up the window insets to allow the content to be displayed edge-to-edge.
     * This method adds padding to the root view to account for the system bars.
     */
    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Retrieves the game operator from the intent extras.
     * Defaults to PLUS if no operator is provided.
     */
    private void getIntentParams() {
        if (getIntent() != null) {
            gameOperator = getIntent().getIntExtra(OPERATOR, GameConfig.Operators.PLUS);
        }
    }

    /**
     * Initializes all the UI views from the layout file.
     */
    private void initViews() {
        tvDigit1 = findViewById(R.id.tv_digit_1);
        tvOperator = findViewById(R.id.tv_operator);
        tvDigit2 = findViewById(R.id.tv_digit_2);
        tvResult = findViewById(R.id.tv_result);
        tvTimer = findViewById(R.id.timer);
        tvRightAnswer = findViewById(R.id.right_answer);
        tvWrongAnswer = findViewById(R.id.wrong_answer);
        btnPause = findViewById(R.id.btn_pause);
    }

    /**
     * Sets up the on-click listeners and animations for all the buttons in the activity.
     */
    private void setupButtons() {
        animationHelper = new ButtonAnimationHelper(this);
        // Number buttons
        for (int i = 0; i <= 9; i++) {
            int buttonId = getResources().getIdentifier("btn_" + i, "id", getPackageName());
            final String number = String.valueOf(i);
            setupButton(buttonId, () -> appendNumber(number));
        }
        // Action buttons
        setupButton(R.id.btn_valid, this::validateAnswer);
        setupButton(R.id.btn_erase, this::eraseLastDigit);
        // Pause button
        btnPause.setOnClickListener(v -> showPauseDialog());
    }

    /**
     * Helper method to set up a single button's animation and click listener.
     * @param buttonId The resource ID of the button.
     * @param action The action to perform when the button is clicked.
     */
    private void setupButton(int buttonId, Runnable action) {
        MaterialCardView button = findViewById(buttonId);
        if (button != null) {
            animationHelper.setupButtonAnimation(button, action);
        }
    }

    /**
     * Starts a new game by resetting the score, round count, and timer.
     */
    public void startGame() {
        currentRound = 0;
        rightAnswers = 0;
        wrongAnswers = 0;
        updateScoreDisplay();
        if (timer == null) {
            timer = new Timer(this);
        }
        timer.start();
        nextRound();
    }

    /**
     * Proceeds to the next round of the game.
     * If the victory condition is met, it shows the victory dialog.
     * Otherwise, it generates a new calculation and updates the display.
     */
    private void nextRound() {
        if (currentRound >= GameConfig.Game.VICTORY_ROUND_COUNT) {
            showVictoryDialog();
            return;
        }
        calculation = new Calculation(currentRound, gameOperator);
        displayCalculation();
        currentRound++;
    }

    /**
     * Displays the current calculation on the screen.
     */
    private void displayCalculation() {
        String[] parts = calculation.getCalculationString().split(" ");
        if (parts.length >= 3) {
            tvDigit1.setText(parts[0]);
            tvOperator.setText(parts[1]);
            tvDigit2.setText(parts[2]);
            tvResult.setText("?");
        }
    }

    /**
     * Appends a number to the current result TextView.
     * @param number The number to append as a String.
     */
    private void appendNumber(String number) {
        String currentText = tvResult.getText().toString();
        if (currentText.equals("?")) {
            tvResult.setText(number);
        } else if (currentText.length() < GameConfig.Game.MAX_ANSWER_LENGTH) {
            tvResult.append(number);
        }
    }

    /**
     * Erases the last digit from the result TextView.
     */
    private void eraseLastDigit() {
        String currentText = tvResult.getText().toString();
        if (!currentText.equals("?") && !currentText.isEmpty()) {
            String newText = currentText.substring(0, currentText.length() - 1);
            tvResult.setText(newText.isEmpty() ? "?" : newText);
        }
    }

    /**
     * Validates the user's answer against the correct result.
     * Triggers either the correct or wrong answer flow.
     */
    private void validateAnswer() {
        int userAnswer = getUserAnswer();
        if (userAnswer != -1) {
            if (userAnswer == calculation.getResult()) {
                onCorrectAnswer();
            } else {
                onWrongAnswer();
            }
        }
    }

    /**
     * Retrieves the user's answer from the result TextView.
     * @return The user's answer as an integer, or -1 if the answer is invalid or not present.
     */
    private int getUserAnswer() {
        try {
            String resultText = tvResult.getText().toString();
            return resultText.equals("?") ? -1 : Integer.parseInt(resultText);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Handles the logic for a correct answer.
     * Increments the score, restarts the timer, and proceeds to the next round.
     */
    private void onCorrectAnswer() {
        rightAnswers++;
        updateScoreDisplay();
        timer.restart();
        nextRound();
    }

    /**
     * Handles the logic for a wrong answer.
     * Increments the wrong answer count, updates the display, and triggers a vibration.
     */
    private void onWrongAnswer() {
        wrongAnswers++;
        updateScoreDisplay();
        vibrateOnError();
    }

    /**
     * Vibrates the device to provide feedback for a wrong answer.
     */
    private void vibrateOnError() {
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrator.vibrate(200);
            }
        }
    }

    /**
     * Updates the score display on the screen.
     */
    private void updateScoreDisplay() {
        tvRightAnswer.setText(String.valueOf(rightAnswers));
        tvWrongAnswer.setText(String.valueOf(wrongAnswers));
    }

    /**
     * Shows the victory dialog when the game is won.
     */
    private void showVictoryDialog() {
        new VictoryDialog(this, this, rightAnswers).show();
    }

    /**
     * Shows the pause dialog and pauses the timer.
     */
    private void showPauseDialog() {
        if (timer != null) {
            timer.pause();
        }
        new PauseDialog(this, this).show();
    }

    /**
     * Shows the game over dialog.
     */
    private void showGameOverDialog() {
        new GameOverDialog(this, this, rightAnswers).show();
    }

    /**
     * Callback from the Timer, called every second to update the timer display.
     * @param currentTime The formatted string representing the current time.
     */
    @Override
    public void eachSecondTimer(String currentTime) {
        runOnUiThread(() -> tvTimer.setText(currentTime));
    }

    /**
     * Callback from the Timer, called when the timer finishes.
     * Shows the game over dialog.
     */
    @Override
    public void onTimerFinish() {
        runOnUiThread(this::showGameOverDialog);
    }

    /**
     * Called when the activity is paused. Pauses the timer.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.pause();
        }
    }

    /**
     * Called when the activity is resumed. Resumes the timer if it was paused.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null && !timer.isRunning()) {
            timer.resume();
        }
    }

    /**
     * Called when the activity is destroyed. Stops the timer.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.stop();
        }
    }

    // Dialog Listeners

    /**
     * Called when the resume button is clicked in the pause dialog.
     * Resumes the timer.
     */
    @Override
    public void onResumeClicked() {
        if (timer != null) {
            timer.resume();
        }
    }

    /**
     * Called when the quit button is clicked in a dialog.
     * Finishes the activity.
     */
    @Override
    public void onQuitClicked() {
        finish();
    }

    /**
     * Called when the restart button is clicked in a dialog.
     * Starts a new game.
     */
    @Override
    public void onRestartClicked() {
        startGame();
    }

    /**
     * Called when the next button is clicked in the victory dialog.
     * Starts a new game.
     */
    @Override
    public void onNextClicked() {
        startGame();
    }

    /**
     * Called when the home button is clicked in a dialog.
     * Finishes the activity.
     */
    @Override
    public void onHomeClicked() {
        finish();
    }
}
