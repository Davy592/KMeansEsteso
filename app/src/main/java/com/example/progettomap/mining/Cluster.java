package com.example.progettomap.mining;

import com.example.progettomap.data.Data;
import com.example.progettomap.data.Tuple;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Cluster implements Serializable {
    private final Tuple centroid;

    private final Set<Integer> clusteredData;

    /*mining.Cluster(){

    }*/

    Cluster(Tuple centroid) {
        this.centroid = centroid;
        clusteredData = new HashSet<>();

    }

    Tuple getCentroid() {
        return centroid;
    }

    void computeCentroid(Data data) {
        for (int i = 0; i < centroid.getLength(); i++) {
            centroid.get(i).update(data, clusteredData);

        }

    }

    //return true if the tuple is changing cluster
    boolean addData(int id) {
        return clusteredData.add(id);

    }

    //verifica se una transazione � clusterizzata nell' array corrente
    boolean contain(int id) {
        return clusteredData.contains(id);
    }


    //remove the tuple that has changed the cluster
    void removeTuple(int id) {
        clusteredData.remove(id);

    }

    public String toString() {
        String str = "Centroid=(";
        for (int i = 0; i < centroid.getLength(); i++)
            str += centroid.get(i) + " ";
        str += ")";
        return str;

    }


    public String toString(Data data) {
        String str = "Centroid=(";
        for (int i = 0; i < centroid.getLength(); i++) {
            str += centroid.get(i) + (i == centroid.getLength() - 1 ? "" : " ");
        }
        str += ")\nExamples:\n";
        for (int i : clusteredData) {
            str += "[";
            for (int j = 0; j < data.getNumberOfAttributes(); j++)
                str += data.getAttributeValue(i, j) + (j == data.getNumberOfAttributes() - 1 ? "" : " ");
            str += "] dist=" + getCentroid().getDistance(data.getItemSet(i)) + "\n";

        }
        str += "AvgDistance=" + getCentroid().avgDistance(data, clusteredData) + "\n";
        return str;

    }

}
