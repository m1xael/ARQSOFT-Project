package com.spreadsheet;

import java.util.List;

public class MinFunction implements FunctionImplementation {
    @Override
    public double execute(List<String> args, Spreadsheet spreadsheet) throws Exception {
        if (args.isEmpty()) {
            throw new Exception("MIN requires at least one argument.");
        }
        double min = Double.MAX_VALUE;
        boolean found = false;
        for (String arg : args) {
            double currentValue;
            if (isRange(arg)) { 
                List<String> rangeCells = spreadsheet.expandRange(arg);
                for (String cell : rangeCells) {
                    Object value = spreadsheet.getCellValue(cell);
                    if (value instanceof Number) {
                        currentValue = ((Number) value).doubleValue();
                        if (currentValue < min) {
                            min = currentValue;
                            found = true;
                        }
                    } else {
                        throw new Exception("Non-numeric value in cell " + cell + " for MIN.");
                    }
                }
            } else if (isCellReference(arg)) { 
                Object value = spreadsheet.getCellValue(arg);
                if (value instanceof Number) {
                    currentValue = ((Number) value).doubleValue();
                    if (currentValue < min) {
                        min = currentValue;
                        found = true;
                    }
                } else {
                    throw new Exception("Non-numeric value in cell " + arg + " for MIN.");
                }
            } else { 
                try {
                    currentValue = Double.parseDouble(arg);
                    if (currentValue < min) {
                        min = currentValue;
                        found = true;
                    }
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid numerical value: " + arg + " for MIN.");
                }
            }
        }
        if (!found) {
            throw new Exception("MIN requires at least one numeric argument.");
        }
        return min;
    }

    private boolean isRange(String token) {
        return token.matches("[A-Z]+[0-9]+:[A-Z]+[0-9]+");
    }

    private boolean isCellReference(String token) {
        return token.toUpperCase().matches("[A-Z]+[0-9]+");
    }
}