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
     * @param savedInstanceState stato dell'istanza
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText tbServerIP = findViewById(R.id.tbServerIP);
        EditText tbServerPORT = findViewById(R.id.tbServerPORT);
        tbServerPORT.setFilters(new InputFilter[] { new RangeInputFilter(0,65535)});
        Button btConnectToServer = findViewById(R.id.btConnectToServer);
        btConnectToServer.setOnClickListener(v -> {
            if (tbServerIP.getText().toString().equals("") || tbServerPORT.getText().toString().equals("")) {
                openDialog("ERRORE", "Inserire tutti i campi!");
            } else {
                ApiClient.setBaseUrl(tbServerIP.getText().toString(), Integer.parseInt(tbServerPORT.getText().toString()));
                try {
                    ApiClient.updateServer();
                    Bundle b = new Bundle();
                    b.putString("serverIP", tbServerIP.getText().toString());
                    b.putString("serverPORT", tbServerPORT.getText().toString());
                    Dashboard.openDashboardWithBundle(this, b);
                } catch (Exception e) {
                    openDialog("ERRORE", "Errore di connessione al server!" + e.getMessage());
                }
            }
        });

    }

    /**
     * <h4>Metodo che apre un dialog</h4>
     * <p>Questo metodo apre un dialog con un messaggio</p>
     * @param titolo titolo del dialog
     * @param messaggio messaggio del dialog
     */
    private void openDialog(String titolo, String messaggio) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(titolo);
        alertDialog.setMessage(messaggio);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
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

}