package com.example.progettomap.data;

public class ContinuousAttribute extends Attribute {
    private final double max;
    private final double min;// rappresentano gli estremi dell'intervallo di valori (dominio) che l'attributo può realmente assumere.

    ContinuousAttribute(String name, int index, double min, double max) {
        super(name, index);
        this.max = max;
        this.min = min;
    }

    double getScaledValue(double v) {
        return (v - min) / (max - min);
    }
}
