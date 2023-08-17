package com.example.progettomap.fragment;

import android.app.Dialog;
import android.content.Context;
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
import com.example.progettomap.custom.CustomDialog;
import com.example.progettomap.custom.CustomizedExpandableListAdapter;
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
     * Dialog per la visualizzazione di messaggi
     */
    private AlertDialog alertDialog;


    private EditText tbServer;
    private EditText tbPort;
    private EditText tbDatabase;
    private EditText tbTable;
    private EditText tbUser;
    private EditText tbEditPassword;


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
        Button btConnect = view.findViewById(R.id.btConnect);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (tbServer.getText().toString().equals("") && tbPort.getText().toString().equals("") && tbDatabase.getText().toString().equals("") && tbTable.getText().toString().equals("") && tbUser.getText().toString().equals("") && tbEditPassword.getText().toString().equals("")) {
                    btConnect.setText("USA PREDEFINITI");
                } else {
                    btConnect.setText("CONNETTI AL DATABASE");
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
        ExpandableListView expandableListView = view.findViewById(R.id.expandableListView);
        ConstraintLayout infoLayout = view.findViewById(R.id.infoLayout),
                clusterLayout = view.findViewById(R.id.clusterLayout),
                resultLayout = view.findViewById(R.id.resultLayout);
        Button btDbInfoReset = view.findViewById(R.id.btDbInfoReset);

        tbDatabase.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tbDatabase.setHint("MapDB");
            } else {
                tbDatabase.setHint("Database");
            }
        });
        tbTable.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tbTable.setHint("playtennis");
            } else {
                tbTable.setHint("Tabella");
            }
        });
        tbUser.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tbUser.setHint("MapUser");
            } else {
                tbUser.setHint("Utente");
            }
        });
        tbPort.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tbPort.setHint("3306");
            } else {
                tbPort.setHint("Porta");
            }
        });
        tbServer.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tbServer.setHint("localhost");
            } else {
                tbServer.setHint("Server");
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
            btConnect.setText("USA PREDEFINITI");
        });


            btConnect.setOnClickListener(v -> {
            if (tbServer.getText().toString().equals("") && tbPort.getText().toString().equals("") && tbDatabase.getText().toString().equals("") && tbTable.getText().toString().equals("") && tbUser.getText().toString().equals("") && tbEditPassword.getText().toString().equals("")) {
                tbServer.setText("localhost");
                tbPort.setText("3306");
                tbDatabase.setText("MapDB");
                tbTable.setText("playtennis");
                tbUser.setText("MapUser");
                tbEditPassword.setText("map");
            } else if (tbServer.getText().toString().equals("") || tbPort.getText().toString().equals("") || tbDatabase.getText().toString().equals("") || tbTable.getText().toString().equals("") || tbUser.getText().toString().equals("") || tbEditPassword.getText().toString().equals("")) {
                CustomDialog.openDialog(requireContext(),"ERRORE", "Inserire tutti i campi!");
            } else {
                showNonClosableAlertDialog(requireContext());
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
                                View dialogView = alertDialog.getWindow().getDecorView();
                                TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
                                messageTextView.setText("Connessione riuscita!");
                                dialogView.postDelayed(() -> alertDialog.dismiss(), 1500);
                                int numCluster = Integer.parseInt(responseList.get(1));
                                List<Integer> listCluster = new LinkedList<>();
                                for (int i = 0; i < numCluster; i++) {
                                    listCluster.add(i + 1);
                                }
                                infoLayout.setVisibility(View.GONE);
                                TextView spinnerCluster;
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
                                Button btCalc = view.findViewById(R.id.btCalc);
                                btCalc.setOnClickListener(v -> {
                                    showNonClosableAlertDialog(requireContext());
                                    List<Integer> list = new LinkedList<>();
                                    list.add(Integer.parseInt(spinnerCluster.getText().toString()));
                                    Call<List<String>> newClusterCall = ApiClient.getApiService().requestNewClusterSet(list);
                                    newClusterCall.enqueue(new Callback<List<String>>() {
                                        @Override
                                        public void onResponse(Call<List<String>> newClusterCall, Response<List<String>> response) {
                                            if (response.isSuccessful()) {
                                                List<String> responseList = response.body();
                                                if (responseList.get(0).equals("ERRORE")) {
                                                    View dialogView = alertDialog.getWindow().getDecorView();
                                                    TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
                                                    messageTextView.setText("ERRORE");
                                                    dialogView.postDelayed(() -> alertDialog.dismiss(), 1500);
                                                    return;

                                                }
                                                View dialogView = alertDialog.getWindow().getDecorView();
                                                TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
                                                messageTextView.setText("Risultati arrivati");
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
                                                dialogView.postDelayed(() -> alertDialog.dismiss(), 100);
                                                btConnect.setEnabled(true);
                                                clusterLayout.setVisibility(LinearLayout.GONE);
                                                resultLayout.setVisibility(LinearLayout.VISIBLE);
                                            } else {
                                                View dialogView = alertDialog.getWindow().getDecorView();
                                                TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
                                                messageTextView.setText("Connessione non riuscita.");
                                                dialogView.postDelayed(() -> alertDialog.dismiss(), 1500);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<List<String>> call, Throwable t) {
                                            View dialogView = alertDialog.getWindow().getDecorView();
                                            TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
                                            messageTextView.setText("Tempo scaduto.");
                                            dialogView.postDelayed(() -> alertDialog.dismiss(), 1500);
                                        }

                                    });
                                });
                            } else {
                                View dialogView = alertDialog.getWindow().getDecorView();
                                TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
                                messageTextView.setText("Connessione non riuscita: " + responseList.get(0));
                                dialogView.postDelayed(() -> alertDialog.dismiss(), 1500);
                            }
                        } else {
                            View dialogView = alertDialog.getWindow().getDecorView();
                            TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
                            messageTextView.setText("Connessione non riuscita.");
                            dialogView.postDelayed(() -> alertDialog.dismiss(), 1500);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        View dialogView = alertDialog.getWindow().getDecorView();
                        TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
                        messageTextView.setText("Connessione non riuscita.");
                        dialogView.postDelayed(() -> alertDialog.dismiss(), 1500);
                    }
                });
            }
        });
        return view;
    }

    /**
     * <h4>Chiamato quando un fragment viene attaccato per la prima volta al suo contesto.</h4>
     * <p>onCreate (android.os.Bundle) verrà chiamato dopo questo</p>
     *
     * @param context il contesto in cui il fragment è stato attaccato
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    /**
     * <h4>Mostra un dialog non chiudibile.</h4>
     */
    private void showNonClosableAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.show();
    }

}