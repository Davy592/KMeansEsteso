package com.example.progettomap.data;

import java.io.Serializable;

public abstract class Attribute implements Serializable {
    private final String name; // nome simbolico dell'attributo
    private final int index; // identificativo numerico dell'attributo

    Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public String toString() {
        return name;
    }

}
