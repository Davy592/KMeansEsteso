package com.example.springserver;

import com.example.springserver.data.Data;
import com.example.springserver.database.DatabaseConnectionException;
import com.example.springserver.database.EmptySetException;
import com.example.springserver.database.NoValueException;
import com.example.springserver.mining.KMeansMiner;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


/**
 * <h2>La classe Controller gestisce le richieste del client.</h2>
 * <p>Il controller riceve le richieste del client e restituisce le risposte.</p>
 */
@RestController
public class Controller {

    /**
     * <h4>Il dataset.</h4>
     */
    private Data data;
    /**
     * <h4>Il nome della tabella.</h4>
     */
    private String table;
    /**
     * <h4>Il nome del database.</h4>
     */
    private String database;

    /**
     * <h4>Riceve dal client le informazioni per la creazione di un nuovo dataset.</h4>
     * <p>Le informazioni sono: server, porta, nome del database, nome della tabella, nome utente e password.</p>
     * <p>Restituisce una lista di stringhe che contiene "OK" se non ci sono stati errori, altrimenti contiene un messaggio di errore.</p>
     *
     * @param info la lista delle informazioni
     * @return la lista dei database
     */
    @PostMapping("/connectionInfo")
    public List<String> receiveInfoFromClient(@RequestBody List<String> info) {
        List<String> list = new LinkedList<>();
        try {
            String server = info.get(0);
            String port = info.get(1);
            database = info.get(2);
            table = info.get(3);
            String user = info.get(4);
            String password = info.get(5);
            this.data = new Data(server, Integer.parseInt(port), database, user, password, table);
            list.add("OK");
            list.add(String.valueOf(data.getNumberOfExamples()));
        } catch (NoValueException | DatabaseConnectionException | EmptySetException | SQLException |
                 NumberFormatException e) {
            list.add("SI E' VERIFICATO UN ERRORE DURANTE L'INTERROGAZIONE AL DATABASE -> " + e.getMessage());
        }
        return list;
    }

    /**
     * <h4>Riceve dal client il numero di cluster da creare.</h4>
     * <p>Restituisce una lista di stringhe che contiene i cluster.</p>
     *
     * @param numCluster il numero di cluster
     * @return la lista dei cluster
     */
    @PostMapping("/newClusterSet")
    public List<String> receiveNumberOfClusters(@RequestBody List<Integer> numCluster) {
        int numC = numCluster.get(0);
        List<String> list = new LinkedList<>();
        try {
            KMeansMiner kMeansMiner = new KMeansMiner(numC);
            int numIter = kMeansMiner.kmeans(this.data);
            kMeansMiner.salva("Salvataggi//" + database + table + numC + ".dat");
            list = kMeansMiner.getC().toString(data);
            ((LinkedList<String>) list).addFirst("Numero di iterazioni:" + numIter);
            data = null;
            table = null;
            database = null;
        } catch (IOException e) {
            list.clear();
            list.add("ERRORE");
            list.add("ERRORE NEL SALVATAGGIO DEL FILE");
        }
        return list;
    }


    /**
     * <h4>Riceve dal client il nome del file da caricare.</h4>
     * <p>Restituisce una lista di stringhe che contiene i centroidi dei cluster.</p>
     *
     * @param info il nome del file
     * @return la lista dei centroidi
     */
    @PostMapping("/fileInfo")
    public List<String> receiveInfoFile(@RequestBody List<String> info) {
        List<String> list = new LinkedList<>();
        String result;
        try {
            String path = "Salvataggi//" + info.get(0);
            KMeansMiner kMeansMiner = new KMeansMiner(path);
            result = kMeansMiner.getC().toString();
        } catch (IOException e) {
            list.add("ERRORE");
            result = "FILE NON ESISTENTE";
        } catch (ClassNotFoundException e) {
            list.add("ERRORE");
            result = "SI E' VERIFICATO UN ERRORE ";
        }
        list.add(result);
        return list;
    }

    /**
     * <h4>Restituisce la lista dei file salvati.</h4>
     * <p>Restituisce una lista di stringhe che contiene i nomi dei file salvati.</p>
     *
     * @return la lista dei file
     */
    @PostMapping("/fileNames")
    public List<String> sendFilesName() {
        List<String> list = new LinkedList<>();
        try {
            File folder = new File("Salvataggi//");
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles.length == 0) list.add("ERRORE");
            else
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        list.add(file.getName());
                    }
                }
        } catch (NullPointerException e) {
            list.add("ERRORE");
        }
        return list;
    }

}
