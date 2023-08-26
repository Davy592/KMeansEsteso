package com.example.progettomap;

import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.progettomap.api.ApiClient;
import com.example.progettomap.custom.RangeInputFilter;
import com.example.progettomap.fragment.Dashboard;

/**
 * <h2> MainActivity rappresenta il main dell'applicazione</h2>
 */

public class MainActivity extends AppCompatActivity {


    /**
     * <h4>Metodo che crea l'activity</h4>
     * <p>Questo metodo crea l'activity e gestisce la connessione iniziale al server</p>
     *
     * @param savedInstanceState stato dell'istanza
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText tbServerIP = findViewById(R.id.tbServerIP);
        EditText tbServerPORT = findViewById(R.id.tbServerPORT);
        tbServerPORT.setFilters(new InputFilter[]{new RangeInputFilter(0, 65535)});
        Button btConnectToServer = findViewById(R.id.btConnectToServer);
        btConnectToServer.setOnClickListener(v -> {
            if (tbServerIP.getText().toString().equals("") || tbServerPORT.getText().toString().equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("ERRORE");
                builder.setMessage("Inserire IP e PORTA del server!");
                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                builder.show();
            } else {
                ApiClient.setBaseUrl(tbServerIP.getText().toString(), Integer.parseInt(tbServerPORT.getText().toString()));
                try {
                    ApiClient.updateServer();
                    Bundle b = new Bundle();
                    b.putString("serverIP", tbServerIP.getText().toString());
                    b.putString("serverPORT", tbServerPORT.getText().toString());
                    Dashboard.openDashboardWithBundle(this, b);
                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("ERRORE");
                    builder.setMessage("Errore di connessione al server!" + e.getMessage());
                    builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                    builder.show();
                }
            }
        });

    }



    /**
     * <h4>Metodo che gestisce il tasto back</h4>
     * <p>Questo metodo gestisce il tasto back</p>
     */
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    /**
     * <h4>Metodo che gestisce la ripresa dell'activity</h4>
     * <p>Questo metodo gestisce la ripresa dell'activity</p>
     */
    @Override
    public void onResume() {
        super.onResume();
    }
}