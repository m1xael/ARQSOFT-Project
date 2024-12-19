package com.spreadsheet;

public class TextContent extends Content {
    private String value;

    public TextContent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}