package com.pixelma.calculator.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.pixelma.calculator.Interfaces.GameEndDialogListener;
import com.pixelma.calculator.R;

/**
 * Dialogue affiché à la fin d'une partie, que ce soit une victoire ou une défaite.
 */
public class GameEndDialog extends AlertDialog {

    private final GameEndDialogListener listener;
    private final int score;
    private final int accumulatedTime;
    private final boolean isVictory;

    /**
     * Constructeur pour le dialogue de fin de partie.
     *
     * @param context         Le contexte de l'application.
     * @param listener        L'écouteur pour les événements du dialogue.
     * @param score           Le score final du joueur.
     * @param accumulatedTime Le temps total accumulé.
     * @param isVictory       Vrai si c'est une victoire, faux sinon.
     */
    public GameEndDialog(Context context, GameEndDialogListener listener, int score, int accumulatedTime, boolean isVictory) {
        super(context);
        this.listener = listener;
        this.score = score;
        this.accumulatedTime = accumulatedTime;
        this.isVictory = isVictory;
        init();
    }

    private void init() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_game_end, null);
        setView(dialogView);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView title = dialogView.findViewById(R.id.game_end_title);
        TextView scoreTv = dialogView.findViewById(R.id.game_end_score);
        TextView timeTv = dialogView.findViewById(R.id.game_end_time);
        FrameLayout btnNext = dialogView.findViewById(R.id.btn_next);
        TextView btnNextText = dialogView.findViewById(R.id.btn_next_text);
        FrameLayout btnHome = dialogView.findViewById(R.id.btn_home);
        CardView card = dialogView.findViewById(R.id.game_end_card);

        scoreTv.setText(String.valueOf(score));
        timeTv.setText(formatTime(accumulatedTime));

        if (isVictory) {
            title.setText("VICTORY");
            btnNextText.setText("NEXT");
            card.setCardBackgroundColor(getContext().getResources().getColor(R.color.button_green_mint, null));
            title.setTextColor(getContext().getResources().getColor(R.color.white, null));
            scoreTv.setTextColor(getContext().getResources().getColor(R.color.white, null));
            timeTv.setTextColor(getContext().getResources().getColor(R.color.white, null));
        } else {
            title.setText("GAME OVER");
            btnNextText.setText("REJOUER");
            card.setCardBackgroundColor(getContext().getResources().getColor(R.color.button_red_coral, null));
            title.setTextColor(getContext().getResources().getColor(R.color.white, null));
            scoreTv.setTextColor(getContext().getResources().getColor(R.color.white, null));
            timeTv.setTextColor(getContext().getResources().getColor(R.color.white, null));
        }

        btnNext.setOnClickListener(v -> {
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

    private String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
}
