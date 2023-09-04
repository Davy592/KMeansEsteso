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
 * <h2> La classe MainActivity rappresenta il main dell'applicazione</h2>
 */

public class MainActivity extends AppCompatActivity {


    /**
     * <h4>Crea l'activity</h4>
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
                builder.setTitle(R.string.errore);
                builder.setMessage(R.string.inserire_tutti_i_campi);
                builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
                builder.show();
            } else {
                ApiClient.setBaseUrl(tbServerIP.getText().toString(), Integer.parseInt(tbServerPORT.getText().toString()));
                try {
                    ApiClient.updateServer();
                    Dashboard.openDashboardWithBundle(this);
                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.errore);
                    builder.setMessage(R.string.connessione_non_riuscita);
                    builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
                    builder.show();
                }
            }
        });

    }



    /**
     * <h4>Gestisce il tasto back</h4>
     * <p>Questo metodo gestisce il tasto back</p>
     */
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


}