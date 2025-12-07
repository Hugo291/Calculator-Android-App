package com.pixelma.calculator.Models;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import com.pixelma.calculator.Interfaces.TimerActions;

/**
 * Timer using Handler instead of manual Thread management
 * Safer and more efficient
 */
public class Timer {

    private final Handler handler;
    private final TimerActions timerActions;
    private final int startSecond = 10;
    private int currentSeconds;
    private boolean isRunning = false;
    private Runnable timerRunnable;

    public Timer(TimerActions timerActions) {
        this.handler = new Handler(Looper.getMainLooper());
        this.timerActions = timerActions;
        this.currentSeconds = startSecond;
    }

    /**
     * Start the timer countdown
     */
    public void start() {
        if (isRunning) return;

        isRunning = true;
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isRunning) return;

                currentSeconds--;
                timerActions.eachSecondTimer(timeFormat(currentSeconds));

                if (currentSeconds > 0) {
                    handler.postDelayed(this, 1000);
                } else {
                    isRunning = false;
                    timerActions.onTimerFinish();
                }
            }
        };

        handler.post(timerRunnable);
    }

    /**
     * Pause the timer
     */
    public void pause() {
        isRunning = false;
        if (timerRunnable != null) {
            handler.removeCallbacks(timerRunnable);
        }
    }

    /**
     * Resume the timer
     */
    public void resume() {
        if (!isRunning && currentSeconds > 0) {
            start();
        }
    }

    /**
     * Stop and reset the timer
     */
    public void stop() {
        isRunning = false;
        if (timerRunnable != null) {
            handler.removeCallbacks(timerRunnable);
        }
        currentSeconds = startSecond;
    }

    /**
     * Add time to current countdown
     */
    public void restart() {
        currentSeconds += startSecond;
    }

    /**
     * Format time as MM:SS
     */
    @SuppressLint("DefaultLocale")
    private String timeFormat(int currentTime) {
        int minutes = currentTime / 60;
        int seconds = currentTime % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    // Getters
    public int getRemainingTime() {
        return currentSeconds;
    }

    public boolean isRunning() {
        return isRunning;
    }
}