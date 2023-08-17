package com.example.progettomap.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.progettomap.R;
import com.example.progettomap.api.ApiClient;
import com.example.progettomap.api.ApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;


/**
 * <h2>La classe Dashboard gestisce la navigazione tra i fragment</h2>
 * <p> La navigazione tra i fragment avviene tramite un BottomNavigationView</p>
 */
public class Dashboard extends AppCompatActivity implements FragmentToActivity {

    /**
     * <h4> Metodo statico che apre la Dashboard</h4>
     *
     * @param context contesto
     * @param params  parametri
     */
    public static void openDashboardWithBundle(Context context, Bundle params) {
        Intent i = new Intent(context, Dashboard.class);
        i.putExtras(params);
        context.startActivity(i);
    }

    /**
     * <h4> Metodo che crea la Dashboard</h4>
     *
     * @param savedInstanceState stato dell'istanza
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        setContentView(R.layout.dashboard);
        BottomNavigationView bnv = findViewById(R.id.navbarview);
        Fragment initialF = new AddFragment();
        initialF.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, initialF).commit();
        bnv.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_add) {
                Fragment f = new AddFragment();
                f.setArguments(b);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, f).commit();
                return true;
            } else if (itemId == R.id.nav_load) {
                Fragment f = new LoadFragment();
                f.setArguments(b);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, f).commit();
                return true;
            } else if (itemId == R.id.nav_change_server) {
                Fragment f = new ServerFragment();
                f.setArguments(b);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, f).commit();
                return true;
            }

            return false;
        });
    }

    /**
     * <h4> Metodo che aggiorna la connessione con il server</h4>
     *
     * @param data dati
     */
    @Override
    public void updateServer(ApiService data) {
        ApiClient.updateServer(data);
    }

    /**
     * <h4> Metodo che gestisce il tasto back</h4>
     */
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}