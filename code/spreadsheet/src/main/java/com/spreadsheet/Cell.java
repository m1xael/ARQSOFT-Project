package com.spreadsheet;

public class Cell {
    private String address;
    private Content content;

    public Cell(String address, Content content) {
        this.address = address;
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}