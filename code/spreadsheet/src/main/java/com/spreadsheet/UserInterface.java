package com.spreadsheet;

import java.util.Scanner;

public class UserInterface {
    private Spreadsheet spreadsheet;
    private Scanner scanner;

    public UserInterface() {
        spreadsheet = new Spreadsheet();
        scanner = new Scanner(System.in);
    }

    public void start() {
        boolean exit = false;
        while (!exit) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    setCellContent();
                    break;
                case "2":
                    viewCellValue();
                    break;
                case "3":
                    saveSpreadsheet();
                    break;
                case "4":
                    loadSpreadsheet();
                    break;
                case "5":
                    exit = true;
                    System.out.println("Exiting the application.");
                    break;
                default:
                    System.out.println("Invalid option. Please select a valid choice.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- Spreadsheet Menu ---");
        System.out.println("1. Set Cell Content");
        System.out.println("2. View Cell Value");
        System.out.println("3. Save Spreadsheet");
        System.out.println("4. Load Spreadsheet");
        System.out.println("5. Exit");
        System.out.print("Please select an option: ");
    }

    private void setCellContent() {
        System.out.print("Enter cell address (e.g., A1): ");
        String address = scanner.nextLine().trim().toUpperCase();
        if (!isValidCellAddress(address)) {
            System.out.println("Invalid cell address.");
            return;
        }

        System.out.print("Enter cell content (number, text, or formula starting with '='): ");
        String contentStr = scanner.nextLine();

        Content content = parseContent(contentStr);
        if (content == null) {
            System.out.println("Invalid content.");
            return;
        }

        spreadsheet.setCellContent(address, content);
        System.out.println("Cell " + address + " updated successfully.");
    }

    private void viewCellValue() {
        System.out.print("Enter cell address (e.g., A1): ");
        String address = scanner.nextLine().trim().toUpperCase();
        if (!isValidCellAddress(address)) {
            System.out.println("Invalid cell address.");
            return;
        }

        try {
            Object value = spreadsheet.getCellValue(address);
            if (value != null) {
                System.out.println("Value of cell " + address + ": " + value);
            } else {
                System.out.println("Cell " + address + " is empty.");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving cell value: " + e.getMessage());
        }
    }

    private void saveSpreadsheet() {
        System.out.print("Enter filename to save (e.g., spreadsheet.txt): ");
        String filename = scanner.nextLine().trim();
        spreadsheet.saveSpreadsheet(filename);
        System.out.println("Spreadsheet saved to " + filename);
    }

    private void loadSpreadsheet() {
        System.out.print("Enter filename to load (e.g., spreadsheet.txt): ");
        String filename = scanner.nextLine().trim();
        spreadsheet.loadSpreadsheet(filename);
        System.out.println("Spreadsheet loaded from " + filename);
    }

    private Content parseContent(String contentStr) {
        if (contentStr.startsWith("=")) {
            return new FormulaContent(new Formula(contentStr));
        } else {
            try {
                double number = Double.parseDouble(contentStr);
                return new NumericalContent(number);
            } catch (NumberFormatException e) {
                return new TextContent(contentStr);
            }
        }
    }

    private boolean isValidCellAddress(String address) {
        return address.matches("[A-Z]+[1-9][0-9]*");
    }
}