package com.example.progettomap;

import static java.lang.Thread.sleep;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

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
        chDef = (CheckBox) findViewById(R.id.chDef);
        tbServer = (EditText) findViewById(R.id.tbServer);
        tbPort = (EditText) findViewById(R.id.tbPort);
        tbDatabase = (EditText) findViewById(R.id.tbDatabase);
        tbTable = (EditText) findViewById(R.id.tbTable);
        tbUser = (EditText) findViewById(R.id.tbUser);
        tbPassword = (EditText) findViewById(R.id.tbPassword);
        tbK = (EditText) findViewById(R.id.tbK);
        btConnect = (Button) findViewById(R.id.btConnect);
        btCalc = (Button) findViewById(R.id.btCalc);
        llK = (LinearLayout) findViewById(R.id.llK);

        chDef.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
            }
        });


        btConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*System.out.println("tbServer: " + tbServer.getText());
                System.out.println("tbPort: " + tbPort.getText());
                System.out.println("tbDatabase: " + tbDatabase.getText());
                System.out.println("tbTable: " + tbTable.getText());
                System.out.println("tbUser: " + tbUser.getText());
                System.out.println("tbPassword: " + tbPassword.getText());*/
                try {
                    if (tbServer.getText().toString().equals("") || tbPort.getText().toString().equals("") || tbDatabase.getText().toString().equals("") || tbTable.getText().toString().equals("") || tbUser.getText().toString().equals("") || tbPassword.getText().toString().equals("")) {
                        //TODO: stampare errore in alertDialog
                    } else {
                        llK.setVisibility(View.INVISIBLE);
                        btCalc.setVisibility(View.INVISIBLE);
                        data = new Data(tbServer.getText().toString(), Integer.parseInt(tbPort.getText().toString()), tbDatabase.getText().toString(), tbTable.getText().toString(), tbUser.getText().toString(), tbPassword.getText().toString());
                        System.out.println("Provo");
                        System.out.println("Data: " + data);
                        llK.setVisibility(View.VISIBLE);
                        btCalc.setVisibility(View.VISIBLE);
                        System.out.println("fatto");
                        //TODO: stampare data in alertDialog
                    }
                } catch (DatabaseConnectionException | SQLException | NoValueException |
                         EmptySetException e) {
                    System.out.println("Ho preso " + e.getMessage());
                    //TODO: stampare e.getMessage() in alertDialog
                }
            }
        });

        btCalc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int numIter;
                try {
                    if (tbK.toString().equals("")) {
                        //TODO: stampare errore in alertDialog
                    } else {
                        kmeans = new KMeansMiner(Integer.parseInt(tbK.toString()));
                        numIter = kmeans.kmeans(data);
                        //TODO: stampare risultato in alertDialog
                    }
                } catch (OutOfRangeSampleSize e) {
                    //TODO: stampare e.getMessage() in alertDialog
                }
            }
        });
    }

}