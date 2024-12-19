package com.spreadsheet;

import java.io.*;
import java.util.*;

public class Spreadsheet {
    private Map<String, Cell> cells;

    public Spreadsheet() {
        cells = new HashMap<>();
    }

    public void setCellContent(String address, Content content) {
        String upperAddress = address.toUpperCase();
        Cell cell = new Cell(upperAddress, content);
        cells.put(upperAddress, cell);
    }

    public Object getCellValue(String address) throws Exception {
        return getCellValue(address.toUpperCase(), new HashSet<>());
    }

    public Object getCellValue(String address, Set<String> visitedCells) throws Exception {
        Cell cell = cells.get(address);
        if (cell != null) {
            Content content = cell.getContent();
            if (content instanceof NumericalContent) {
                return ((NumericalContent) content).getValue();
            } else if (content instanceof TextContent) {
                return ((TextContent) content).getValue();
            } else if (content instanceof FormulaContent) {
                if (visitedCells.contains(address)) {
                    throw new Exception("Circular reference detected at cell " + address);
                }
                visitedCells.add(address);
                Object result = ((FormulaContent) content).getValue(this, visitedCells);
                visitedCells.remove(address);
                return result;
            }
        }
        return null;
    }

    public void saveSpreadsheet(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            int maxRow = getMaxRow();
            int maxCol = getMaxColumn();
            for (int row = 1; row <= maxRow; row++) {
                StringBuilder sb = new StringBuilder();
                for (int col = 1; col <= maxCol; col++) {
                    String cellAddress = getColumnName(col) + row;
                    Cell cell = cells.get(cellAddress);
                    if (cell != null) {
                        Content content = cell.getContent();
                        if (content instanceof FormulaContent) {
                            sb.append(((FormulaContent) content).getFormula().getExpression().replace(";", ","));
                        } else {
                            sb.append(content.toString().replace(";", ","));
                        }
                    }
                    if (col < maxCol) {
                        sb.append(";");
                    }
                }
                writer.write(sb.toString());
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
            int currentRow = 1;
            while ((line = reader.readLine()) != null) {
                String[] cellContents = line.split(";", -1); 
                for (int col = 1; col <= cellContents.length; col++) {
                    String contentStr = cellContents[col - 1].replace(",", ";"); 
                    if (!contentStr.isEmpty()) {
                        String cellAddress = getColumnName(col) + currentRow;
                        Content content = parseContent(contentStr);
                        setCellContent(cellAddress, content);
                    }
                }
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public List<String> expandRange(String range) throws Exception {
        String[] parts = range.split(":");
        if (parts.length != 2) throw new Exception("Invalid range format: " + range);

        String start = parts[0].toUpperCase();
        String end = parts[1].toUpperCase();

        if (!isValidCellReference(start) || !isValidCellReference(end)) {
            throw new Exception("Invalid cell reference in range: " + range);
        }

        int startCol = getColumnNumber(start.replaceAll("[0-9]+", ""));
        int endCol = getColumnNumber(end.replaceAll("[0-9]+", ""));
        int startRow = Integer.parseInt(start.replaceAll("[A-Z]+", ""));
        int endRow = Integer.parseInt(end.replaceAll("[A-Z]+", ""));

        if (startCol > endCol || startRow > endRow) {
            throw new Exception("Invalid range boundaries: " + range);
        }

        List<String> cellsInRange = new ArrayList<>();
        for (int col = startCol; col <= endCol; col++) {
            for (int row = startRow; row <= endRow; row++) {
                String cellAddress = getColumnName(col) + row;
                cellsInRange.add(cellAddress);
            }
        }
        return cellsInRange;
    }

    private int getColumnNumber(String column) {
        int number = 0;
        for (int i = 0; i < column.length(); i++) {
            number = number * 26 + (column.charAt(i) - 'A' + 1);
        }
        return number;
    }

    private String getColumnName(int number) {
        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            int rem = (number - 1) % 26;
            sb.insert(0, (char)(rem + 'A'));
            number = (number - 1) / 26;
        }
        return sb.toString();
    }

    private int getMaxRow() {
        return cells.keySet().stream()
                .mapToInt(addr -> Integer.parseInt(addr.replaceAll("[A-Z]+", "")))
                .max()
                .orElse(1);
    }

    private int getMaxColumn() {
        return cells.keySet().stream()
                .map(addr -> addr.replaceAll("[0-9]+", ""))
                .mapToInt(this::getColumnNumber)
                .max()
                .orElse(1);
    }

    private boolean isValidCellReference(String cell) {
        return cell.matches("[A-Z]+[1-9][0-9]*");
    }

}