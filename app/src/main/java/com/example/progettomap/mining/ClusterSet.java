package com.example.progettomap.mining;

import com.example.progettomap.data.Data;
import com.example.progettomap.data.OutOfRangeSampleSize;
import com.example.progettomap.data.Tuple;

import java.io.Serializable;

public class ClusterSet implements Serializable {
    private final Cluster[] C;
    private int i = 0; //posizione valida per la memorizzazione di un nuovo cluster in C

    ClusterSet(int k) throws OutOfRangeSampleSize {
        try {
            C = new Cluster[k];
        } catch (NegativeArraySizeException e) {
            throw new OutOfRangeSampleSize("k deve essere maggiore di 0");
        }
    }

    void add(Cluster c) {
        C[i] = c;
        i++;
    }

    Cluster get(int i) {
        return C[i];
    }

    void initializeCentroids(Data data) throws OutOfRangeSampleSize {
        int[] centroidIndexes = data.sampling(C.length);
        for (int centroidIndex : centroidIndexes) {
            Tuple centroidI = data.getItemSet(centroidIndex);
            add(new Cluster(centroidI));
        }
    }

    Cluster nearestCluster(Tuple tuple) {
        double min = tuple.getDistance(C[0].getCentroid()), tmp;
        Cluster nearest = C[0];
        for (int i = 1; i < C.length; i++) {
            tmp = tuple.getDistance(C[i].getCentroid());
            if (tmp < min) {
                min = tmp;
                nearest = C[i];
            }
        }
        return nearest;
    }

    Cluster currentCluster(int id) {
        for (Cluster cluster : C) {
            if (cluster.contain(id))
                return cluster;
        }
        return null;
    }

    void updateCentroids(Data data) {
        for (Cluster cluster : C) {
            cluster.computeCentroid(data);
        }
    }

    public String toString() {
        String str = "";
        for (int i = 0; i < C.length; i++) {
            str += i + ": " + C[i] + "\n";
        }
        return str;
    }

    public String toString(Data data) {
        String str = "";
        for (int i = 0; i < C.length; i++) {
            if (C[i] != null) {
                str += i + ": " + C[i].toString(data) + "\n";
            }
        }
        return str;
    }

}
