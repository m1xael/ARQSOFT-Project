package com.spreadsheet;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Spreadsheet {
    private Map<String, Cell> cells;

    public Spreadsheet() {
        cells = new HashMap<>();
    }

    public void setCellContent(String address, Content content) {
        Cell cell = new Cell(address, content);
        cells.put(address, cell);
    }

    public Content getCellContent(String address) {
        Cell cell = cells.get(address);
        if (cell != null) {
            return cell.getContent();
        }
        return null;
    }

    public Object getCellValue(String address) throws Exception {
        Cell cell = cells.get(address);
        if (cell != null) {
            Content content = cell.getContent();
            if (content instanceof NumericalContent) {
                return ((NumericalContent) content).getValue();
            } else if (content instanceof TextContent) {
                return ((TextContent) content).getValue();
            } else if (content instanceof FormulaContent) {
                return ((FormulaContent) content).getValue();
            }
        }
        return null;
    }

    public void saveSpreadsheet(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Cell cell : cells.values()) {
                String address = cell.getAddress();
                Content content = cell.getContent();
                String contentType = "";
                String contentValue = "";

                if (content instanceof NumericalContent) {
                    contentType = "number";
                    contentValue = ((NumericalContent) content).getValue().toString();
                } else if (content instanceof TextContent) {
                    contentType = "text";
                    contentValue = ((TextContent) content).getValue();
                } else if (content instanceof FormulaContent) {
                    contentType = "formula";
                    contentValue = ((FormulaContent) content).getFormula().getExpression();
                }

                writer.write(address + ";" + contentType + ";" + contentValue);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSpreadsheet(String filename) {
        cells.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", 3);
                if (parts.length == 3) {
                    String address = parts[0];
                    String contentType = parts[1];
                    String contentValue = parts[2];
                    Content content = null;

                    switch (contentType) {
                        case "number":
                            Number numberValue = Double.parseDouble(contentValue);
                            content = new NumericalContent(numberValue);
                            break;
                        case "text":
                            content = new TextContent(contentValue);
                            break;
                        case "formula":
                            Formula formula = new Formula(contentValue);
                            content = new FormulaContent(formula);
                            break;
                    }

                    if (content != null) {
                        setCellContent(address, content);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}