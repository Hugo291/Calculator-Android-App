package com.pixelma.calculator.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pixelma.calculator.Interfaces.TimerActions;
import com.pixelma.calculator.Models.Calculation;
import com.pixelma.calculator.Models.Timer;
import com.pixelma.calculator.R;

public class GameActivity extends AppCompatActivity {

    private Calculation calculation;
    private Timer timer;

    public final static String OPERATOR = "OPERATOR";

    private int currentRound = 0;

    private TextView tvCalculation;
    private TextView tvUserResult;
    private TextView tvRightAnswer;
    private TextView tvWrongAnswer;
    private TextView tvTimer;

    private ImageView ivRightAnswer;
    private ImageView ivWrongAnswer;

    public final String TAG = "TAG_GameActivity";

    private int gameOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getParamsAct();
        initResource();

        onStartGame();

        new Thread(timer = new Timer(new TimerActions() {
            @Override
            public void eachSecondTimer(final String currentTime) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTimer.setText(currentTime);
                    }
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
        calculation = new Calculation(currentRound, this.gameOperator);
        setUserResult("?");
        setCalculation(calculation);
    }

    public void onRightAnswer() {
        calculation = new Calculation(currentRound, this.gameOperator);
        setUserResult("?");
        nextRound();
        addRightAnswer();
        setCalculation(calculation);

        if (timer != null) {
            timer.restart();

        }

        Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        ivRightAnswer.startAnimation(aniSlide);
    }

    public void onWrongAnswer() {

        addWrongAnswer();
        Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        ivWrongAnswer.startAnimation(aniSlide);

    }

    public void setUserResult(String str) {
        tvUserResult.setText(str);
    }

    public void removeCharUserResult() {
        String str = removeLastChar((String) tvUserResult.getText());
        setUserResult(str);
        if (str.isEmpty()) {
            setUserResult("?");
        }
    }

    public void addRightAnswer() {
        tvRightAnswer.setText(Integer.parseInt((String) tvRightAnswer.getText()) + 1 + "");

    }

    public void addWrongAnswer() {
        tvWrongAnswer.setText(Integer.parseInt((String) tvWrongAnswer.getText()) + 1 + "");
    }

    public void appendCharUserResult(String str) {
        if (tvUserResult.getText().equals("?")) {
            tvUserResult.setText("");
        }
        setUserResult(tvUserResult.getText() + str);
    }

    public void setCalculation(Calculation calculation) {
        tvCalculation.setText(calculation.getCalculationString() + " = ");
    }


    public int getUserResultValue() {
        try {
            return Integer.parseInt(tvUserResult.getText().toString().isEmpty() ? "0" : tvUserResult.getText().toString());

        } catch (Exception e) {
            return 0;
        }
    }

    public String removeLastChar(String str) {
        if (str.length() < 1)
            return "";
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < str.length() - 1; i++) {
            output.append(str.charAt(i));
        }
        return output.toString();
    }

    public void initResource() {

        tvUserResult = findViewById(R.id.user_result);
        tvCalculation = findViewById(R.id.calculation);
        tvRightAnswer = findViewById(R.id.right_answer);
        tvWrongAnswer = findViewById(R.id.wrong_answer);

        ivWrongAnswer = findViewById(R.id.iv_wrong_answer);
        ivRightAnswer = findViewById(R.id.iv_right_answer);

        tvTimer = findViewById(R.id.timer);

        final LinearLayout linear = findViewById(R.id.group_number);

        for (int z = 0; z < linear.getChildCount(); z++) {

            LinearLayout grid = (LinearLayout) linear.getChildAt(z);

            for (int i = 0; i < grid.getChildCount(); i++) {

                if (grid.getChildAt(i) instanceof ImageButton)
                    continue;

                final String text = (String) ((TextView) grid.getChildAt(i)).getText();
                final Button button = (Button) grid.getChildAt(i);

                try {
                    Integer.parseInt(text);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            appendCharUserResult(text);
                        }
                    });
                } catch (NumberFormatException ignored) {

                }

                grid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        final int heightButton = (linear.getMeasuredHeight() / 4) - 3;

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                heightButton,
                                1);
                        button.setLayoutParams(params);
                        button.setTextSize(heightButton/6);
                    }
                });
            }
        }

        findViewById(R.id.valid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getUserResultValue() == calculation.getResult()) {
                    onRightAnswer();
                } else {
                    onWrongAnswer();
                }
            }
        });

        findViewById(R.id.erase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCharUserResult();
            }
        });
    }

    private void nextRound() {
        currentRound++;
    }
}
