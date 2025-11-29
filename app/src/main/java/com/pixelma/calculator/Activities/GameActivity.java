package com.pixelma.calculator.Activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.card.MaterialCardView;
import com.pixelma.calculator.Models.Calculation;
import com.pixelma.calculator.Models.Timer;
import com.pixelma.calculator.Interfaces.TimerActions;
import com.pixelma.calculator.R;
import com.pixelma.calculator.Utils.ButtonAnimationHelper;
import com.pixelma.calculator.Utils.GameConfig;

/**
 * Game activity - optimized version
 * Better separation of concerns and cleaner code
 */
public class GameActivity extends AppCompatActivity implements TimerActions {

    public static final String OPERATOR = "OPERATOR";

    private ButtonAnimationHelper animationHelper;
    private Calculation calculation;
    private Timer timer;

    private int currentRound = 0;
    private int rightAnswers = 0;
    private int wrongAnswers = 0;
    private int gameOperator;

    // UI Elements
    private TextView tvDigit1;
    private TextView tvOperator;
    private TextView tvDigit2;
    private TextView tvResult;
    private TextView tvTimer;
    private TextView tvRightAnswer;
    private TextView tvWrongAnswer;
    private FrameLayout btnPause;

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
    }

    /**
     * Setup window insets for edge-to-edge display
     */
    private void setupWindowInsets() {
        View rootView = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(rootView);
        if (windowInsetsController != null) {
            windowInsetsController.setAppearanceLightStatusBars(true);
        }
    }

    /**
     * Get operator parameter from intent
     */
    private void getIntentParams() {
        if (getIntent() != null) {
            gameOperator = getIntent().getIntExtra(OPERATOR, 0);
        }
    }

    /**
     * Initialize all views
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
     * Setup all buttons with animations
     */
    private void setupButtons() {
        animationHelper = new ButtonAnimationHelper(this);

        // Number buttons
        setupButton(R.id.btn_0, () -> appendNumber("0"));
        setupButton(R.id.btn_1, () -> appendNumber("1"));
        setupButton(R.id.btn_2, () -> appendNumber("2"));
        setupButton(R.id.btn_3, () -> appendNumber("3"));
        setupButton(R.id.btn_4, () -> appendNumber("4"));
        setupButton(R.id.btn_5, () -> appendNumber("5"));
        setupButton(R.id.btn_6, () -> appendNumber("6"));
        setupButton(R.id.btn_7, () -> appendNumber("7"));
        setupButton(R.id.btn_8, () -> appendNumber("8"));
        setupButton(R.id.btn_9, () -> appendNumber("9"));

        // Action buttons
        setupButton(R.id.btn_valid, this::validateAnswer);
        setupButton(R.id.btn_erase, this::eraseLastDigit);

        // Pause button
        btnPause.setOnClickListener(v -> showPauseDialog());
    }

    /**
     * Setup a single button
     */
    private void setupButton(int buttonId, Runnable action) {
        MaterialCardView button = findViewById(buttonId);
        if (button != null) {
            animationHelper.setupButtonAnimation(button, action);
        }
    }

    /**
     * Start new game
     */
    private void startGame() {
        currentRound = 0;
        rightAnswers = 0;
        wrongAnswers = 0;
        updateScoreDisplay();
        nextRound();
    }

    /**
     * Move to next round
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
     * Display current calculation
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
     * Append a number to the result
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
     * Erase last digit from result
     */
    private void eraseLastDigit() {
        String currentText = tvResult.getText().toString();

        if (currentText.equals("?") || currentText.isEmpty()) {
            return;
        }

        String newText = currentText.substring(0, currentText.length() - 1);
        tvResult.setText(newText.isEmpty() ? "?" : newText);
    }

    /**
     * Validate user answer
     */
    private void validateAnswer() {
        int userAnswer = getUserAnswer();

        if (userAnswer == -1) {
            return; // No answer entered
        }

        if (userAnswer == calculation.getResult()) {
            onCorrectAnswer();
        } else {
            onWrongAnswer();
        }
    }

    /**
     * Get user's answer as integer
     */
    private int getUserAnswer() {
        try {
            String resultText = tvResult.getText().toString();
            if (resultText.equals("?")) {
                return -1;
            }
            return Integer.parseInt(resultText);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Handle correct answer
     */
    private void onCorrectAnswer() {
        rightAnswers++;
        updateScoreDisplay();
        nextRound();
    }

    /**
     * Handle wrong answer
     */
    private void onWrongAnswer() {
        wrongAnswers++;
        updateScoreDisplay();
        // Optional: shake animation or visual feedback
    }

    /**
     * Update score display
     */
    private void updateScoreDisplay() {
        tvRightAnswer.setText(String.valueOf(rightAnswers));
        tvWrongAnswer.setText(String.valueOf(wrongAnswers));
    }

    /**
     * Show victory dialog
     */
    private void showVictoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_victory, null);
        builder.setView(dialogView);

        TextView victoryScore = dialogView.findViewById(R.id.victory_score);
        victoryScore.setText(String.valueOf(rightAnswers));

        FrameLayout btnNext = dialogView.findViewById(R.id.btn_next);
        FrameLayout btnHome = dialogView.findViewById(R.id.btn_home);

        AlertDialog dialog = builder.create();

        btnNext.setOnClickListener(v -> {
            startGame();
            dialog.dismiss();
        });

        btnHome.setOnClickListener(v -> {
            finish();
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * Show pause dialog
     */
    private void showPauseDialog() {
        if (timer != null) {
            timer.pause();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_pause, null);
        builder.setView(dialogView);

        FrameLayout btnResume = dialogView.findViewById(R.id.btn_resume);
        FrameLayout btnQuit = dialogView.findViewById(R.id.btn_quit);

        AlertDialog dialog = builder.create();

        btnResume.setOnClickListener(v -> {
            if (timer != null) {
                timer.resume();
            }
            dialog.dismiss();
        });

        btnQuit.setOnClickListener(v -> {
            finish();
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    // TimerActions interface implementation
    @Override
    public void eachSecondTimer(String currentTime) {
        runOnUiThread(() -> tvTimer.setText(currentTime));
    }

    @Override
    public void finish() {
        // Timer finished - handle game over
        runOnUiThread(this::showGameOverDialog);
    }

    /**
     * Show game over dialog
     */
    private void showGameOverDialog() {
        // TODO: Implement game over dialog
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null && !timer.isRunning()) {
            timer.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.stop();
        }
    }
}