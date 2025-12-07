package com.pixelma.calculator.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.pixelma.calculator.Interfaces.PauseDialogListener;
import com.pixelma.calculator.R;

/**
 * Dialogue affiché lorsque le jeu est en pause.
 */
public class PauseDialog extends AlertDialog {

    private final PauseDialogListener listener;

    /**
     * Constructeur pour le dialogue de pause.
     *
     * @param context  Le contexte de l'application.
     * @param listener L'écouteur pour les événements du dialogue.
     */
    public PauseDialog(Context context, PauseDialogListener listener) {
        super(context);
        this.listener = listener;
        init();
    }

    private void init() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pause, null);
        setView(dialogView);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        FrameLayout btnResume = dialogView.findViewById(R.id.btn_resume);
        FrameLayout btnQuit = dialogView.findViewById(R.id.btn_quit);

        btnResume.setOnClickListener(v -> {
            if (listener != null) {
                listener.onResumeClicked();
            }
            dismiss();
        });

        btnQuit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onQuitClicked();
            }
            dismiss();
        });

        setCancelable(false);
    }
}
