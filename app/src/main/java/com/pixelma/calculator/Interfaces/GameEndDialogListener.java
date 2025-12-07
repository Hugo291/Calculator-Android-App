package com.pixelma.calculator.Interfaces;

/**
 * Interface pour écouter les événements du dialogue de fin de partie.
 */
public interface GameEndDialogListener {
    /**
     * Appelé lorsque le bouton de redémarrage est cliqué.
     */
    void onRestartClicked();

    /**
     * Appelé lorsque le bouton d'accueil est cliqué.
     */
    void onHomeClicked();
}
