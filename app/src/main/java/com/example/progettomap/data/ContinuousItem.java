package com.example.progettomap.data;

import static java.lang.Math.abs;

public class ContinuousItem extends Item {

    ContinuousItem(ContinuousAttribute attribute, double value) {
        super(attribute, value);
    }

    double distance(Object a) {
        ContinuousAttribute attribute = (ContinuousAttribute) this.getAttribute();
        return abs(attribute.getScaledValue((double) this.getValue()) - attribute.getScaledValue((double) a));
    }

}
