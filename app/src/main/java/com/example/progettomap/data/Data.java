package com.example.progettomap.data;

import static com.example.progettomap.database.QUERY_TYPE.MAX;
import static com.example.progettomap.database.QUERY_TYPE.MIN;

import com.example.progettomap.database.DatabaseConnectionException;
import com.example.progettomap.database.DbAccess;
import com.example.progettomap.database.EmptySetException;
import com.example.progettomap.database.Example;
import com.example.progettomap.database.NoValueException;
import com.example.progettomap.database.TableData;
import com.example.progettomap.database.TableSchema;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Data {
    // Le visibilità di classi, attributi e metodi devono essere decise dagli studenti
    private List<Example> data; // una matrice nXm di tipo Object dove ogni riga modella una transazioni
    private final int numberOfExamples; // cardinalità dell’insieme di transazioni (numero di righe in data)
    private final List<Attribute> attributeSet; // un vettore degli attributi in ciascuna tupla (schema della tabella di dati)

    public Data(String server, String database, String table, String userId, String password) throws DatabaseConnectionException, SQLException, NoValueException, EmptySetException {
        DbAccess db = new DbAccess(server, database, userId, password);
        db.initConnection();
        TableData td = new TableData(db);
        TableSchema ts = new TableSchema(db, table);
        data = td.getDistinctTransazioni(table);
        numberOfExamples = data.size();
        attributeSet = new ArrayList<>();
        for (int i = 0; i < ts.getNumberOfAttributes(); i++) {
            if (ts.getColumn(i).isNumber()) {
                attributeSet.add(new ContinuousAttribute(ts.getColumn(i).getColumnName(), i, (double) td.getAggregateColumnValue(table, ts.getColumn(i), MIN), (double) td.getAggregateColumnValue(table, ts.getColumn(i), MAX)));
            } else {
                HashSet<Object> distValues = (HashSet<Object>) td.getDistinctColumnValues(table, ts.getColumn(i));
                HashSet<String> values = new HashSet<>();
                for (Object o : distValues) {
                    values.add((String) o);
                }
                attributeSet.add(new DiscreteAttribute(ts.getColumn(i).getColumnName(), i, values));
            }
        }
    }

    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    public int getNumberOfAttributes() {
        return attributeSet.size();
    }

    public Object getAttributeValue(int exampleIndex, int attributeIndex) {
        return data.get(exampleIndex).get(attributeIndex);
    }

    Attribute getAttribute(int index) {
        return attributeSet.get(index);
    }

    public Tuple getItemSet(int index) {
        Tuple tuple = new Tuple(attributeSet.size());
        for (int i = 0; i < attributeSet.size(); i++)
            if (attributeSet.get(i) instanceof ContinuousAttribute)
                tuple.add(new ContinuousItem((ContinuousAttribute) attributeSet.get(i), (Double) data.get(index).get(i)), i);
            else
                tuple.add(new DiscreteItem((DiscreteAttribute) attributeSet.get(i), (String) data.get(index).get(i)), i);
        return tuple;
    }

    public int[] sampling(int k) throws OutOfRangeSampleSize {
        if (k <= 0 || k > data.size()) {
            throw new OutOfRangeSampleSize("Inserire k compreso tra 1 e " + data.size()
            );
        }
        int[] centroidIndexes = new int[k]; //choose k random different centroids in data.
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        for (int i = 0; i < k; i++) {
            boolean found;
            int c;
            do {
                found = false;
                c = rand.nextInt(getNumberOfExamples()); // verify that centroid[c] is not equal to a centroid already stored in CentroidIndexes
                for (int j = 0; j < i; j++)
                    if (compare(centroidIndexes[j], c)) {
                        found = true;
                        break;
                    }
            }
            while (found);
            centroidIndexes[i] = c;
        }
        return centroidIndexes;
    }

    private boolean compare(int i, int j) {
        for (int k = 0; k < attributeSet.size(); k++)
            if (!data.get(i).get(k).equals(data.get(j).get(k)))
                return false;
        return true;
    }

    Object computePrototype(Set<Integer> idList, Attribute attribute) {
        if (attribute instanceof ContinuousAttribute)
            return computePrototype(idList, (ContinuousAttribute) attribute);
        else
            return computePrototype(idList, (DiscreteAttribute) attribute);
    }

    String computePrototype(Set<Integer> idList, DiscreteAttribute attribute) {
        int max = 0, tmp;
        String prototype = "";
        for (String a : attribute) {
            tmp = attribute.frequency(this, idList, a);
            if (tmp > max) {
                max = tmp;
                prototype = a;
            }
        }
        return prototype;
    }

    Double computePrototype(Set<Integer> idList, ContinuousAttribute attribute) {
        double sum = 0;
        for (int i : idList) {
            sum += (double) data.get(i).get(attribute.getIndex());
        }
        return sum / (double) idList.size();
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < attributeSet.size(); i++)
            s += attributeSet.get(i).getName() + (i == attributeSet.size() - 1 ? "\n" : ", ");
        for (int i = 0; i < numberOfExamples; i++) {
            s += (i + 1) + ": ";
            for (int j = 0; j < attributeSet.size(); j++)
                s += data.get(i).get(j) + (j == attributeSet.size() - 1 ? "\n" : ", ");
        }
        return s;
    }

}
