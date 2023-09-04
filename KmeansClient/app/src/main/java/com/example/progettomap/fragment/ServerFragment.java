package com.example.progettomap.fragment;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.progettomap.R;
import com.example.progettomap.api.ApiClient;
import com.example.progettomap.custom.RangeInputFilter;

/**
 * <h2>La classe ServerFragment gestisce il Fragment che permette di cambiare il server</h2>
 */
public class ServerFragment extends Fragment {

    /**
     * Button per la connessione al server
     */
    private Button btChangeServer;

    /**
     * EditText per l'ip del server
     */
    private EditText tbServerIP;

    /**
     * EditText per la porta del server
     */
    private EditText tbServerPORT;

    /**
     * TextView che mostra il server a cui si e' attualmente connessi
     */
    private TextView tvActualServer;


    /**
     * <h4>Crea la view del fragment</h4>
     *
     * @param inflater           oggetto che permette di "gonfiare" un layout XML in una View corrispondente
     * @param container          Se non-null, questo e' il padre a cui la View viene agganciata.
     * @param savedInstanceState Se non-null, questo e' un precedente stato salvato della View
     * @return la view del fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_server, container, false);
        btChangeServer = view.findViewById(R.id.btChangeServer);
        tbServerIP = view.findViewById(R.id.tbServerIP);
        tbServerPORT = view.findViewById(R.id.tbServerPORT);
        tbServerPORT.setFilters(new InputFilter[]{new RangeInputFilter(0, 65535)});
        tvActualServer = view.findViewById(R.id.tvActualServer);
        tvActualServer.setText(getResources().getString(R.string.server_attuale, ApiClient.getBaseUrl().substring(7, ApiClient.getBaseUrl().length()-1)));
        btChangeServer.setOnClickListener(v -> {
            if (tbServerIP.getText().toString().equals("") || tbServerPORT.getText().toString().equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle(R.string.errore);
                builder.setMessage(R.string.inserire_tutti_i_campi);
                builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
                builder.show();
            } else {
                ApiClient.closeClient();
                ApiClient.setBaseUrl(tbServerIP.getText().toString(), Integer.parseInt(tbServerPORT.getText().toString()));
                ApiClient.updateServer();
                Toast.makeText(requireContext(), getResources().getString(R.string.server_cambiato_correttamente), Toast.LENGTH_LONG).show();
                tvActualServer.setText(getResources().getString(R.string.server_attuale, ApiClient.getBaseUrl().substring(7, ApiClient.getBaseUrl().length()-1)));
            }
        });

        return view;
    }


}