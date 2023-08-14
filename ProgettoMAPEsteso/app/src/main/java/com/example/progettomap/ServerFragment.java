package com.example.progettomap;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ServerFragment extends Fragment {
    FragmentToActivity dataPasser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_server, container, false);
        Button btChangeServer = view.findViewById(R.id.btChangeServer);
        EditText tbServerIP = view.findViewById(R.id.tbServerIP);
        EditText tbServerPORT = view.findViewById(R.id.tbServerPORT);

        btChangeServer.setOnClickListener(v -> {
            if (tbServerIP.getText().toString().equals("") || tbServerPORT.getText().toString().equals("")) {
                openDialog("ERRORE", "Inserire tutti i campi!");
            } else {
                ApiClient.closeClient();
                ApiClient.setBaseUrl(tbServerIP.getText().toString(), Integer.parseInt(tbServerPORT.getText().toString()));
                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                dataPasser.updateServer(apiService);
                Toast.makeText(requireContext(), "Server cambiato correttamente", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (FragmentToActivity) context;
    }

    private void openDialog(String titolo, String messaggio) {
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
        alertDialog.setTitle(titolo);
        alertDialog.setMessage(messaggio);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

}