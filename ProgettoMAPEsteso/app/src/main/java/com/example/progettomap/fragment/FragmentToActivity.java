package com.example.progettomap.fragment;

import com.example.progettomap.api.ApiService;

/**
 * <h2>Interfaccia che permette la comunicazione tra fragment e activity</h2>
 */

public interface FragmentToActivity {

    /**
     * <h2>Metodo che permette di aggiornare i dati del server</h2>
     *
     * @param data dati del server
     */
    void updateServer(ApiService data);
}
