package com.spreadsheet;

import java.util.List;

public interface FunctionImplementation {
    double execute(List<String> args, Spreadsheet spreadsheet) throws Exception;
}