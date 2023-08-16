package com.example.progettomap.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.progettomap.R;
import com.example.progettomap.api.ApiClient;
import com.example.progettomap.custom.RangeInputFilter;

/**
 * <h2>Fragment che permette di cambiare il server</h2>
 */
public class ServerFragment extends Fragment {

    /**
     * Permette la comunicazione tra fragment e activity
     */
    private FragmentToActivity dataPasser;

    /**
     * <h4>Metodo che permette di creare la view del fragment</h4>
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState savedInstanceState
     * @return view del fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_server, container, false);
        Button btChangeServer = view.findViewById(R.id.btChangeServer);
        EditText tbServerIP = view.findViewById(R.id.tbServerIP);
        EditText tbServerPORT = view.findViewById(R.id.tbServerPORT);
        tbServerPORT.setFilters(new InputFilter[] { new RangeInputFilter(0,65535)});
        btChangeServer.setOnClickListener(v -> {
            if (tbServerIP.getText().toString().equals("") || tbServerPORT.getText().toString().equals("")) {
                openDialog("ERRORE", "Inserire tutti i campi!");
            } else {
                ApiClient.closeClient();
                ApiClient.setBaseUrl(tbServerIP.getText().toString(), Integer.parseInt(tbServerPORT.getText().toString()));
                ApiClient.updateServer();
                Toast.makeText(requireContext(), "Server cambiato correttamente", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }


    /**
     * <h4>Metodo che permette di attaccare il fragment all'activity</h4>
     * @param context context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (FragmentToActivity) context;
    }

    /**
     * <h4>Metodo che permette di aprire un dialog</h4>
     * @param titolo titolo del dialog
     * @param messaggio messaggio del dialog
     */
    private void openDialog(String titolo, String messaggio) {
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
        alertDialog.setTitle(titolo);
        alertDialog.setMessage(messaggio);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

}