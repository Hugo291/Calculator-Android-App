package com.pixelma.calculator.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pixelma.calculator.Interfaces.GameOverDialogListener;
import com.pixelma.calculator.R;

public class GameOverDialog extends AlertDialog {

    private final GameOverDialogListener listener;
    private final int score;

    public GameOverDialog(Context context, GameOverDialogListener listener, int score) {
        super(context);
        this.listener = listener;
        this.score = score;
        init();
    }

    private void init() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_game_over, null);
        setView(dialogView);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView finalScore = dialogView.findViewById(R.id.final_score);
        finalScore.setText(String.valueOf(score));

        FrameLayout btnRestart = dialogView.findViewById(R.id.btn_restart);
        FrameLayout btnHome = dialogView.findViewById(R.id.btn_home);

        btnRestart.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRestartClicked();
            }
            dismiss();
        });

        btnHome.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHomeClicked();
            }
            dismiss();
        });

        setCancelable(false);
    }
}
