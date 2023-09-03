package com.example.springserver.mining;

import com.example.springserver.data.Data;
import com.example.springserver.data.OutOfRangeSampleSize;


import java.io.*;

/**
 * <h2>La classe KMeansMiner modella l'algoritmo K-means.</h2>
 */
public class KMeansMiner {

    /**
     * <h4>Insieme di cluster.</h4>
     * @see ClusterSet
     */
    private ClusterSet C;

    /**
     * <h4>Inizializza il clusterSet su cui eseguire il K-means.</h4>
     * @param k il numero di cluster da creare nel clusterSet
     * @throws OutOfRangeSampleSize se il numero di cluster specificato è minore di 1
     */
    public KMeansMiner(int k) throws OutOfRangeSampleSize {
        C = new ClusterSet(k);
    }

    /**
     * <h4>Carica il clusterSet su cui è stato eseguito il K-means da un file.</h4>
     * @param fileName il nome del file da cui caricare il clusterSet
     * @throws IOException se si verifica un errore di I/O
     * @throws ClassNotFoundException se si verifica un errore di caricamento della classe
     */
    public KMeansMiner(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)));
        C = (ClusterSet) in.readObject();
        in.close();
    }

    /**
     * <h4>Restituisce il clusterSet su cui è stato eseguito il K-means.</h4>
     * @return Il clusterSet su cui è stato eseguito il K-means
     */
    public ClusterSet getC() {
        return C;
    }

    /**
     * <h4>Esegue l'algoritmo di clustering K-means.</h4>
     * @param data il dataset
     * @return Il numero di iterazioni eseguite
     * @throws OutOfRangeSampleSize se il numero di tuple del dataset è minore del numero di cluster
     */
    public int kmeans(Data data) throws OutOfRangeSampleSize {
        int numberOfIterations = 0;
        //STEP 1. Scelta casuale di centroidi per k clusters
        C.initializeCentroids(data);
        boolean changedCluster;
        do {
            numberOfIterations++;
            //STEP 2. Assegnazione di ciascuna riga della matrice in data al cluster avente centroide più vicino all'esempio
            changedCluster = false;
            for (int i = 0; i < data.getNumberOfExamples(); i++) {
                Cluster nearestCluster = C.nearestCluster(data.getItemSet(i));
                Cluster oldCluster = C.currentCluster(i);
                boolean currentChange = nearestCluster.addData(i);
                if (currentChange)
                    changedCluster = true;
                //rimuovo la tupla dal vecchio cluster
                if (currentChange && oldCluster != null)
                    //il nodo va rimosso dal suo vecchio cluster
                    oldCluster.removeTuple(i);
            }
            //STEP 3. Calcolo dei nuovi centroidi per ciascun cluster
            C.updateCentroids(data);
        }
        while (changedCluster);
        return numberOfIterations;
    }

    /**
     * <h4>Salva il clusterSet su cui è stato eseguito il K-means su un file.</h4>
     * @param fileName il nome del file su cui salvare il clusterSet
     * @throws IOException se si verifica un errore di I/O
     */
    public void salva(String fileName) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
        out.writeObject(C);
        out.close();
    }

}

