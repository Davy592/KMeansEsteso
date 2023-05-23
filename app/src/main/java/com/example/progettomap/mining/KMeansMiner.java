package com.example.progettomap.mining;

import com.example.progettomap.data.Data;
import com.example.progettomap.data.OutOfRangeSampleSize;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class KMeansMiner {
    private final ClusterSet C;

    public KMeansMiner(int k) throws OutOfRangeSampleSize {
        C = new ClusterSet(k);
    }

    public KMeansMiner(String fileName) throws IOException, ClassNotFoundException {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            C = (ClusterSet) in.readObject();
            in.close();
        } catch (IOException e) {
            throw new IOException("Errore di I/O");
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Classe non trovata");
        }
    }

    public void salva(String fileName) throws IOException {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(this.C);
            out.close();
        } catch (IOException e) {
            throw new IOException("Errore di I/O");
        }
    }

    public ClusterSet getC() {
        return C;
    }

    public int kmeans(Data data) throws OutOfRangeSampleSize {
        int numberOfIterations = 0;
        //STEP 1
        C.initializeCentroids(data);
        boolean changedCluster;
        do {
            numberOfIterations++;
            //STEP 2
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
            //STEP 3
            C.updateCentroids(data);
        } while (changedCluster);
        return numberOfIterations;
    }
}
