package com.example.progettomap.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.progettomap.R;
import com.example.progettomap.api.ApiClient;
import com.example.progettomap.custom.NonClosableDialog;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <h2>Classe che gestisce il fragment per il caricamento dei file</h2>
 */

public class LoadFragment extends Fragment {

    /**
     * Variabile che rappresenta il dialog per la selezione del file
     */
    private Dialog fileDialog;

    /**
     * Variabile che rappresenta la TextView per la selezione del file
     */
    private TextView tvFileNames;

    /**
     * Variabile che rappresenta il dialog non chiudibile per i caricamenti
     */
    private NonClosableDialog dialog;

    /**
     * Variabile che rappresenta il bottone per la connessione al server
     */
    private  Button btFileConnect;


    /**
     * <h4>Metodo che gestisce la richiesta dei file presenti sul server</h4>
     *
     * @param inflater           Il LayoutInflater che viene utilizzato per "gonfiare" la View
     * @param container          Se non-null, questo è il padre a cui la View viene agganciata.
     * @param savedInstanceState Se non-null, questo è un precedente stato salvato della View
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fileDialog = new Dialog(requireContext());
        View view = inflater.inflate(R.layout.fragment_load, container, false);
        tvFileNames = view.findViewById(R.id.tvFileNames);
        tvFileNames.setOnClickListener(v -> requestFilesName());
        btFileConnect = view.findViewById(R.id.btFileConnect);
        btFileConnect.setOnClickListener(v -> {
            if (tvFileNames.getText().equals(R.string.seleziona_file)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle(R.string.errore);
                builder.setMessage(R.string.seleziona_file);
                builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
                builder.show();
            } else {
                String fileName = tvFileNames.getText().toString();
                List<String> list = new LinkedList<>();
                list.add(fileName);
                Call<List<String>> call = ApiClient.getApiService().sendFileToServer(list);
                call.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        if (response.isSuccessful()) {
                            ListView listResult = view.findViewById(R.id.listResult);
                            String[] responseString = response.body().get(0).split("\n");
                            Toast.makeText(requireContext(), R.string.risultati_arrivati_correttamente, Toast.LENGTH_SHORT).show();
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, responseString);
                            listResult.setAdapter(adapter);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setTitle(R.string.errore);
                            builder.setMessage(R.string.connessione_non_riuscita);
                            builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
                            builder.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle(R.string.errore);
                        builder.setMessage(R.string.connessione_non_riuscita+": " + t.getMessage());
                        builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
                        builder.show();
                    }
                });
            }
        });
        return view;
    }

    /**
     * <h4>Metodo che gestisce la richiesta dei file presenti sul server</h4>
     */
    private void requestFilesName() {
        dialog = new NonClosableDialog();
        dialog.show(getParentFragmentManager(), R.string.tentativo_di_connessione+"");
        Call<List<String>> call = ApiClient.getApiService().requestFilesName();
        List<String> list = new LinkedList<>();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    list.addAll(response.body());
                    showFileDialog(list);
                } else {
                    dialog.dismiss();
                    Toast.makeText(requireContext(), R.string.connessione_non_riuscita, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(requireContext(), R.string.connessione_non_riuscita, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * <h4>Metodo che gestisce la visualizzazione del dialog per la selezione del file</h4>
     *
     * @param fileNames Lista dei file presenti sul server
     */
    private void showFileDialog(List<String> fileNames) {
        dialog.dismiss();
        fileDialog.setContentView(R.layout.dialog_searchable_spinner);
        fileDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ListView listView = fileDialog.findViewById(R.id.list_view);
        EditText editText = fileDialog.findViewById(R.id.edit_text);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_list_item_1, fileNames);
        listView.setAdapter(adapter);
        fileDialog.show();

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            tvFileNames.setText(adapter.getItem(i));
            fileDialog.dismiss();
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}