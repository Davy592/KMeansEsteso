package com.example.progettomap;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.LinkedList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ApiService apiService;
    private LinearLayout llK, llFile, llOpt1, llOpt2, llMain,llConnect;
    private CheckBox chDef;
    private EditText tbServer, tbPort, tbDatabase, tbTable, tbUser, tbPassword,tbServerIP,tbServerPORT;
    private Spinner spinnerCluster;
    private Button btConnect, btCalc, btFile, btClusterSet, btFileConnect, btNewKMeans, btLoadKMeans,btConnectToServer,btChangeServer1,btChangeServer2,btChangeServer3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         final TextView[] tvFileNames = new TextView[1];
         final Dialog fileDialog = new Dialog(this);
        chDef = findViewById(R.id.chDef);
        tbServer = findViewById(R.id.tbServer);
        tbPort = findViewById(R.id.tbPort);
        tbDatabase = findViewById(R.id.tbDatabase);
        tbTable = findViewById(R.id.tbTable);
        tbUser = findViewById(R.id.tbUser);
        tbPassword = findViewById(R.id.tbPassword);
        spinnerCluster = findViewById(R.id.spinnerCluster);
        btConnect = findViewById(R.id.btConnect);
        btCalc = findViewById(R.id.btCalc);
        llK = findViewById(R.id.llK);
        llFile = findViewById(R.id.llFile);
        llOpt1 = findViewById(R.id.llOpt1);
        llOpt2 = findViewById(R.id.llOpt2);
        llMain = findViewById(R.id.llMain);
        btFile = findViewById(R.id.btFile);
        btClusterSet = findViewById(R.id.btClusterSet);
        btFileConnect = findViewById(R.id.btFileConnect);
        btNewKMeans = findViewById(R.id.btNewKMeans);
        btLoadKMeans = findViewById(R.id.btLoadKMeans);
        tvFileNames[0] = findViewById(R.id.tvFileNames);
        llConnect = findViewById(R.id.llConnect);
        tbServerIP = findViewById(R.id.tbServerIP);
        tbServerPORT = findViewById(R.id.tbServerPORT);
        btConnectToServer = findViewById(R.id.btConnectToServer);
        btChangeServer1 = findViewById(R.id.btChangeServer1);
        btChangeServer2 = findViewById(R.id.btChangeServer2);
        btChangeServer3 = findViewById(R.id.btChangeServer3);
        chDef.setOnClickListener(v -> {
            if (chDef.isChecked()) {
                tbServer.setEnabled(false);
                tbPort.setEnabled(false);
                tbDatabase.setEnabled(false);
                tbTable.setEnabled(false);
                tbUser.setEnabled(false);
                tbPassword.setEnabled(false);
                tbServer.setText("localhost");
                tbPort.setText("3306");
                tbDatabase.setText("MapDB");
                tbTable.setText("playtennis");
                tbUser.setText("MapUser");
                tbPassword.setText("map");
            } else {
                tbServer.setEnabled(true);
                tbPort.setEnabled(true);
                tbDatabase.setEnabled(true);
                tbTable.setEnabled(true);
                tbUser.setEnabled(true);
                tbPassword.setEnabled(true);
                tbServer.setText("");
                tbPort.setText("");
                tbDatabase.setText("");
                tbTable.setText("");
                tbUser.setText("");
                tbPassword.setText("");
            }
        });

        btChangeServer1.setOnClickListener(v->{
            changeServer();
                });
        btChangeServer2.setOnClickListener(v->{
            changeServer();
        });
        btChangeServer3.setOnClickListener(v->{
            changeServer();
        });

        btConnectToServer.setOnClickListener(v->{
            if (tbServerIP.getText().toString().equals("") || tbServerPORT.getText().toString().equals("")) {
                openDialog("ERRORE", "Inserire tutti i campi!");
            } else {
                ApiClient.setBaseUrl( tbServerIP.getText().toString(), Integer.parseInt(tbServerPORT.getText().toString()));
                try {
                    apiService = ApiClient.getClient().create(ApiService.class);
                    llConnect.setVisibility(LinearLayout.GONE);
                    llMain.setVisibility(LinearLayout.VISIBLE);
                } catch (Exception e) {
                    openDialog("ERRORE", "Errore di connessione al server!");
                }
            }
        });

        btConnect.setOnClickListener(v -> {
            if (tbServer.getText().toString().equals("") || tbPort.getText().toString().equals("") || tbDatabase.getText().toString().equals("") || tbTable.getText().toString().equals("") || tbUser.getText().toString().equals("") || tbPassword.getText().toString().equals("")) {
                openDialog("ERRORE", "Inserire tutti i campi!");
            } else {
                System.out.println("Ciao, server!");
                List<String> list = new LinkedList<>();
                list.add(tbServer.getText().toString());
                list.add(tbPort.getText().toString());
                list.add(tbDatabase.getText().toString());
                list.add(tbTable.getText().toString());
                list.add(tbUser.getText().toString());
                list.add(tbPassword.getText().toString());
                Call<List<String>> call = apiService.sendInfoToServer(list);
                call.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        if (response.isSuccessful()) {
                            List<String> responseList = response.body();
                            if (responseList.get(0).equals("OK")) {
                                openDialog("OPERAZIONE AVVENUTA CON SUCCESSO", "Connessione riuscita!\nInserire il numero dei cluster e premere il pulsante \"Calcola\".\n");

                                int numCluster = Integer.parseInt(responseList.get(1));
                                List<Integer> listCluster = new LinkedList<>();
                                for (int i = 0; i < numCluster; i++) {
                                    listCluster.add(i + 1);
                                }
                                ArrayAdapter<Integer> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, listCluster);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerCluster.setAdapter(adapter);
                                tbServer.setEnabled(false);
                                tbPort.setEnabled(false);
                                tbDatabase.setEnabled(false);
                                tbTable.setEnabled(false);
                                tbUser.setEnabled(false);
                                tbPassword.setEnabled(false);
                                btConnect.setEnabled(false);
                                chDef.setEnabled(false);
                                llK.setVisibility(LinearLayout.VISIBLE);
                                llFile.setVisibility(LinearLayout.GONE);
                                btCalc.setVisibility(Button.VISIBLE);


                            } else {
                                openDialog("ERRORE", responseList.get(0));
                            }
                        } else {
                            openDialog("ERRORE", "Connessione non riuscita!");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        openDialog("ERRORE", t.getMessage());
                    }
                });
            }
        });

        btCalc.setOnClickListener(v -> {
            String k = spinnerCluster.getSelectedItem().toString();
            List<String> list = new LinkedList<>();
            list.add(k);
            Call<List<String>> call = apiService.requestNewClusterSet(list);
            call.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    if (response.isSuccessful()) {
                        List<String> responseList = response.body();
                        openDialog("RISULTATO", responseList.get(0) + "\nPREMERE OK PER CONTINUARE");
                        btConnect.setEnabled(true);
                        chDef.setEnabled(true);
                        llK.setVisibility(LinearLayout.GONE);
                        llFile.setVisibility(LinearLayout.VISIBLE);
                        btCalc.setVisibility(Button.GONE);
                    } else {
                        openDialog("ERRORE", "Connessione non riuscita!");
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    openDialog("ERRORE", t.getMessage());
                }


            });
        });

        btFile.setOnClickListener(v -> {
            llOpt1.setVisibility(LinearLayout.GONE);
            llOpt2.setVisibility(LinearLayout.VISIBLE);
        });

        btClusterSet.setOnClickListener(v -> {
            llOpt1.setVisibility(LinearLayout.VISIBLE);
            llOpt2.setVisibility(LinearLayout.GONE);
        });

        btLoadKMeans.setOnClickListener(v -> {
            llOpt2.setVisibility(LinearLayout.VISIBLE);
            llMain.setVisibility(LinearLayout.GONE);
        });

        btNewKMeans.setOnClickListener(v -> {
            llOpt1.setVisibility(LinearLayout.VISIBLE);
            llMain.setVisibility(LinearLayout.GONE);
        });

        btFileConnect.setOnClickListener(v -> {
            if (tvFileNames[0].getText().equals("")) {
                openDialog("ERRORE", "SELEZIONA UN FILE!");
            } else {
                String fileName = tvFileNames[0].getText().toString();
                List<String> list = new LinkedList<>();
                list.add(fileName);
                Call<List<String>> call = apiService.sendFileToServer(list);
                call.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        if (response.isSuccessful()) {
                            String responseString = response.body().get(0);
                            openDialog("RISULTATO", responseString + "\nPREMERE OK PER CONTINUARE");

                        } else {
                            openDialog("ERRORE", "Connessione non riuscita!");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        openDialog("ERRORE", t.getMessage());
                    }
                });
            }
        });


        tvFileNames[0].setOnClickListener(v -> {
            fileDialog.setContentView(R.layout.dialog_searchable_spinner);
            fileDialog.getWindow().setLayout(650, 800);
            fileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ListView listView = fileDialog.findViewById(R.id.list_view);
            EditText editText = fileDialog.findViewById(R.id.edit_text);
            List<String> FileArray = requestFilesName();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    MainActivity.this, android.R.layout.simple_list_item_1, FileArray);
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
                tvFileNames[0].setText(adapter.getItem(i));
                fileDialog.dismiss();
            });

            // Aggiungi un ritardo di 200 millisecondi (o più) prima di popolare la ListView
            new Handler().postDelayed(() -> {
                adapter.notifyDataSetChanged();
            }, 200);
        });
    }

            private void openDialog(String titolo, String messaggio) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle(titolo);
                alertDialog.setMessage(messaggio);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }

            private List<String> requestFilesName() {
                Call<List<String>> call = apiService.requestFilesName();
                List<String> list = new LinkedList<>();
                call.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        list.addAll(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        openDialog("ERRORE", t.getMessage());
                    }
                });
                return list;
            }
            private void changeServer()
            {
                llConnect.setVisibility(LinearLayout.VISIBLE);
                llMain.setVisibility(LinearLayout.GONE);
                llOpt1.setVisibility(LinearLayout.GONE);
                llOpt2.setVisibility(LinearLayout.GONE);
                llK.setVisibility(LinearLayout.GONE);
                llFile.setVisibility(LinearLayout.VISIBLE);
                ApiClient.closeClient();
                apiService=null;
                btConnect.setEnabled(true);
                chDef.setEnabled(true);
                btCalc.setVisibility(Button.GONE);
            }
    }