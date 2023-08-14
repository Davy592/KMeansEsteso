package com.example.progettomap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ApiService apiService;
    private EditText tbServerIP, tbServerPORT;
    private Button btConnectToServer;

    public static void openMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getSplashScreen().setOnExitAnimationListener(splashScreenView -> {
                final ObjectAnimator slideUp = ObjectAnimator.ofFloat(
                        splashScreenView,
                        View.TRANSLATION_Y,
                        0f,
                        -splashScreenView.getHeight()
                );
                slideUp.setInterpolator(new AnticipateInterpolator());
                slideUp.setDuration(200L);

                // Call SplashScreenView.remove at the end of your custom animation.
                slideUp.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        splashScreenView.remove();
                    }
                });

                // Run your animation.
                slideUp.start();
            });
        }*/

        tbServerIP = findViewById(R.id.tbServerIP);
        tbServerPORT = findViewById(R.id.tbServerPORT);
        btConnectToServer = findViewById(R.id.btConnectToServer);

        btConnectToServer.setOnClickListener(v -> {
            if (tbServerIP.getText().toString().equals("") || tbServerPORT.getText().toString().equals("")) {
                openDialog("ERRORE", "Inserire tutti i campi!");
            } else {
                ApiClient.setBaseUrl(tbServerIP.getText().toString(), Integer.parseInt(tbServerPORT.getText().toString()));
                try {
                    apiService = ApiClient.getClient().create(ApiService.class);
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

    private void openDialog(String titolo, String messaggio) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(titolo);
        alertDialog.setMessage(messaggio);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}