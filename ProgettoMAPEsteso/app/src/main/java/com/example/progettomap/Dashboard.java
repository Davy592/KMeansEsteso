package com.example.progettomap;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity implements FragmentToActivity {

    private BottomNavigationView bnv;
    private ApiService apiService;
    private EditText tbServer, tbPort, tbDatabase, tbTable, tbUser, tbPassword,tbServerIP,tbServerPORT;
    private Spinner spinnerCluster;
    private Button btConnect, btCalc, btFile, btClusterSet, btFileConnect;
    public static void openDashboard(Context context) {
        Intent intent = new Intent(context, Dashboard.class);
        context.startActivity(intent);
    }

    public static void openDashboardWithBundle(Context context, Bundle params) {
        Intent i = new Intent(context, Dashboard.class);
        i.putExtras(params);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        setContentView(R.layout.dashboard);
        bnv = findViewById(R.id.navbarview);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
        bnv.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                return true;
            } else if (itemId == R.id.nav_add) {
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

    @Override
    public void disableNavbar(boolean disable) {
        //bnv.getMenu().setGroupEnabled(bnv.getMenu().size(), disable);
        bnv.setVisibility(disable ? View.GONE : View.VISIBLE);
    }
    @Override
    public void updateServer(ApiService data) {
        apiService = data;
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}