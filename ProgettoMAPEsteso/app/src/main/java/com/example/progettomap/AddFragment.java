package com.example.progettomap;

import android.app.MediaRouteButton;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
        EditText tbServer, tbPort, tbDatabase, tbTable, tbUser, tbPassword;
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
            tbPassword.setText("");
        });
        btConnect.setOnClickListener(v -> {
            if (tbServer.getText().toString().equals("") && tbPort.getText().toString().equals("") && tbDatabase.getText().toString().equals("") && tbTable.getText().toString().equals("") && tbUser.getText().toString().equals("") && tbPassword.getText().toString().equals("")) {
                tbServer.setText("localhost");
                tbPort.setText("3306");
                tbDatabase.setText("MapDB");
                tbTable.setText("playtennis");
                tbUser.setText("MapUser");
                tbPassword.setText("map");
            } else if (tbServer.getText().toString().equals("") || tbPort.getText().toString().equals("") || tbDatabase.getText().toString().equals("") || tbTable.getText().toString().equals("") || tbUser.getText().toString().equals("") || tbPassword.getText().toString().equals("")) {
                openDialog("ERRORE", "Inserire tutti i campi!");
            } else {
                showNonClosableAlertDialog(requireContext());
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
                                ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, listCluster);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                tbServer.setVisibility(View.GONE);
                                tbPort.setVisibility(View.GONE);
                                tbDatabase.setVisibility(View.GONE);
                                tbTable.setVisibility(View.GONE);
                                tbUser.setVisibility(View.GONE);
                                tbPassword.setVisibility(View.GONE);
                                btConnect.setVisibility(View.GONE);
                                btDbInfoReset.setVisibility(View.GONE);
                                Spinner spinnerCluster;
                                spinnerCluster = view.findViewById(R.id.spinnerCluster);
                                spinnerCluster.setAdapter(adapter);
                                spinnerCluster.setVisibility(View.VISIBLE);
                                Button btCalc = view.findViewById(R.id.btCalc);
                                btCalc.setVisibility(View.VISIBLE);
                                btCalc.setOnClickListener(v -> {
                                    showNonClosableAlertDialog(requireContext());
                                    String k = spinnerCluster.getSelectedItem().toString();
                                    List<String> list = new LinkedList<>();
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
                                                tbPassword.setVisibility(View.VISIBLE);
                                                btConnect.setVisibility(View.VISIBLE);
                                                btDbInfoReset.setVisibility(View.VISIBLE);
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