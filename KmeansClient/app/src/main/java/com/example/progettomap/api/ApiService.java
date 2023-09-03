package com.example.progettomap.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * <h2>
 * Interfaccia che definisce i metodi per le chiamate alle API.
 * </h2>
 */
public interface ApiService {
    /**
     * <h4>Metodo che invia al server le informazioni di connessione</h4>.
     *
     * @param list lista di stringhe contenente le informazioni di connessione
     * @return l'oggetto che rappresenta la chiamata
     */
    @POST("connectionInfo")
    Call<List<String>> sendInfoToServer(@Body List<String> list);

    /**
     * <h4>Metodo che invia al server il numero di cluster</h4>.
     *
     * @param numCluster numero di cluster
     * @return l'oggetto che rappresenta la chiamata
     */
    @POST("newClusterSet")
    Call<List<String>> requestNewClusterSet(@Body List<Integer> numCluster);

    /**
     * <h4>Metodo che invia al server il nome del file</h4>.
     *
     * @param list lista di stringhe contenente il nome del file
     * @return l'oggetto che rappresenta la chiamata
     */
    @POST("fileInfo")
    Call<List<String>> sendFileToServer(@Body List<String> list);

    /**
     * <h4>Metodo che richiede al server i nomi dei file</h4>.
     *
     * @return l'oggetto che rappresenta la chiamata
     */
    @POST("fileNames")
    Call<List<String>> requestFilesName();
}