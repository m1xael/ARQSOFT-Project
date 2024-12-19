package com.spreadsheet;

import java.util.List;

public class SumFunction implements FunctionImplementation {
    @Override
    public double execute(List<String> args, Spreadsheet spreadsheet) throws Exception {
        double sum = 0;
        for (String arg : args) {
            if (isRange(arg)) { 
                List<String> rangeCells = spreadsheet.expandRange(arg);
                for (String cell : rangeCells) {
                    Object value = spreadsheet.getCellValue(cell);
                    if (value instanceof Number) {
                        sum += ((Number) value).doubleValue();
                    } else {
                        throw new Exception("Non-numeric value in cell " + cell + " for SUM.");
                    }
                }
            } else if (isCellReference(arg)) { 
                Object value = spreadsheet.getCellValue(arg);
                if (value instanceof Number) {
                    sum += ((Number) value).doubleValue();
                } else {
                    throw new Exception("Non-numeric value in cell " + arg + " for SUM.");
                }
            } else { 
                try {
                    sum += Double.parseDouble(arg);
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid numerical value: " + arg + " for SUM.");
                }
            }
        }
        return sum;
    }

    private boolean isRange(String token) {
        return token.matches("[A-Z]+[0-9]+:[A-Z]+[0-9]+");
    }

    private boolean isCellReference(String token) {
        return token.toUpperCase().matches("[A-Z]+[0-9]+");
    }
}