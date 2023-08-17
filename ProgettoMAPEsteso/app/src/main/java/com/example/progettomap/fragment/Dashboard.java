package com.example.progettomap.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.progettomap.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


/**
 * <h2>La classe Dashboard gestisce la navigazione tra i fragment</h2>
 * <p> La navigazione tra i fragment avviene tramite un BottomNavigationView</p>
 */
public class Dashboard extends AppCompatActivity {

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
        setContentView(R.layout.dashboard);
        BottomNavigationView bnv = findViewById(R.id.navbarview);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new AddFragment()).commit();
        bnv.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_add) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new AddFragment()).addToBackStack("add").commit();
                return true;
            } else if (itemId == R.id.nav_load) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new LoadFragment()).addToBackStack("load").commit();
                return true;
            } else if (itemId == R.id.nav_change_server) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ServerFragment()).addToBackStack("change").commit();
                return true;
            }

            return false;
        });
    }


    /**
     * <h4>Il metodo onResume gestisce il comportamento della applicazione quando viene ripresa in esecuzione</h4>
     */
    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bnv = findViewById(R.id.navbarview);
        if (bnv.getSelectedItemId() == R.id.nav_add) {
            bnv.setSelectedItemId(R.id.nav_add);
        } else if (bnv.getSelectedItemId() == R.id.nav_load) {
            bnv.setSelectedItemId(R.id.nav_load);
        } else if (bnv.getSelectedItemId() == R.id.nav_change_server) {
            bnv.setSelectedItemId(R.id.nav_change_server);
        }
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