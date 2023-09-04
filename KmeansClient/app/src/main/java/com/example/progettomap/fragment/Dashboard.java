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
     * Rappresenta il BottomNavigationView
     */
    private BottomNavigationView bnv;

    /**
     * <h4>Apre la Dashboard</h4>
     *
     * @param context contesto
     */
    public static void openDashboardWithBundle(Context context) {
        Intent i = new Intent(context, Dashboard.class);
        context.startActivity(i);
    }

    /**
     * <h4>Crea la Dashboard</h4>
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
     * <h4>Gestisce il comportamento della applicazione quando viene ripresa in esecuzione</h4>
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
     * <h4>Gestisce la pressione del tasto back</h4>
     */
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        bnv = findViewById(R.id.navbarview);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog;
        String fName = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
        if (fName.equals("add")) {
            AddFragment af = (AddFragment) fm.getFragments().get(0);
            if (af.getVisibilityInfo()) {
                builder
                        .setMessage(getResources().getString(R.string.uscire))
                        .setCancelable(true)
                        .setPositiveButton(getResources().getString(R.string.si), (dialogInterface, i) -> finish())
                        .setNegativeButton(getResources().getString(R.string.no), (dialogInterface, i) -> dialogInterface.cancel());
                alertDialog = builder.create();
                alertDialog.show();
            } else {
                if (af.getVisibilityCluster()) {
                    af.setVisibilityCluster(false);
                    af.setVisibilityInfo(true);
                } else {
                    af.setVisibilityResult(false);
                    af.setVisibilityInfo(true);
                }
            }
            bnv.setSelectedItemId(R.id.nav_add);
        } else {
            builder
                    .setMessage(getResources().getString(R.string.uscire))
                    .setCancelable(true)
                    .setPositiveButton(getResources().getString(R.string.si), (dialogInterface, i) -> finish())
                    .setNegativeButton(getResources().getString(R.string.no), (dialogInterface, i) -> dialogInterface.cancel());
            alertDialog = builder.create();
            alertDialog.show();

            alertDialog = builder.create();
            alertDialog.show();
        }
    }
}