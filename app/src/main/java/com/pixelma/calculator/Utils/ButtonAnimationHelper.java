package com.pixelma.calculator.Utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.card.MaterialCardView;
import com.pixelma.calculator.R;

/**
 * Helper class to handle button press animations
 * Eliminates code duplication between MenuActivity and GameActivity
 */
public class ButtonAnimationHelper {

    private final int shadowOffset;
    private final int shadowOffsetPressed;
    private final int pressDuration;
    private final int releaseDuration;

    public ButtonAnimationHelper(Context context) {
        this.shadowOffset = context.getResources()
                .getDimensionPixelSize(R.dimen.game_button_shadow_offset);
        this.shadowOffsetPressed = context.getResources()
                .getDimensionPixelSize(R.dimen.game_button_shadow_offset_pressed);
        this.pressDuration = context.getResources()
                .getInteger(R.integer.game_button_press_duration);
        this.releaseDuration = context.getResources()
                .getInteger(R.integer.game_button_release_duration);
    }

    /**
     * Setup animation for a button
     * @param button The MaterialCardView button
     * @param action The action to perform on click
     */
    public void setupButtonAnimation(MaterialCardView button, Runnable action) {
        button.setOnTouchListener(new ButtonTouchListener(action));
    }

    /**
     * Setup animation for a generic view
     *
     * @param button The View to animate
     * @param action The action to perform on click
     */
    public void setupButtonAnimation(View button, Runnable action) {
        button.setOnTouchListener(new ViewTouchListener(action));
    }

    /**
     * Animate button press/release
     * @param view The view to animate
     * @param from Starting offset value
     * @param to Ending offset value
     * @param duration Animation duration
     */
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

    /**
     * Touch listener that handles press animation and action execution for MaterialCardView
     */
    private class ButtonTouchListener implements View.OnTouchListener {
        private final Runnable action;

        ButtonTouchListener(Runnable action) {
            this.action = action;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    animateButton(view, shadowOffset, shadowOffsetPressed, pressDuration);
                    return true;

                case MotionEvent.ACTION_UP:
                    animateButton(view, shadowOffsetPressed, shadowOffset, releaseDuration);
                    if (action != null) {
                        action.run();
                    }
                    return true;

                case MotionEvent.ACTION_CANCEL:
                    animateButton(view, shadowOffsetPressed, shadowOffset, releaseDuration);
                    return true;
            }
            return false;
        }
    }

    /**
     * Touch listener that handles press animation and action execution for a generic View
     */
    private class ViewTouchListener implements View.OnTouchListener {
        private final Runnable action;

        ViewTouchListener(Runnable action) {
            this.action = action;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    view.animate().scaleX(0.9f).scaleY(0.9f).setDuration(pressDuration).start();
                    return true;

                case MotionEvent.ACTION_UP:
                    view.animate().scaleX(1f).scaleY(1f).setDuration(releaseDuration).start();
                    if (action != null) {
                        action.run();
                    }
                    return true;

                case MotionEvent.ACTION_CANCEL:
                    view.animate().scaleX(1f).scaleY(1f).setDuration(releaseDuration).start();
                    return true;
            }
            return false;
        }
    }
}
