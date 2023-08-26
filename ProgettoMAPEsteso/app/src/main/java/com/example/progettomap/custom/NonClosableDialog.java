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

public class NonClosableDialog extends DialogFragment {
    private View dialogView;
    private TextView textView;

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

    public void setText(String text) {
        if (textView != null) textView.setText(text);
    }

    @Override
    public void dismiss() {
        if (dialogView == null) return;
        dialogView = this.getDialog().getWindow().getDecorView();
        dialogView.postDelayed(() -> {
            if (getDialog() != null)
                getDialog().dismiss();
        }, 500);
    }
}