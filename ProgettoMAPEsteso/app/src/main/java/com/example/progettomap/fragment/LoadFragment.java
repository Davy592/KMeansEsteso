package com.example.progettomap.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <h2>Fragment che permette di caricare un file dal server</h2>
 */
public class LoadFragment extends Fragment {

    /**
     * <h4>Metodo che permette di creare la view del fragment</h4>
     *
     * @param inflater           inflater
     * @param container          container
     * @param savedInstanceState savedInstanceState
     * @return view del fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load, container, false);
        TextView tvFileNames = view.findViewById(R.id.tvFileNames);
        final Dialog fileDialog = new Dialog(requireContext());
        tvFileNames.setOnClickListener(v -> {
            fileDialog.setContentView(R.layout.dialog_searchable_spinner);
            fileDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ListView listView = fileDialog.findViewById(R.id.list_view);
            EditText editText = fileDialog.findViewById(R.id.edit_text);
            List<String> FileArray = requestFilesName();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(), android.R.layout.simple_list_item_1, FileArray);
            listView.setAdapter(adapter);
            fileDialog.show();
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

            listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                tvFileNames.setText(adapter.getItem(i));
                fileDialog.dismiss();
            });

            new Handler().postDelayed(adapter::notifyDataSetChanged, 200);
        });

        Button btFileConnect = view.findViewById(R.id.btFileConnect);
        btFileConnect.setOnClickListener(v -> {
            System.out.println(tvFileNames.getText());
            if (tvFileNames.getText().equals("SELEZIONA FILE")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Errore!");
                builder.setMessage("Seleziona un file");
                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
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
                            if (response.body().get(0).equals("ERRORE")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                                builder.setTitle("Errore!");
                                builder.setMessage("Connessione non riuscita: " + response.body().get(1));
                                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                                builder.show();
                            } else {
                                ListView listResult = view.findViewById(R.id.listResult);
                                String[] responseString = response.body().get(0).split("\n");
                                Toast.makeText(requireContext(), "Risultati caricati correttamente!", Toast.LENGTH_SHORT).show();
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, responseString);
                                listResult.setAdapter(adapter);
                            }
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setTitle("Errore!");
                            builder.setMessage("Connessione non riuscita");
                            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                            builder.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle("Errore!");
                        builder.setMessage("Connessione non riuscita: " + t.getMessage());
                        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                        builder.show();
                    }
                });
            }
        });
        return view;
    }

    /**
     * <h4>Metodo che permette di richiedere i nomi dei file presenti sul server</h4>
     *
     * @return lista dei nomi dei file presenti sul server
     */
    private List<String> requestFilesName() {
        Call<List<String>> call = ApiClient.getApiService().requestFilesName();
        List<String> list = new LinkedList<>();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                list.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Errore!");
                builder.setMessage("Connessione non riuscita: " + t.getMessage());
                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                builder.show();
            }
        });
        return list;
    }
}
