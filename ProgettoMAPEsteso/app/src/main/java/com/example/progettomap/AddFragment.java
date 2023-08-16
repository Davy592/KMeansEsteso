package com.example.progettomap;

import android.app.Dialog;
import android.app.MediaRouteButton;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddFragment extends Fragment {
    private AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        Bundle b = this.getArguments();

        Button btConnect = view.findViewById(R.id.btConnect);
        EditText tbServer, tbPort, tbDatabase, tbTable, tbUser;
        TextInputEditText tbEditPassword;
        TextInputLayout tbPassword;
        tbServer = view.findViewById(R.id.tbServer);
        tbPort = view.findViewById(R.id.tbPort);
        tbDatabase = view.findViewById(R.id.tbDatabase);
        tbDatabase.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tbDatabase.setHint("MapDB");
            } else {
                tbDatabase.setHint("Database");
            }
        });
        tbTable = view.findViewById(R.id.tbTable);
        tbUser = view.findViewById(R.id.tbUser);
        tbEditPassword = view.findViewById(R.id.tbEditPassword);
        tbPassword = view.findViewById(R.id.tbPassword);
        String port = "", server = "";
        try {
            port = b.getString("serverPORT");
            server = b.getString("serverIP");
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Errore: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        ApiClient.setBaseUrl(server, Integer.parseInt(port));
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Button btDbInfoReset = view.findViewById(R.id.btDbInfoReset);
        btDbInfoReset.setOnClickListener(v -> {
            tbServer.setText("");
            tbPort.setText("");
            tbDatabase.setText("");
            tbTable.setText("");
            tbUser.setText("");
            tbEditPassword.setText("");
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
                openDialog("ERRORE", "Inserire tutti i campi!");
            } else {
                showNonClosableAlertDialog(requireContext());
                List<String> list = new LinkedList<>();
                list.add(tbServer.getText().toString());
                list.add(tbPort.getText().toString());
                list.add(tbDatabase.getText().toString());
                list.add(tbTable.getText().toString());
                list.add(tbUser.getText().toString());
                list.add(tbEditPassword.getText().toString());
                Call<List<String>> call = apiService.sendInfoToServer(list);
                call.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        if (response.isSuccessful()) {
                            List<String> responseList = response.body();
                            if (responseList.get(0).equals("OK")) {
                                View dialogView = alertDialog.getWindow().getDecorView();
                                TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
                                messageTextView.setText("Connessione riuscita!");

                                // Dismiss the AlertDialog after a delay
                                dialogView.postDelayed(() -> alertDialog.dismiss(), 1500);
                                int numCluster = Integer.parseInt(responseList.get(1));
                                List<Integer> listCluster = new LinkedList<>();
                                for (int i = 0; i < numCluster; i++) {
                                    listCluster.add(i + 1);
                                }
                                tbServer.setVisibility(View.GONE);
                                tbPort.setVisibility(View.GONE);
                                tbDatabase.setVisibility(View.GONE);
                                tbTable.setVisibility(View.GONE);
                                tbUser.setVisibility(View.GONE);
                                tbEditPassword.setVisibility(View.GONE);
                                btConnect.setVisibility(View.GONE);
                                btDbInfoReset.setVisibility(View.GONE);
                                tbPassword.setVisibility(View.GONE);
                                TextView spinnerCluster;
                                spinnerCluster = view.findViewById(R.id.spinnerCluster);
                                spinnerCluster.setVisibility(View.VISIBLE);
                                final Dialog clusterDialog = new Dialog(requireContext());
                                spinnerCluster.setOnClickListener(v->{
                                    clusterDialog.setContentView(R.layout.cluster_searchable_spinner);
                                    clusterDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                    clusterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

                                    // Aggiungi un ritardo di 200 millisecondi (o più) prima di popolare la ListView
                                    new Handler().postDelayed(adapter1::notifyDataSetChanged, 200);
                                });
                                Button btCalc = view.findViewById(R.id.btCalc);
                                btCalc.setVisibility(View.VISIBLE);
                                btCalc.setOnClickListener(v -> {
                                    showNonClosableAlertDialog(requireContext());
                                    int k;
                                    try {
                                        k = Integer.parseInt(spinnerCluster.getText().toString());
                                    }
                                    catch(NumberFormatException e)
                                    {
                                        openDialog("ERRORE", "Inserire un numero di cluster valido!");
                                        return;
                                    }
                                    List<Integer> list = new LinkedList<>();
                                    list.add(k);
                                    Call<List<String>> newClusterCall = apiService.requestNewClusterSet(list);
                                    newClusterCall.enqueue(new Callback<List<String>>() {
                                        @Override
                                        public void onResponse(Call<List<String>> newClusterCall, Response<List<String>> response) {
                                            if (response.isSuccessful()) {
                                                View dialogView = alertDialog.getWindow().getDecorView();
                                                TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
                                                messageTextView.setText("Risultati arrivati");

                                                // Dismiss the AlertDialog after a delay
                                                dialogView.postDelayed(() -> alertDialog.dismiss(), 100);
                                                List<String> responseList = response.body();
                                                openDialog("RISULTATO", responseList.get(0) + "\nPREMERE OK PER CONTINUARE");
                                                btConnect.setEnabled(true);
                                                spinnerCluster.setVisibility(Spinner.GONE);
                                                btCalc.setVisibility(Button.GONE);
                                                tbServer.setVisibility(View.VISIBLE);
                                                tbPort.setVisibility(View.VISIBLE);
                                                tbDatabase.setVisibility(View.VISIBLE);
                                                tbTable.setVisibility(View.VISIBLE);
                                                tbUser.setVisibility(View.VISIBLE);
                                                tbEditPassword.setVisibility(View.VISIBLE);
                                                btConnect.setVisibility(View.VISIBLE);
                                                btDbInfoReset.setVisibility(View.VISIBLE);
                                                tbPassword.setVisibility(View.VISIBLE);
                                            } else {
                                                View dialogView = alertDialog.getWindow().getDecorView();
                                                TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
                                                messageTextView.setText("Connessione non riuscita.");

                                                // Dismiss the AlertDialog after a delay
                                                dialogView.postDelayed(() -> alertDialog.dismiss(), 1500);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<List<String>> call, Throwable t) {
                                            View dialogView = alertDialog.getWindow().getDecorView();
                                            TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
                                            messageTextView.setText("Tempo scaduto.");

                                            // Dismiss the AlertDialog after a delay
                                            dialogView.postDelayed(() -> alertDialog.dismiss(), 1500);
                                        }

                                    });
                                });
                            } else {
                                View dialogView = alertDialog.getWindow().getDecorView();
                                TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
                                messageTextView.setText("Connessione non riuscita: " + responseList.get(0));

                                // Dismiss the AlertDialog after a delay
                                dialogView.postDelayed(() -> alertDialog.dismiss(), 1500);
                            }
                        } else {
                            openDialog("ERRORE", "Connessione non riuscita(D).");
                            View dialogView = alertDialog.getWindow().getDecorView();
                            TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
                            messageTextView.setText("Connessione non riuscita.");

                            // Dismiss the AlertDialog after a delay
                            dialogView.postDelayed(() -> alertDialog.dismiss(), 1500);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        View dialogView = alertDialog.getWindow().getDecorView();
                        TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
                        messageTextView.setText("Connessione non riuscita.");

                        // Dismiss the AlertDialog after a delay
                        dialogView.postDelayed(() -> alertDialog.dismiss(), 1500);
                    }
                });
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // callback = (FragmentToActivity) context;
    }

    private void openDialog(String titolo, String messaggio) {
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
        alertDialog.setTitle(titolo);
        alertDialog.setMessage(messaggio);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

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