package com.spreadsheet;

public class NumericalContent extends Content {
    private Number value;

    public NumericalContent(Number value) {
        this.value = value;
    }

    public Number getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}