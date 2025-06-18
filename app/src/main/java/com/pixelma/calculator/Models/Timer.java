package com.pixelma.calculator.Models;


import android.annotation.SuppressLint;

import com.pixelma.calculator.Interfaces.TimerActions;

public class Timer implements Runnable {

    private boolean isRunning = true;
    private TimerActions timerActions;
    private final int  startSecond = 10;

    private int current;


    public Timer(TimerActions timerActions) {
        this.current = startSecond;
        this.setListenerTimerActions(timerActions);
    }

    @SuppressLint("DefaultLocale")
    private String timeFormat(int currentTime) {
        int minutes = (currentTime / 60);
        int seconds = currentTime % 60;
        return String.format("%02d:%02d", minutes, seconds);

    }

    @Override
    public void run() {

        while (isRunning) {

            current--;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            timerActions.eachSecondTimer(timeFormat(current));

            if(current <= 0){
                timerActions.finish();
                isRunning = false;
            }
        }

    }


    private void setListenerTimerActions(TimerActions timerActions) {
        this.timerActions = timerActions;
    }


    public void restart() {
        current += startSecond;
    }

    public int getTimeSecond() {
        return current;
    }

    public String getInitValue() {
        return this.timeFormat(this.startSecond);
    }

}

