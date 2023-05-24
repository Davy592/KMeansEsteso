package com.example.progettomap;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.progettomap.data.Data;
import com.example.progettomap.data.OutOfRangeSampleSize;
import com.example.progettomap.database.DatabaseConnectionException;
import com.example.progettomap.database.EmptySetException;
import com.example.progettomap.database.NoValueException;
import com.example.progettomap.mining.KMeansMiner;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    Data data;
    KMeansMiner kmeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckBox chDef;
        EditText tbServer, tbPort, tbDatabase, tbTable, tbUser, tbPassword, tbK;
        Button btConnect, btCalc;
        LinearLayout llK;
        chDef = findViewById(R.id.chDef);
        tbServer = findViewById(R.id.tbServer);
        tbPort = findViewById(R.id.tbPort);
        tbDatabase = findViewById(R.id.tbDatabase);
        tbTable = findViewById(R.id.tbTable);
        tbUser = findViewById(R.id.tbUser);
        tbPassword = findViewById(R.id.tbPassword);
        tbK = findViewById(R.id.tbK);
        btConnect = findViewById(R.id.btConnect);
        btCalc = findViewById(R.id.btCalc);
        llK = findViewById(R.id.llK);

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


        btConnect.setOnClickListener(v -> {
            try {
                if (tbServer.getText().toString().equals("") || tbPort.getText().toString().equals("") || tbDatabase.getText().toString().equals("") || tbTable.getText().toString().equals("") || tbUser.getText().toString().equals("") || tbPassword.getText().toString().equals("")) {
                    openDialog("ERRORE", "Inserire tutti i campi!");
                } else {
                    llK.setVisibility(View.INVISIBLE);
                    btCalc.setVisibility(View.INVISIBLE);
                    data = new Data(tbServer.getText().toString(), Integer.parseInt(tbPort.getText().toString()), tbDatabase.getText().toString(), tbTable.getText().toString(), tbUser.getText().toString(), tbPassword.getText().toString());
                    System.out.println("Provo");
                    System.out.println("Data: " + data);
                    llK.setVisibility(View.VISIBLE);
                    btCalc.setVisibility(View.VISIBLE);
                    System.out.println("fatto");
                    openDialog("DATI CARICATI", data.toString());
                }
            } catch (DatabaseConnectionException | SQLException | NoValueException | EmptySetException e) {
                System.out.println("Ho preso " + e.getMessage());
                openDialog("ERRORE", e.getMessage());
            }
        });

        btCalc.setOnClickListener(v -> {
            int numIter;
            try {
                if (tbK.toString().equals("")) {
                    openDialog("ERRORE", "Inserire il numero di cluster!");
                } else {
                    kmeans = new KMeansMiner(Integer.parseInt(tbK.toString()));
                    numIter = kmeans.kmeans(data);
                    openDialog("K-MEANS", "Numero di iterazioni: " + numIter + "\n" + kmeans.getC().toString(data));
                }
            } catch (OutOfRangeSampleSize e) {
                openDialog("ERRORE", e.getMessage());
            }
        });
    }

    void openDialog(String titolo, String messaggio) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(titolo);
        alertDialog.setMessage(messaggio);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

}