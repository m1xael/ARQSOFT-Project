package com.spreadsheet;

public class Main {
    public static void main(String[] args) {
        Spreadsheet spreadsheet = new Spreadsheet();

        try {
            spreadsheet.setCellContent("A1", new NumericalContent(10));
            spreadsheet.setCellContent("A2", new NumericalContent(5));
            spreadsheet.setCellContent("A3", new TextContent("Test"));

            spreadsheet.setCellContent("B1", new FormulaContent(new Formula("=10 + 5")));
            spreadsheet.setCellContent("B2", new FormulaContent(new Formula("=10 * 2")));
            spreadsheet.setCellContent("B3", new FormulaContent(new Formula("=(10 - 5) / 3")));

            spreadsheet.saveSpreadsheet("spreadsheet.txt");

            Spreadsheet loadedSpreadsheet = new Spreadsheet();
            loadedSpreadsheet.loadSpreadsheet("spreadsheet.txt");

            System.out.println("A1: " + loadedSpreadsheet.getCellValue("A1")); 
            System.out.println("A2: " + loadedSpreadsheet.getCellValue("A2")); 
            System.out.println("A3: " + loadedSpreadsheet.getCellValue("A3")); 
            System.out.println("B1 (=10 + 5): " + loadedSpreadsheet.getCellValue("B1")); 
            System.out.println("B2 (=10 * 2): " + loadedSpreadsheet.getCellValue("B2")); 
            System.out.println("B3 (=(10 - 5) / 3): " + loadedSpreadsheet.getCellValue("B3")); 
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}