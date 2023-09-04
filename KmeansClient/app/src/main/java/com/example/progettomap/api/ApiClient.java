package com.example.progettomap.api;


import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.internal.JavaNetCookieJar;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.net.CookieManager;


/**
 * <h2>ApiClient e' la classe che gestisce la connessione con il server</h2>
 */
public class ApiClient {
    /**
     * Istanza della classe singleton
     */
    private static final ApiClient instance = new ApiClient();

    /**
     * URL del server
     */
    private String BASE_URL = null;

    /**
     * Oggetto che gestisce la connessione con il server
     */
    private Retrofit retrofit = null;

    /**
     * Oggetto che gestisce le chiamate al server
     */
    private ApiService apiService = null;

    /**
     * <h4>Costruttore privato</h4>
     * <p>Il costruttore e' privato per evitare che vengano create piu' istanze della classe</p>
     */
    private ApiClient() {
    }

    /**
     * <h4>Restituisce l'url del server</h4>
     *
     * @return URL del server
     */
    public static String getBaseUrl() {
        return instance.BASE_URL;
    }

    /**
     * <h4>Modifica l'URL del server</h4>
     *
     * @param server Indirizzo del server
     * @param port   Porta del server
     */
    public static void setBaseUrl(String server, int port) {
        instance.BASE_URL = "http://" + server + ":" + port + "/";
    }


    /**
     * <h4>Crea un oggetto che gestisce la connessione con il server</h4>
     * <p>Se l'oggetto e' <code>null</code>, non viene creato un nuovo oggetto</p>
     */
    public static void updateServer() {
        if (instance.retrofit == null) {
            //create session with cookie
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .cookieJar(new JavaNetCookieJar(new CookieManager()))
                    .build();
            //set timeout
            httpClient.newBuilder()
                    .readTimeout(90, TimeUnit.SECONDS);
            instance.retrofit = new Retrofit.Builder()
                    .baseUrl(instance.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        instance.apiService = instance.retrofit.create(ApiService.class);
    }


    /**
     * <h4>Chiude la connessione con il server</h4>
     */
    public static void closeClient() {
        instance.retrofit = null;
    }

    /**
     * <h4>Restituisce l'oggetto che gestisce le chiamate al server</h4>
     *
     * @return Oggetto che gestisce le chiamate al server
     */
    public static ApiService getApiService() {
        return instance.apiService;
    }


}
