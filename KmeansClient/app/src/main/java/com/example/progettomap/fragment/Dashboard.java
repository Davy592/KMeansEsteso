package com.example.progettomap.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.progettomap.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * <h2>La classe Dashboard gestisce la navigazione tra i fragment</h2>
 * <p> La navigazione tra i fragment avviene tramite un BottomNavigationView</p>
 */
public class Dashboard extends AppCompatActivity {


    /**
     * Variabile che rappresenta il BottomNavigationView
     */
    private BottomNavigationView bnv;

    /**
     * <h4> Metodo statico che apre la Dashboard</h4>
     *
     * @param context contesto
     */
    public static void openDashboardWithBundle(Context context) {
        Intent i = new Intent(context, Dashboard.class);
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
        bnv = findViewById(R.id.navbarview);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new AddFragment()).addToBackStack("add").commit();
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

        bnv.setOnItemReselectedListener(item -> {});
    }


    /**
     * <h4>Il metodo onResume gestisce il comportamento della applicazione quando viene ripresa in esecuzione</h4>
     */
    @Override
    public void onResume() {
        super.onResume();
        bnv = findViewById(R.id.navbarview);
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
        FragmentManager fm = getSupportFragmentManager();
        bnv = findViewById(R.id.navbarview);
        if (fm.getBackStackEntryCount() <= 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage("USCIRE?")
                    .setCancelable(true)
                    .setPositiveButton("SI", (dialogInterface, i) -> finish())
                    .setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.cancel());

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            fm.popBackStack();
            String fName = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 2).getName();
            switch (fName) {
                case "add":
                    bnv.setSelectedItemId(R.id.nav_add);
                    break;
                case "load":
                    bnv.setSelectedItemId(R.id.nav_load);
                    break;
                case "change":
                    bnv.setSelectedItemId(R.id.nav_change_server);
                    break;
                default:
                    break;
            }
            super.onBackPressed();
        }
    }

}
