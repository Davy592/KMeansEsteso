package com.example.progettomap.custom;

import android.content.Context;


import androidx.appcompat.app.AlertDialog;


/**
 * <h2>Classe che permette di creare un dialog personalizzato</h2>
 */
public class CustomDialog {

    /**
     * <h4>Costruttore privato</h4>
     */
    private CustomDialog(){}


    /**
     * <h4>Metodo che permette di creare un dialog personalizzato</h4>
     *
     * @param context contesto
     * @param title titolo
     * @param message messaggio
     */
    public static void openDialog(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
