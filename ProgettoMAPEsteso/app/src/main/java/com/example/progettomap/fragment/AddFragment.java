package com.example.progettomap.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.progettomap.R;
import com.example.progettomap.api.ApiClient;
import com.example.progettomap.custom.CustomizedExpandableListAdapter;
import com.example.progettomap.custom.NonClosableDialog;
import com.example.progettomap.custom.RangeInputFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * <h2>Classe che gestisce il fragment per la creazione di un nuovo cluster</h2>
 */
public class AddFragment extends Fragment {


    /**
     * Variabile che rappresenta l'EditText del server
     */
    private EditText tbServer;

    /**
     * Variabile che rappresenta l'EditText della porta del server
     */
    private EditText tbPort;

    /**
     * Variabile che rappresenta l'EditText del database
     */
    private EditText tbDatabase;

    /**
     * Variabile che rappresenta l'EditText della tabella
     */
    private EditText tbTable;

    /**
     * Variabile che rappresenta l'EditText dell'utente
     */
    private EditText tbUser;

    /**
     * Variabile che rappresenta l'EditText della password
     */
    private EditText tbEditPassword;

    /**
     * Variabile che rappresenta il bottone per la connessione
     */
    private Button btConnect;


    /**
     * Variabile che rappresenta il botttone per il reset delle informazioni
     */
    private Button btDbInfoReset;

    /**
     * Variabile che rappresenta il bottone per il calcolo del cluster
     */
    private Button btCalc;


    /**
     * Variabile che rappresenta la lista per il numero di cluster
     */
    private  ExpandableListView expandableListView;

    /**
     * Variabile che rappresenta il layout per le informazioni del database
     */
    private ConstraintLayout infoLayout;

    /**
     * Variabile che rappresenta il layout per la selezione del cluster
     */
    private ConstraintLayout clusterLayout;

    /**
     * Variabile che rappresenta il layout per il risultato
     */
    private ConstraintLayout resultLayout;

    /**
     * Variabile che rappresenta il testo per il numero di cluster
     */
    private TextView spinnerCluster;

