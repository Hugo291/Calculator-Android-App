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
import com.pixelma.calculator.Dialogs.GameEndDialog;
import com.pixelma.calculator.Dialogs.PauseDialog;
import com.pixelma.calculator.Interfaces.GameEndDialogListener;
import com.pixelma.calculator.Interfaces.PauseDialogListener;
import com.pixelma.calculator.Interfaces.TimerActions;
import com.pixelma.calculator.Models.Calculation;
import com.pixelma.calculator.Models.Timer;
import com.pixelma.calculator.R;
import com.pixelma.calculator.Utils.ButtonAnimationHelper;
import com.pixelma.calculator.Utils.GameConfig;

public class GameActivity extends AppCompatActivity implements TimerActions, PauseDialogListener, GameEndDialogListener {

    public static final String OPERATOR = "OPERATOR";

    private ButtonAnimationHelper animationHelper;
    private Calculation calculation;
    private Timer timer;
    private Vibrator vibrator;

    private int currentRound = 0;
    private int rightAnswers = 0;
    private int wrongAnswers = 0;
    private int score = 0;
    private int accumulatedTime = 0;
    private int gameOperator;

    // UI Elements
    private TextView tvDigit1, tvOperator, tvDigit2, tvResult, tvTimer, tvRightAnswer, tvWrongAnswer, tvTotalScore;
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
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
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
            gameOperator = getIntent().getIntExtra(OPERATOR, GameConfig.Operators.PLUS);
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
        tvTotalScore = findViewById(R.id.tv_total_score);
        btnPause = findViewById(R.id.btn_pause);
    }

    /**
     * Initialise les boutons de l'interface du jeu.
     * Cette méthode attache les animations de pression et les écouteurs de clics
     * à tous les boutons, y compris les chiffres, la validation, la suppression et la pause.
     */
    private void setupButtons() {
        animationHelper = new ButtonAnimationHelper(this);
        int[] numberButtonIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9
        };
        for (int i = 0; i < numberButtonIds.length; i++) {
            final String number = String.valueOf(i);
            setupButton(numberButtonIds[i], () -> appendNumber(number));
        }
        setupButton(R.id.btn_valid, this::validateAnswer);
        setupButton(R.id.btn_erase, this::eraseLastDigit);

        // Applique l'animation de pression et l'action de clic au bouton de pause.
        animationHelper.setupButtonAnimation(btnPause, this::showPauseDialog);
    }

    /**
     * Méthode d'aide pour configurer un bouton avec une animation et une action.
     *
     * @param buttonId L'ID de la ressource du bouton (MaterialCardView).
     * @param action   L'action (Runnable) à exécuter lors du clic.
     */
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
        score = 0;
        accumulatedTime = 0;
        updateScoreDisplay();
        if (timer == null) {
            timer = new Timer(this);
        }
        timer.start();
        nextRound();
    }

    private void nextRound() {
        if (currentRound >= GameConfig.Game.VICTORY_ROUND_COUNT) {
            showGameEndDialog(true);
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
        score += 100;
        accumulatedTime += timer.getRemainingTime();
        updateScoreDisplay();
        timer.restart();
        nextRound();
    }

    private void onWrongAnswer() {
        wrongAnswers++;
        score -= 20;
        updateScoreDisplay();
        vibrateOnError();
        if (score <= 0) {
            showGameEndDialog(false);
        }
    }

    private void vibrateOnError() {
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(200);
            }
        }
    }

    private void updateScoreDisplay() {
        tvRightAnswer.setText(String.valueOf(rightAnswers));
        tvWrongAnswer.setText(String.valueOf(wrongAnswers));
        tvTotalScore.setText(String.valueOf(score));
    }

    private void showGameEndDialog(boolean isVictory) {
        new GameEndDialog(this, this, score, accumulatedTime, isVictory).show();
    }

    private void showPauseDialog() {
        if (timer != null) {
            timer.pause();
        }
        new PauseDialog(this, this).show();
    }

    @Override
    public void eachSecondTimer(String currentTime) {
        runOnUiThread(() -> tvTimer.setText(currentTime));
    }

    @Override
    public void onTimerFinish() {
        runOnUiThread(() -> showGameEndDialog(false));
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
    public void onHomeClicked() {
        finish();
    }
}
