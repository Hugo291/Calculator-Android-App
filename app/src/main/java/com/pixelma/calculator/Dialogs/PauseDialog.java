package com.pixelma.calculator.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

        setButtonAnimation(btnResume);
        setButtonAnimation(btnQuit);

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

    /**
     * Applique une animation de pression à un bouton.
     * Le bouton est mis à l'échelle lorsqu'il est pressé et revient à sa taille normale lorsqu'il est relâché.
     *
     * @param button Le bouton (FrameLayout) auquel appliquer l'animation.
     */
    private void setButtonAnimation(FrameLayout button) {
        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Le bouton est pressé, on le réduit.
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // Le bouton est relâché ou le contact est annulé, on le remet à sa taille normale.
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            // Retourne false pour ne pas consommer l'événement et permettre au OnClickListener de fonctionner.
            return false;
        });
    }
}
