package com.example.progettomap.custom;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.progettomap.R;

/**
 * <h2>Classe che gestisce il dialog non chiudibile</h2>
 */
public class NonClosableDialog extends DialogFragment {

    /**
     * Variabile che rappresenta la View del dialog
     */
    private View dialogView;

    /**
     * Variabile che rappresenta la TextView del dialog
     */
    private TextView textView;


    /**
     * <h4>Metodo che crea il dialog</h4>
     *
     * @param savedInstanceState Se non-null, questo è un precedente stato salvato della View
     * @return Il dialog creato
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_layout, null);
        textView = dialogView.findViewById(R.id.messageTextView);
        builder.setView(dialogView);
        setCancelable(false);
        return builder.create();
    }

    /**
     * <h4>Metodo che imposta il testo del dialog</h4>
     *
     * @param text Il testo da impostare
     */
    public void setText(String text) {
        if (textView != null) textView.setText(text);
    }

    /**
     * <h4>Metodo che chiude il dialog</h4>
     */
    @Override
    public void dismiss() {
        if (dialogView == null) return;
        dialogView = this.getDialog().getWindow().getDecorView();
        dialogView.postDelayed(() -> {
            if (getDialog() != null)
                getDialog().dismiss();
        }, 750);
    }
}