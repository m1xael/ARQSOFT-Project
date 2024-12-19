package com.spreadsheet;

public class Main {
    public static void main(String[] args) {

        UserInterface ui = new UserInterface();
        ui.start();

        /*Spreadsheet spreadsheet = new Spreadsheet();
        try {
            spreadsheet.setCellContent("A1", new NumericalContent(1));
            spreadsheet.setCellContent("A2", new NumericalContent(1));
            String formulaA3 = "=A1 + A2";
            spreadsheet.setCellContent("A3", new FormulaContent(new Formula(formulaA3)));
            spreadsheet.setCellContent("A4", new TextContent("This is a Test!"));
            
            spreadsheet.setCellContent("B1", new NumericalContent(5));
            spreadsheet.setCellContent("B2", new NumericalContent(10));
            String formulaB3 = "=SUM(A1; A2; A3; B1; B2)";
            spreadsheet.setCellContent("B3", new FormulaContent(new Formula(formulaB3)));
            
            spreadsheet.setCellContent("C1", new NumericalContent(20));
            spreadsheet.setCellContent("C2", new NumericalContent(30));
            spreadsheet.setCellContent("C3", new NumericalContent(40));
            String formulaC4 = "=AVG(A1:A3; B1:B3; C1:C3)";
            spreadsheet.setCellContent("C4", new FormulaContent(new Formula(formulaC4)));
            
            String formulaD1 = "=MIN(A1:A3; B1:B3; C1:C3)";
            spreadsheet.setCellContent("D1", new FormulaContent(new Formula(formulaD1)));
            
            String formulaE1 = "=MAX(A1:A3; B1:B3; C1:C3)";
            spreadsheet.setCellContent("E1", new FormulaContent(new Formula(formulaE1)));
            
            String formulaF1 = "=SUM(A1:A3; B1:B3)";
            spreadsheet.setCellContent("F1", new FormulaContent(new Formula(formulaF1)));

            String formulaG1 = "=AVG(5; 15; A1:A2)";
            spreadsheet.setCellContent("G1", new FormulaContent(new Formula(formulaG1)));
            
            String formulaH1 = "=H2 + 1";
            spreadsheet.setCellContent("H1", new FormulaContent(new Formula(formulaH1)));
            String formulaH2 = "=H1 + 1";
            spreadsheet.setCellContent("H2", new FormulaContent(new Formula(formulaH2)));

            String fromulaI1 = "=5+2";
            spreadsheet.setCellContent("I1", new FormulaContent(new Formula(fromulaI1)));

            spreadsheet.setCellContent("J1", new NumericalContent(4));
            spreadsheet.setCellContent("J2", new NumericalContent(1));
            String formulaJ3 = "=SUM(A1; A2)";
            spreadsheet.setCellContent("J3", new FormulaContent(new Formula(formulaJ3)));
            String formulaJ4 = "=SUM(J1:J3)";
            spreadsheet.setCellContent("J4", new FormulaContent(new Formula(formulaJ4)));

            spreadsheet.saveSpreadsheet("spreadsheet.txt");

            Spreadsheet loadedSpreadsheet = new Spreadsheet();
            loadedSpreadsheet.loadSpreadsheet("spreadsheet.txt");

            Object resultA3 = loadedSpreadsheet.getCellValue("A3");
            System.out.println("A3 (" + formulaA3 + "): " + resultA3); // Expected: 2.0

            Object contentA4 = loadedSpreadsheet.getCellValue("A4");
            System.out.println("A4 " + contentA4); // Expected: This is a test!

            Object resultB3 = loadedSpreadsheet.getCellValue("B3");
            System.out.println("B3 (" + formulaB3 + "): " + resultB3); // Expected: 1 + 1 + 2 + 5 + 10 = 19.0

            Object resultC4 = loadedSpreadsheet.getCellValue("C4");
            System.out.println("C4 (" + formulaC4 + "): " + resultC4); // Expected: (1+1+2 + 5+10+19 + 20+30+40) / 9 = 14.22

            Object resultE1 = loadedSpreadsheet.getCellValue("E1");
            System.out.println("E1 (" + formulaE1 + "): " + resultE1); // Expected: max(1,1,2,5,10,19,20,30,40) = 40.0

            Object resultF1 = loadedSpreadsheet.getCellValue("F1");
            System.out.println("F1 (" + formulaF1 + "): " + resultF1); // Expected: 1 +1 +2 +5 +10 +19 = 38.0

            Object resultG1 = loadedSpreadsheet.getCellValue("G1");
            System.out.println("G1 (" + formulaG1 + "): " + resultG1); // Expected: (5 + 15 +1 +1)/4 = 22 /4 = 5.5

            Object resultI1 = loadedSpreadsheet.getCellValue("I1");
            System.out.println("I1 (" + fromulaI1 + "): " + resultI1); // Expected: 7.0

            Object resultJ4 = loadedSpreadsheet.getCellValue("J4");
            System.out.println("J4 (" + formulaJ4 + "): " + resultJ4); // Expected: 7.0

            // Evaluating H1 should throw an exception
            Object resultH1 = loadedSpreadsheet.getCellValue("H1");
            System.out.println("H1 (" + formulaH1 + "): " + resultH1);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }*/
    }
}