package com.spreadsheet;

import java.util.HashMap;
import java.util.Map;

public class FunctionRegistry {
    private static final Map<String, FunctionImplementation> FUNCTIONS = new HashMap<>();

    static {
        FUNCTIONS.put("SUM", new SumFunction());
        FUNCTIONS.put("MIN", new MinFunction());
        FUNCTIONS.put("MAX", new MaxFunction());
        FUNCTIONS.put("AVG", new AvgFunction());
    }

    public static FunctionImplementation getFunction(String name) {
        return FUNCTIONS.get(name.toUpperCase());
    }

    public static boolean isFunction(String name) {
        return FUNCTIONS.containsKey(name.toUpperCase());
    }
}