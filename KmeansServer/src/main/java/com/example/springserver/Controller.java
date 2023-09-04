package com.example.springserver;

import com.example.springserver.data.Data;
import com.example.springserver.data.OutOfRangeSampleSize;
import com.example.springserver.database.DatabaseConnectionException;
import com.example.springserver.database.EmptySetException;
import com.example.springserver.database.NoValueException;
import com.example.springserver.mining.KMeansMiner;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;


/**
 * <h2>La classe Controller gestisce le richieste del client.</h2>
 * <p>Il controller riceve le richieste del client e restituisce le risposte.</p>
 */

@RestController
public class Controller {

    /**
     * <h4>Mappa dei dataset richiesti dai client.</h4>
     */
    //private Map<String, Data> clientDataMap = new ConcurrentHashMap<>();
    /**
     * <h4>Mappa dei nomi della tabella richiesti dai client.</h4>
     */
    //private Map<String, String> clientTable = new ConcurrentHashMap<>();
    /**
     * <h4>Mappa dei nomi del database richiesti dai client.</h4>
     */
    //private Map<String, String> clientDatabase = new ConcurrentHashMap<>();

    /**
     * <h4>Riceve dal client le informazioni per la creazione di un nuovo dataset.</h4>
     * <p>Le informazioni sono: server, porta, nome del database, nome della tabella, nome utente e password.</p>
     * <p>Restituisce una lista di stringhe che contiene "OK" se non ci sono stati errori, altrimenti contiene un messaggio di errore.</p>
     *
     * @param request contiene le informazioni della richiesta http
     * @param info la lista delle informazioni
     * @return la lista delle transazioni, o un messaggio di errore
     */
    @PostMapping("/connectionInfo")
    public List<String> receiveInfoFromClient(HttpServletRequest request, HttpSession session, @RequestBody List<String> info) {
        List<String> list = new LinkedList<>();
        try {
            String client=request.getRemoteAddr();
            String server = info.get(0);
            String port = info.get(1);
            String database = info.get(2);
            String table = info.get(3);
            String user = info.get(4);
            String password = info.get(5);
            Data data = new Data(server, Integer.parseInt(port), database, user, password, table);
            //clientDataMap.put(client,data);
            //clientDatabase.put(client,database);
            //clientTable.put(client,table);
            session.setAttribute("data",data);
            session.setAttribute("database",database);
            session.setAttribute("table",table);
            System.out.println(session);
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
     * <p>Restituisce una lista di stringhe che contiene i cluster, altrimenti contiene un messaggio di errore.</p>
     *
     * @param request contiene le informazioni della richiesta http
     * @param numCluster il numero di cluster
     * @return la lista dei cluster o un messaggio di errore
     */
    @PostMapping("/newClusterSet")
    public synchronized List<String> receiveNumberOfClusters(HttpServletRequest request, HttpSession session, @RequestBody List<Integer> numCluster) {
        System.out.println(session);
        Data localData = (Data) session.getAttribute("data");
        String localDatabase=(String) session.getAttribute("database");
        String localTable=(String) session.getAttribute("table");
        List<String> result;
            int numC = numCluster.get(0);
            try {
                    KMeansMiner kMeansMiner = new KMeansMiner(numC);
                    int numIter = kMeansMiner.kmeans(localData);
                    kMeansMiner.salva("Salvataggi//" + localDatabase + localTable + numC + ".dat");
                    result = kMeansMiner.getC().toString(localData);
                    ((LinkedList<String>) result).addFirst("Numero di iterazioni:" + numIter);
                    return result;
            } catch (IOException e) {
                List<String> errorResult = new LinkedList<>();
                errorResult.add("ERRORE");
                errorResult.add("ERRORE NEL SALVATAGGIO DEL FILE");
                return errorResult;
            } catch (OutOfRangeSampleSize e) {
                List<String> errorResult = new LinkedList<>();
                errorResult.add("ERRORE");
                errorResult.add("NUMERO DI CLUSTER NON VALIDO");
                return errorResult;
            }
        }


    /**
     * <h4>Riceve dal client il nome del file da caricare.</h4>
     * <p>Restituisce una lista di stringhe che contiene i centroidi dei cluster, a meno di errori.</p>
     *
     * @param info il nome del file
     * @return la lista dei centroidi o un messaggio di errore
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
     * <p>Restituisce una lista di stringhe che contiene i nomi dei file salvati, a meno di errori.</p>
     *
     * @return la lista dei file o un messaggio di errore
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
