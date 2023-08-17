package com.example.progettomap.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <h2>Classe che gestisce la connessione con il server</h2>
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
     * <p>Il costruttore è privato per evitare che vengano create più istanze della classe</p>
     */
    private ApiClient() {
    }

    /**
     * <h4>Metodo che setta l'URL del server</h4>
     *
     * @param server Indirizzo del server
     * @param port   Porta del server
     */
    public static void setBaseUrl(String server, int port) {
        instance.BASE_URL = "http://" + server + ":" + port + "/";
    }


    /**
     * <h4>Metodo che crea un oggetto che gestisce la connessione con il server</h4>
     * <p>Se l'oggetto è già stato creato, non viene creato un nuovo oggetto</p>
     */
    public static void updateServer() {
        if (instance.retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.readTimeout(60, TimeUnit.SECONDS);
            instance.retrofit = new Retrofit.Builder()
                    .baseUrl(instance.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        instance.apiService = instance.retrofit.create(ApiService.class);
    }

    /**
     * <h4>Metodo che crea un oggetto che gestisce la connessione con il server</h4>
     * <p>L'oggetto viene passato come parametro</p>
     *
     * @param apiService Oggetto che gestisce le chiamate al server
     */
    public static void updateServer(ApiService apiService) {
        instance.apiService = apiService;
    }

    /**
     * <h4>Metodo che chiude la connessione con il server</h4>
     */
    public static void closeClient() {
        instance.retrofit = null;
    }

    /**
     * <h4>Metodo che restituisce l'oggetto che gestisce le chiamate al server</h4>
     *
     * @return Oggetto che gestisce le chiamate al server
     */
    public static ApiService getApiService() {
        return instance.apiService;
    }


}