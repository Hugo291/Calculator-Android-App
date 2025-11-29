package com.pixelma.calculator.Activities;

import android.os.Bundle;
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
import com.pixelma.calculator.Interfaces.VictoryDialogListener;
import com.pixelma.calculator.Models.Calculation;
import com.pixelma.calculator.Models.Timer;
import com.pixelma.calculator.Interfaces.TimerActions;
import com.pixelma.calculator.R;
import com.pixelma.calculator.Utils.ButtonAnimationHelper;
import com.pixelma.calculator.Utils.GameConfig;

public class GameActivity extends AppCompatActivity implements TimerActions, PauseDialogListener, GameOverDialogListener, VictoryDialogListener {

    public static final String OPERATOR = "OPERATOR";

    private ButtonAnimationHelper animationHelper;
    private Calculation calculation;
    private Timer timer;

    private int currentRound = 0;
    private int rightAnswers = 0;
    private int wrongAnswers = 0;
    private int gameOperator;

    // UI Elements
    private TextView tvDigit1, tvOperator, tvDigit2, tvResult, tvTimer, tvRightAnswer, tvWrongAnswer;
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

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void getIntentParams() {
        if (getIntent() != null) {
            gameOperator = getIntent().getIntExtra(OPERATOR, 0);
        }
    }

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

    private void setupButton(int buttonId, Runnable action) {
        MaterialCardView button = findViewById(buttonId);
        if (button != null) {
            animationHelper.setupButtonAnimation(button, action);
        }
    }

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

    private void nextRound() {
        if (currentRound >= GameConfig.Game.VICTORY_ROUND_COUNT) {
            showVictoryDialog();
            return;
        }
        calculation = new Calculation(currentRound, gameOperator);
        displayCalculation();
        currentRound++;
    }

    private void displayCalculation() {
        String[] parts = calculation.getCalculationString().split(" ");
        if (parts.length >= 3) {
            tvDigit1.setText(parts[0]);
            tvOperator.setText(parts[1]);
            tvDigit2.setText(parts[2]);
            tvResult.setText("?");
        }
    }

    private void appendNumber(String number) {
        String currentText = tvResult.getText().toString();
        if (currentText.equals("?")) {
            tvResult.setText(number);
        } else if (currentText.length() < GameConfig.Game.MAX_ANSWER_LENGTH) {
            tvResult.append(number);
        }
    }

    private void eraseLastDigit() {
        String currentText = tvResult.getText().toString();
        if (!currentText.equals("?") && !currentText.isEmpty()) {
            String newText = currentText.substring(0, currentText.length() - 1);
            tvResult.setText(newText.isEmpty() ? "?" : newText);
        }
    }

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

    private int getUserAnswer() {
        try {
            String resultText = tvResult.getText().toString();
            return resultText.equals("?") ? -1 : Integer.parseInt(resultText);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void onCorrectAnswer() {
        rightAnswers++;
        updateScoreDisplay();
        timer.restart();
        nextRound();
    }

    private void onWrongAnswer() {
        wrongAnswers++;
        updateScoreDisplay();
    }

    private void updateScoreDisplay() {
        tvRightAnswer.setText(String.valueOf(rightAnswers));
        tvWrongAnswer.setText(String.valueOf(wrongAnswers));
    }

    private void showVictoryDialog() {
        new VictoryDialog(this, this, rightAnswers).show();
    }

    private void showPauseDialog() {
        if (timer != null) {
            timer.pause();
        }
        new PauseDialog(this, this).show();
    }

    private void showGameOverDialog() {
        new GameOverDialog(this, this, rightAnswers).show();
    }

    @Override
    public void eachSecondTimer(String currentTime) {
        runOnUiThread(() -> tvTimer.setText(currentTime));
    }

    @Override
    public void onTimerFinish() {
        runOnUiThread(this::showGameOverDialog);
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

    // Dialog Listeners
    @Override
    public void onResumeClicked() {
        if (timer != null) {
            timer.resume();
        }
    }

    @Override
    public void onQuitClicked() {
        finish();
    }

    @Override
    public void onRestartClicked() {
        startGame();
    }

    @Override
    public void onNextClicked() {
        startGame();
    }

    @Override
    public void onHomeClicked() {
        finish();
    }
}
