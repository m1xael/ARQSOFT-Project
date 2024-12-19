package com.spreadsheet;

import java.util.*;

public class ShuntingYardParser {
    private static final Map<String, Integer> OPERATORS = Map.of(
        "+", 1,
        "-", 1,
        "*", 2,
        "/", 2
    );

    public static Queue<String> infixToRPN(String expression) throws Exception {
        List<String> tokens = tokenize(expression);
        Queue<String> outputQueue = new LinkedList<>();
        Stack<String> operatorStack = new Stack<>();
        Stack<Integer> functionArgCount = new Stack<>();

        for (String token : tokens) {
            if (isNumber(token) || isCellReference(token) || isRange(token)) {
                outputQueue.add(token);
            } else if (FunctionRegistry.isFunction(token)) {
                operatorStack.push(token.toUpperCase());
                functionArgCount.push(1); 
            } else if (token.equals(";")) { 
                if (operatorStack.isEmpty()) {
                    throw new Exception("Misplaced function argument separator or no function context.");
                }
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    outputQueue.add(operatorStack.pop());
                }
                if (operatorStack.isEmpty() || !operatorStack.peek().equals("(")) {
                    throw new Exception("Misplaced function argument separator or mismatched parentheses.");
                }
                if (!functionArgCount.isEmpty()) {
                    functionArgCount.push(functionArgCount.pop() + 1);
                } else {
                    throw new Exception("Function argument count stack is empty.");
                }
            } else if (isOperator(token)) {
                while (!operatorStack.isEmpty() && isOperator(operatorStack.peek()) &&
                        OPERATORS.get(token) <= OPERATORS.get(operatorStack.peek())) {
                    outputQueue.add(operatorStack.pop());
                }
                operatorStack.push(token);
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    outputQueue.add(operatorStack.pop());
                }
                if (operatorStack.isEmpty()) {
                    throw new Exception("Mismatched parentheses.");
                }
                operatorStack.pop();
                if (!operatorStack.isEmpty() && FunctionRegistry.isFunction(operatorStack.peek())) {
                    String func = operatorStack.pop();
                    int argCount = functionArgCount.pop();
                    outputQueue.add(func + ":" + argCount);
                }
            } else {
                throw new Exception("Invalid token: " + token);
            }
        }

        while (!operatorStack.isEmpty()) {
            String op = operatorStack.pop();
            if (op.equals("(") || op.equals(")")) {
                throw new Exception("Mismatched parentheses.");
            }
            outputQueue.add(op);
        }

        return outputQueue;
    }

    public static double evaluateRPN(Queue<String> rpnQueue, Spreadsheet spreadsheet, Set<String> visitedCells) throws Exception {
        Stack<Object> stack = new Stack<>();
        while (!rpnQueue.isEmpty()) {
            String token = rpnQueue.poll();
            if (isNumber(token)) {
                stack.push(token); 
            } else if (isCellReference(token) || isRange(token)) {
                stack.push(token.toUpperCase());
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new Exception("Invalid RPN expression.");
                }
                Object objB = stack.pop();
                Object objA = stack.pop();
                double a, b;
                if (objA instanceof String) {
                    a = resolveOperand((String) objA, spreadsheet, visitedCells);
                } else {
                    throw new Exception("Invalid operand type for operator " + token);
                }
                if (objB instanceof String) {
                    b = resolveOperand((String) objB, spreadsheet, visitedCells);
                } else {
                    throw new Exception("Invalid operand type for operator " + token);
                }
                stack.push(applyOperator(token, a, b));
            } else if (isFunctionWithArgs(token)) {
                String[] parts = token.split(":");
                String funcName = parts[0];
                int argCount = Integer.parseInt(parts[1]);
                if (stack.size() < argCount) {
                    throw new Exception("Insufficient arguments for function " + funcName);
                }
                List<String> args = new ArrayList<>();
                for (int i = 0; i < argCount; i++) {
                    Object obj = stack.pop();
                    if (obj instanceof Double) {
                        args.add(String.valueOf(obj));
                    } else if (obj instanceof String) {
                        args.add((String) obj);
                    } else {
                        throw new Exception("Invalid argument type for function " + funcName);
                    }
                }
                FunctionImplementation func = FunctionRegistry.getFunction(funcName);
                if (func == null) {
                    throw new Exception("Unknown function: " + funcName);
                }
                double result = func.execute(args, spreadsheet);
                stack.push(result);
            } else {
                throw new Exception("Invalid token in RPN expression: " + token);
            }
        }
        if (stack.size() != 1) {
            throw new Exception("Invalid RPN expression.");
        }
        Object finalResult = stack.pop();
        if (finalResult instanceof Double) {
            return (Double) finalResult;
        } else if (finalResult instanceof String) {
            try {
                return Double.parseDouble((String) finalResult);
            } catch (NumberFormatException e) {
                throw new Exception("Final result is not a number.");
            }
        } else {
            throw new Exception("Final result is of an unexpected type.");
        }
    }

    private static double resolveOperand(String operand, Spreadsheet spreadsheet, Set<String> visitedCells) throws Exception {
        if (isNumber(operand)) {
            return Double.parseDouble(operand);
        } else if (isCellReference(operand)) {
            Object value = spreadsheet.getCellValue(operand, visitedCells);
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            } else {
                throw new Exception("Cell " + operand + " does not contain a numerical value.");
            }
        } else if (isRange(operand)) {
            throw new Exception("Ranges cannot be used as standalone operands.");
        } else {
            throw new Exception("Invalid operand: " + operand);
        }
    }

    private static double applyOperator(String op, double a, double b) throws Exception {
        switch (op) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0) throw new Exception("Division by zero.");
                return a / b;
            default:
                throw new Exception("Unknown operator: " + op);
        }
    }

    private static boolean isOperator(String token) {
        return OPERATORS.containsKey(token);
    }

    private static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isCellReference(String token) {
        return token.toUpperCase().matches("[A-Z]+[0-9]+");
    }

    private static boolean isRange(String token) {
        return token.matches("[A-Z]+[0-9]+:[A-Z]+[0-9]+");
    }

    private static boolean isFunctionWithArgs(String token) {
        return token.contains(":");
    }

    private static List<String> tokenize(String expression) throws IllegalArgumentException {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        int i = 0;
        while (i < expression.length()) {
            char c = expression.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            if (Character.isLetter(c)) {
                sb.setLength(0);
                while (i < expression.length() && Character.isLetter(expression.charAt(i))) {
                    sb.append(expression.charAt(i));
                    i++;
                }
                String funcOrCell = sb.toString();
                if (i < expression.length() && expression.charAt(i) == '(') {
                    tokens.add(funcOrCell.toUpperCase());
                } else {
                    while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                        sb.append(expression.charAt(i));
                        i++;
                    }
                    tokens.add(sb.toString().toUpperCase());
                    continue;
                }
            } else if (Character.isDigit(c) || c == '.') {
                sb.setLength(0);
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i));
                    i++;
                }
                tokens.add(sb.toString());
            } else if (c == ':' ) {
                if (tokens.isEmpty() || !isCellReference(tokens.get(tokens.size() -1))) {
                    throw new IllegalArgumentException("Invalid range format near ':'");
                }
                tokens.add(":");
                i++;
            } else if (c == '(' || c == ')' || c == '+' || c == '-' || c == '*' || c == '/' || c == ';') {
                tokens.add(String.valueOf(c));
                i++;
            } else {
                throw new IllegalArgumentException("Invalid character in expression: " + c);
            }
        }

        List<String> mergedTokens = new ArrayList<>();
        for (int j = 0; j < tokens.size(); j++) {
            String current = tokens.get(j);
            if (isCellReference(current)) {
                if (j + 2 < tokens.size() && tokens.get(j +1).equals(":") && isCellReference(tokens.get(j +2))) {
                    String range = current + ":" + tokens.get(j +2);
                    mergedTokens.add(range);
                    j += 2; 
                } else {
                    mergedTokens.add(current);
                }
            } else {
                mergedTokens.add(current);
            }
        }

        return mergedTokens;
    }
}