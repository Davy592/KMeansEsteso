package com.example.springserver;

import com.example.springserver.data.Data;
import com.example.springserver.data.OutOfRangeSampleSize;
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

@RestController
public class Controller {

    private Data data;
    private String table;
    private String database;

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
            this.data = new Data(server, Integer.parseInt(port), database,  user,  password,  table);
            list.add("OK");
            list.add(String.valueOf(data.getNumberOfExamples()));
        } catch (NoValueException | DatabaseConnectionException | EmptySetException | SQLException | NumberFormatException e) {
             list.add("SI E' VERIFICATO UN ERRORE DURANTE L'INTERROGAZIONE AL DATABASE -> " + e.getMessage());
        }
        return list;
    }

    @PostMapping("/newClusterSet")
    public List<String> receiveNumberOfClusters(@RequestBody List<String> numCluster) {
        String str_numCluster=numCluster.get(0).replace("\"","");
        List<String> list = new LinkedList<>();
        String result;
        try
        {
            KMeansMiner kMeansMiner = new KMeansMiner(Integer.parseInt(str_numCluster));
            int numIter=kMeansMiner.kmeans(this.data);
            kMeansMiner.salva(".//Salvataggi//"+database+table+str_numCluster+".dat");
            result="Numero di iterazioni:"+numIter+"\n"+kMeansMiner.getC().toString(data);
            data=null;
            table=null;
            database=null;
        } catch (OutOfRangeSampleSize e) {
            result = "IL INSERIRE UN VALORE VALIDO PER IL NUMERO DI CLUSTER";
        } catch (IOException e) {
            result = "SI E' VERIFICATO UN ERRORE ";
        }
        list.add(result);
        return list;
    }


    @PostMapping("/fileInfo")
    public List<String> receiveInfoFile(@RequestBody List<String> info) {
        List<String> list=new LinkedList<>();
        String result;
        try {
            String path = ".//Salvataggi/"+info.get(0);
            System.out.println(path);
            KMeansMiner kMeansMiner = new KMeansMiner(path);
            result = kMeansMiner.getC().toString();
        } catch (IOException e) {
            result = "FILE NON ESISTENTE";
        } catch (ClassNotFoundException e) {
            result = "SI E' VERIFICATO UN ERRORE ";
        }
        list.add(result);
        return list;
    }

    @PostMapping("/fileNames")
    public List<String> sendFilesName()
    {
        File folder = new File(".//Salvataggi//");
        File[] listOfFiles = folder.listFiles();
        List<String> list=new LinkedList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                list.add(file.getName());
            }
        }
        return list;
    }

}
