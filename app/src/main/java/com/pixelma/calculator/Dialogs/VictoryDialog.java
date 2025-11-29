package com.pixelma.calculator.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pixelma.calculator.Interfaces.VictoryDialogListener;
import com.pixelma.calculator.R;

public class VictoryDialog extends AlertDialog {

    private final VictoryDialogListener listener;
    private final int score;

    public VictoryDialog(Context context, VictoryDialogListener listener, int score) {
        super(context);
        this.listener = listener;
        this.score = score;
        init();
    }

    private void init() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_victory, null);
        setView(dialogView);

        TextView victoryScore = dialogView.findViewById(R.id.victory_score);
        victoryScore.setText(String.valueOf(score));

        FrameLayout btnNext = dialogView.findViewById(R.id.btn_next);
        FrameLayout btnHome = dialogView.findViewById(R.id.btn_home);

        btnNext.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNextClicked();
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
