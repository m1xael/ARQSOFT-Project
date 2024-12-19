package com.spreadsheet;

import java.util.List;

public class MaxFunction implements FunctionImplementation {
    @Override
    public double execute(List<String> args, Spreadsheet spreadsheet) throws Exception {
        if (args.isEmpty()) {
            throw new Exception("MAX requires at least one argument.");
        }
        double max = Double.MIN_VALUE;
        boolean found = false;
        for (String arg : args) {
            double currentValue;
            if (isRange(arg)) { 
                List<String> rangeCells = spreadsheet.expandRange(arg);
                for (String cell : rangeCells) {
                    Object value = spreadsheet.getCellValue(cell);
                    if (value instanceof Number) {
                        currentValue = ((Number) value).doubleValue();
                        if (currentValue > max) {
                            max = currentValue;
                            found = true;
                        }
                    } else {
                        throw new Exception("Non-numeric value in cell " + cell + " for MAX.");
                    }
                }
            } else if (isCellReference(arg)) { 
                Object value = spreadsheet.getCellValue(arg);
                if (value instanceof Number) {
                    currentValue = ((Number) value).doubleValue();
                    if (currentValue > max) {
                        max = currentValue;
                        found = true;
                    }
                } else {
                    throw new Exception("Non-numeric value in cell " + arg + " for MAX.");
                }
            } else {
                try {
                    currentValue = Double.parseDouble(arg);
                    if (currentValue > max) {
                        max = currentValue;
                        found = true;
                    }
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid numerical value: " + arg + " for MAX.");
                }
            }
        }
        if (!found) {
            throw new Exception("MAX requires at least one numeric argument.");
        }
        return max;
    }

    private boolean isRange(String token) {
        return token.matches("[A-Z]+[0-9]+:[A-Z]+[0-9]+");
    }

    private boolean isCellReference(String token) {
        return token.toUpperCase().matches("[A-Z]+[0-9]+");
    }
}