    /**
     * <h4> Metodo che crea il fragment</h4>
     *
     * @param inflater           oggetto che permette di "gonfiare" un layout XML in una View corrispondente
     * @param container          se non nullo, questo fragment viene ricostruito da uno stato precedente salvato come valore in questo bundle.
     * @param savedInstanceState se non nullo, questo fragment viene ricostruito da uno stato precedente salvato come valore in questo bundle.
     * @return la View del fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        tbServer = view.findViewById(R.id.tbServer);
        tbPort = view.findViewById(R.id.tbPort);
        tbDatabase = view.findViewById(R.id.tbDatabase);
        tbTable = view.findViewById(R.id.tbTable);
        tbUser = view.findViewById(R.id.tbUser);
        tbEditPassword = view.findViewById(R.id.tbEditPassword);
        btConnect = view.findViewById(R.id.btConnect);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (tbServer.getText().toString().equals("") && tbPort.getText().toString().equals("") && tbDatabase.getText().toString().equals("") && tbTable.getText().toString().equals("") && tbUser.getText().toString().equals("") && tbEditPassword.getText().toString().equals("")) {
                    btConnect.setText(R.string.usa_predefiniti);
                } else {
                    btConnect.setText(R.string.conn);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };


        tbDatabase.addTextChangedListener(textWatcher);
        tbTable.addTextChangedListener(textWatcher);
        tbUser.addTextChangedListener(textWatcher);
        tbServer.addTextChangedListener(textWatcher);
        tbPort.addTextChangedListener(textWatcher);
        tbEditPassword.addTextChangedListener(textWatcher);

        expandableListView = view.findViewById(R.id.expandableListView);
        infoLayout = view.findViewById(R.id.infoLayout);
        clusterLayout = view.findViewById(R.id.clusterLayout);
        resultLayout = view.findViewById(R.id.resultLayout);
        btDbInfoReset = view.findViewById(R.id.btDbInfoReset);

        tbDatabase.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tbDatabase.setHint(R.string.db_hint);
            } else {
                tbDatabase.setHint(R.string.database);
            }
        });
        tbTable.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tbTable.setHint(R.string.tabella_hint);
            } else {
                tbTable.setHint(R.string.tabella);
            }
        });
        tbUser.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tbUser.setHint(R.string.utente_hint);
            } else {
                tbUser.setHint(R.string.utente);
            }
        });
        tbPort.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tbPort.setHint(R.string.porta_hint);
            } else {
                tbPort.setHint(R.string.porta);
            }
        });
        tbServer.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tbServer.setHint(R.string.server_hint);
            } else {
                tbServer.setHint(R.string.server);
            }
        });
        tbPort.setFilters(new InputFilter[]{new RangeInputFilter(0, 65535)});
        btDbInfoReset.setOnClickListener(v -> {
            tbServer.setText("");
            tbPort.setText("");
            tbDatabase.setText("");
            tbTable.setText("");
            tbUser.setText("");
            tbEditPassword.setText("");
            btConnect.setText(R.string.usa_predefiniti);
        });


            btConnect.setOnClickListener(v -> {
            if (tbServer.getText().toString().equals("") && tbPort.getText().toString().equals("") && tbDatabase.getText().toString().equals("") && tbTable.getText().toString().equals("") && tbUser.getText().toString().equals("") && tbEditPassword.getText().toString().equals("")) {
                tbServer.setText(R.string.server_hint);
                tbPort.setText(R.string.porta_hint);
                tbDatabase.setText(R.string.db_hint);
                tbTable.setText(R.string.tabella_hint);
                tbUser.setText(R.string.utente_hint);
                tbEditPassword.setText(R.string.password_hint);
            } else if (tbServer.getText().toString().equals("") || tbPort.getText().toString().equals("") || tbDatabase.getText().toString().equals("") || tbTable.getText().toString().equals("") || tbUser.getText().toString().equals("") || tbEditPassword.getText().toString().equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle(R.string.errore);
                builder.setMessage(R.string.inserire_tutti_i_campi);
                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                builder.show();
            } else {
                NonClosableDialog ncd = new NonClosableDialog();
                ncd.show(getChildFragmentManager(), getString(R.string.tentativo_di_connessione));
                List<String> list = new LinkedList<>();
                list.add(tbServer.getText().toString());
                list.add(tbPort.getText().toString());
                list.add(tbDatabase.getText().toString());
                list.add(tbTable.getText().toString());
                list.add(tbUser.getText().toString());
                list.add(tbEditPassword.getText().toString());
                Call<List<String>> call = ApiClient.getApiService().sendInfoToServer(list);
                call.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        if (response.isSuccessful()) {
                            List<String> responseList = response.body();
                            if (responseList.get(0).equals("OK")) {
                                clusterLayout.setVisibility(View.VISIBLE);
                                ncd.setText(getResources().getString(R.string.connessione_riuscita));
                                ncd.dismiss();
                                int numCluster = Integer.parseInt(responseList.get(1));
                                List<Integer> listCluster = new LinkedList<>();
                                for (int i = 0; i < numCluster; i++) {
                                    listCluster.add(i + 1);
                                }
                                infoLayout.setVisibility(View.GONE);
                                spinnerCluster = view.findViewById(R.id.spinnerCluster);
                                spinnerCluster.setVisibility(View.VISIBLE);
                                final Dialog clusterDialog = new Dialog(requireContext());
                                spinnerCluster.setOnClickListener(v -> {
                                    clusterDialog.setContentView(R.layout.dialog_searchable_spinner);
                                    clusterDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                    ListView listView = clusterDialog.findViewById(R.id.list_view);
                                    EditText editText = clusterDialog.findViewById(R.id.edit_text);
                                    ArrayAdapter<Integer> adapter1 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, listCluster);
                                    listView.setAdapter(adapter1);
                                    clusterDialog.show();
                                    editText.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            adapter1.getFilter().filter(s);
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                        }
                                    });

                                    listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                                        spinnerCluster.setText(adapter1.getItem(i).toString());
                                        clusterDialog.dismiss();
                                    });

                                    new Handler().postDelayed(adapter1::notifyDataSetChanged, 200);
                                });
                                btCalc = view.findViewById(R.id.btCalc);
                                btCalc.setOnClickListener(v -> {
                                    NonClosableDialog ncd1 = new NonClosableDialog();
                                    ncd1.show(getChildFragmentManager(), getString(R.string.calcolo_in_corso));
                                    List<Integer> list = new LinkedList<>();
                                    list.add(Integer.parseInt(spinnerCluster.getText().toString()));
                                    Call<List<String>> newClusterCall = ApiClient.getApiService().requestNewClusterSet(list);
                                    newClusterCall.enqueue(new Callback<List<String>>() {
                                        @Override
                                        public void onResponse(Call<List<String>> newClusterCall, Response<List<String>> response) {
                                            if (response.isSuccessful()) {
                                                List<String> responseList = response.body();
                                                if (responseList.get(0).equals("ERRORE")) {
                                                    ncd1.setText(getResources().getString(R.string.errore)+": "+responseList.get(1));
                                                    ncd1.dismiss();
                                                    return;
                                                }
                                                ncd1.setText(getString(R.string.risultati_arrivati_correttamente));
                                                ncd1.dismiss();
                                                String nrIter = responseList.get(0);
                                                TextView tvResult = view.findViewById(R.id.tvResult);
                                                tvResult.setText(nrIter);
                                                responseList.remove(0);
                                                HashMap<String, List<String>> clusterMap = new HashMap<>();
                                                for (String str : responseList) {
                                                    String[] split = str.split("Examples:");
                                                    List<String> list = new LinkedList<>();
                                                    list.addAll(Arrays.asList(split).subList(1, split.length));
                                                    clusterMap.put(split[0], list);
                                                }
                                                List<String> keys = new ArrayList<>();
                                                keys.addAll(clusterMap.keySet());
                                                CustomizedExpandableListAdapter expandableListAdapter = new CustomizedExpandableListAdapter(requireContext(), keys, clusterMap);
                                                expandableListView.setAdapter(expandableListAdapter);
                                                clusterLayout.setVisibility(LinearLayout.GONE);
                                                resultLayout.setVisibility(LinearLayout.VISIBLE);
                                            } else {
                                                ncd.setText(getString(R.string.connessione_non_riuscita));
                                                ncd.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<List<String>> call, Throwable t) {
                                            ncd.setText(getResources().getString(R.string.tempo_scaduto));
                                            ncd.dismiss();
                                        }

                                    });
                                });
                            } else {
                                ncd.setText(getResources().getString(R.string.connessione_non_riuscita)+": " + responseList.get(0));
                                ncd.dismiss();
                            }
                        } else {
                            ncd.setText(getResources().getString(R.string.connessione_non_riuscita));
                            ncd.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        ncd.setText(getResources().getString(R.string.connessione_non_riuscita));
                        ncd.dismiss();
                    }
                });
            }
        });
        return view;
    }
}