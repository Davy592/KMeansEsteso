package com.example.progettomap.data;

import java.io.Serializable;
import java.util.Set;

public abstract class Item implements Serializable {
    private final Attribute attribute; // attributo coinvolto nell'item
    private Object value; // valore assegnato all'attributo

    Item(Attribute attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public Object getValue() {
        return value;
    }

    public String toString() {
        return value.toString();
    }

    abstract double distance(Object a); // L’implementazione sarà diversa per item discreto e item continuo

    public void update(Data data, Set<Integer> clusteredData) {
        value = data.computePrototype(clusteredData, attribute);
    }
}
