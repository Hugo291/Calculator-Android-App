package com.pixelma.calculator.Activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.card.MaterialCardView;
import com.pixelma.calculator.R;
import com.pixelma.calculator.Utils.GameConfig;

public class MenuActivity extends AppCompatActivity {

    private int shadowOffset;
    private int shadowOffsetPressed;
    private int pressDuration;
    private int releaseDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_menu);

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

        initResource();

        //TODO add google sign

        // Starts a game with the addition operator
        setButtonTouchListener(R.id.btn_plus, () -> startGame(GameConfig.Operators.PLUS));
        // Starts a game with the division operator
        setButtonTouchListener(R.id.btn_divide, () -> startGame(GameConfig.Operators.DIVIDE));
        // Starts a game with the subtraction operator
        setButtonTouchListener(R.id.btn_minus, () -> startGame(GameConfig.Operators.MINUS));
        // Starts a game with the multiplication operator
        setButtonTouchListener(R.id.btn_multi, () -> startGame(GameConfig.Operators.MULTIPLY));
        // Starts a game with a random operator
        setButtonTouchListener(R.id.btn_random, () -> startGame(GameConfig.Operators.RANDOM));

        // ? No action defined
        setButtonTouchListener(R.id.btn_challenge, () -> {});
        // ? No action defined
        setButtonTouchListener(R.id.btn_timer, () -> {});
        // ? No action defined
        setButtonTouchListener(R.id.btn_progress, () -> {});
    }

    public void initResource() {
        shadowOffset = (int) getResources().getDimension(R.dimen.game_button_shadow_offset);
        shadowOffsetPressed = (int) getResources().getDimension(R.dimen.game_button_shadow_offset_pressed);
        pressDuration = getResources().getInteger(R.integer.game_button_press_duration);
        releaseDuration = getResources().getInteger(R.integer.game_button_release_duration);
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

    private void startGame(int operator) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.OPERATOR, operator);
        startActivity(intent);
    }
}
