package com.example.progettomap.data;

import java.util.Iterator;
import java.util.Set;

public class DiscreteAttribute extends Attribute implements Iterable<String> {
    private final Set<String> values;// Array di oggetti String, uno per ciascun valore del dominio discreto. I valori del dominio sono memorizzati in values seguendo un ordine lessicografico.

    DiscreteAttribute(String name, int index, Set<String> values) {
        super(name, index);
        this.values = values;
    }

    public Iterator<String> iterator() {
        return this.values.iterator();
    }

    int getNumberOfDistinctValues() {
        return values.size();
    }

    int frequency(Data data, Set<Integer> idList, String v) {
        int count = 0;
        for (int i : idList)
            if (data.getAttributeValue(i, this.getIndex()).equals(v))
                count++;
        return count;
    }

}
