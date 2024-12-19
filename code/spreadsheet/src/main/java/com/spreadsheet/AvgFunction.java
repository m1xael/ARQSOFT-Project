
package com.spreadsheet;

import java.util.List;

public class AvgFunction implements FunctionImplementation {
    @Override
    public double execute(List<String> args, Spreadsheet spreadsheet) throws Exception {
        if (args.isEmpty()) {
            throw new Exception("AVG requires at least one argument.");
        }
        double sum = 0;
        int count = 0;
        for (String arg : args) {
            if (isRange(arg)) { 
                List<String> rangeCells = spreadsheet.expandRange(arg);
                for (String cell : rangeCells) {
                    Object value = spreadsheet.getCellValue(cell);
                    if (value instanceof Number) {
                        sum += ((Number) value).doubleValue();
                        count++;
                    } else {
                        throw new Exception("Non-numeric value in cell " + cell + " for AVG.");
                    }
                }
            } else if (isCellReference(arg)) { 
                Object value = spreadsheet.getCellValue(arg);
                if (value instanceof Number) {
                    sum += ((Number) value).doubleValue();
                    count++;
                } else {
                    throw new Exception("Non-numeric value in cell " + arg + " for AVG.");
                }
            } else { 
                try {
                    sum += Double.parseDouble(arg);
                    count++;
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid numerical value: " + arg + " for AVG.");
                }
            }
        }
        if (count == 0) {
            throw new Exception("AVG requires at least one numeric argument.");
        }
        return sum / count;
    }

    private boolean isRange(String token) {
        return token.matches("[A-Z]+[0-9]+:[A-Z]+[0-9]+");
    }

    private boolean isCellReference(String token) {
        return token.toUpperCase().matches("[A-Z]+[0-9]+");
    }
}