package com.pixelma.calculator.Activities;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.card.MaterialCardView;
import com.pixelma.calculator.Interfaces.TimerActions;
import com.pixelma.calculator.Models.Calculation;
import com.pixelma.calculator.Models.Timer;
import com.pixelma.calculator.R;

public class GameActivity extends AppCompatActivity {

    private Calculation calculation;
    private Timer timer;

    public final static String OPERATOR = "OPERATOR";

    private int currentRound = 0;

    private TextView tvCalculationPart1;
    private TextView tvCalculationOperator;
    private TextView tvCalculationPart2;
    private TextView tvCalculationEquals;
    private TextView tvCalculationQuestion;
    private TextView tvRightAnswer;
    private TextView tvWrongAnswer;
    private TextView tvTimer;
    private TextView tvLevel;

    private ImageView ivRightAnswer;
    private ImageView ivWrongAnswer;

    private int shadowOffset;
    private int shadowOffsetPressed;
    private int pressDuration;
    private int releaseDuration;

    public final String TAG = "TAG_GameActivity";

    private int gameOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_game);

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

        getParamsAct();
        initResource();

        onStartGame();

        new Thread(timer = new Timer(new TimerActions() {
            @Override
            public void eachSecondTimer(final String currentTime) {
                runOnUiThread(() -> {
                    tvTimer.setText(currentTime);
                    Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
                    tvTimer.startAnimation(aniSlide);
                });
            }

            @Override
            public void finish() {
            }
        })).start();

    }

    public void getParamsAct() {
        if (getIntent() != null) {
            gameOperator = getIntent().getIntExtra(OPERATOR, 0);
        }
    }

    public void onStartGame() {
        currentRound = 0;
        tvRightAnswer.setText("0");
        tvWrongAnswer.setText("0");
        updateLevelText();
        calculation = new Calculation(currentRound, this.gameOperator);
        setCalculation(calculation);
    }

    public void onRightAnswer() {
        addRightAnswer();
        if (currentRound == 2) { // Changed from 14 to 2
            showVictoryDialog();
            return;
        }
        nextRound();
        updateLevelText();
        calculation = new Calculation(currentRound, this.gameOperator);
        setCalculation(calculation);

        if (timer != null) {
            timer.restart();
        }

        Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        tvRightAnswer.startAnimation(aniSlide);
    }

    public void onWrongAnswer() {
        addWrongAnswer();
        Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        tvWrongAnswer.startAnimation(aniSlide);
    }

    private void showVictoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_victory, null);
        builder.setView(dialogView);

        TextView victoryScore = dialogView.findViewById(R.id.victory_score);
        victoryScore.setText(String.format("%03d", Integer.parseInt(tvRightAnswer.getText().toString())));

        Button btnNext = dialogView.findViewById(R.id.btn_next);
        Button btnHome = dialogView.findViewById(R.id.btn_home);

        final AlertDialog dialog = builder.create();

        btnNext.setOnClickListener(v -> {
            onStartGame();
            dialog.dismiss();
        });

        btnHome.setOnClickListener(v -> {
            finish();
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void updateLevelText() {
        tvLevel.setText("Niv " + (currentRound + 1) + "/3");
    }

    public void setUserResult(String str) {
        tvCalculationQuestion.setText(str);
    }

    public void removeCharUserResult() {
        String currentText = tvCalculationQuestion.getText().toString();
        if (currentText.equals("?")) {
            return;
        }
        String newText = removeLastChar(currentText);
        if (newText.isEmpty()) {
            setUserResult("?");
        } else {
            setUserResult(newText);
        }
    }

    public void addRightAnswer() {
        int currentScore = Integer.parseInt(tvRightAnswer.getText().toString());
        tvRightAnswer.setText(String.valueOf(currentScore + 1));
    }

    public void addWrongAnswer() {
        int currentScore = Integer.parseInt(tvWrongAnswer.getText().toString());
        tvWrongAnswer.setText(String.valueOf(currentScore + 1));
    }

    public void appendCharUserResult(String str) {
        String currentText = tvCalculationQuestion.getText().toString();
        if (currentText.equals("?")) {
            tvCalculationQuestion.setText(str);
        } else if (currentText.length() < 3) {
            tvCalculationQuestion.append(str);
        }
    }

    public void setCalculation(Calculation calculation) {
        String[] parts = calculation.getCalculationString().split(" ");
        if (parts.length >= 3) {
            tvCalculationPart1.setText(parts[0]);
            tvCalculationOperator.setText(parts[1]);
            tvCalculationPart2.setText(parts[2]);
            tvCalculationEquals.setText("=");
            tvCalculationQuestion.setText("?");
        }
    }

    public int getUserResultValue() {
        try {
            String resultText = tvCalculationQuestion.getText().toString();
            if (resultText.equals("?")) {
                return -1; // Use a value unlikely to be a correct answer
            }
            return Integer.parseInt(resultText);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String removeLastChar(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        return str.substring(0, str.length() - 1);
    }

    private void animateButton(View view, int from, int to, int duration) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(duration);

        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            params.setMarginStart(shadowOffset - value);
            params.topMargin = shadowOffset - value;
            params.setMarginEnd(value);
            params.bottomMargin = value;
            view.setLayoutParams(params);
        });
        animator.start();
    }

    private void setButtonTouchListener(int buttonId, Runnable action) {
        MaterialCardView button = findViewById(buttonId);
        button.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    animateButton(view, shadowOffset, shadowOffsetPressed, pressDuration);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    animateButton(view, shadowOffsetPressed, shadowOffset, releaseDuration);
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        action.run();
                    }
                    return true;
            }
            return false;
        });
    }

    public void initResource() {
        shadowOffset = (int) getResources().getDimension(R.dimen.game_button_shadow_offset);
        shadowOffsetPressed = (int) getResources().getDimension(R.dimen.game_button_shadow_offset_pressed);
        pressDuration = getResources().getInteger(R.integer.game_button_press_duration);
        releaseDuration = getResources().getInteger(R.integer.game_button_release_duration);

        tvCalculationPart1 = findViewById(R.id.calculation_part_1);
        tvCalculationOperator = findViewById(R.id.calculation_operator);
        tvCalculationPart2 = findViewById(R.id.calculation_part_2);
        tvCalculationEquals = findViewById(R.id.calculation_equals);
        tvCalculationQuestion = findViewById(R.id.calculation_question);

        tvRightAnswer = findViewById(R.id.right_answer);
        tvWrongAnswer = findViewById(R.id.wrong_answer);
        tvTimer = findViewById(R.id.timer);
        tvLevel = findViewById(R.id.level);

        // Set listeners for number buttons
        setButtonTouchListener(R.id.btn_1, () -> appendCharUserResult("1"));
        setButtonTouchListener(R.id.btn_2, () -> appendCharUserResult("2"));
        setButtonTouchListener(R.id.btn_3, () -> appendCharUserResult("3"));
        setButtonTouchListener(R.id.btn_4, () -> appendCharUserResult("4"));
        setButtonTouchListener(R.id.btn_5, () -> appendCharUserResult("5"));
        setButtonTouchListener(R.id.btn_6, () -> appendCharUserResult("6"));
        setButtonTouchListener(R.id.btn_7, () -> appendCharUserResult("7"));
        setButtonTouchListener(R.id.btn_8, () -> appendCharUserResult("8"));
        setButtonTouchListener(R.id.btn_9, () -> appendCharUserResult("9"));
        setButtonTouchListener(R.id.btn_0, () -> appendCharUserResult("0"));

        setButtonTouchListener(R.id.btn_valid, () -> {
            if (getUserResultValue() == calculation.getResult()) {
                onRightAnswer();
            } else {
                onWrongAnswer();
            }
        });

        setButtonTouchListener(R.id.btn_erase, this::removeCharUserResult);
    }

    private void nextRound() {
        currentRound++;
    }
}
