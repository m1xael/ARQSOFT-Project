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

        for (String token : tokens) {
            if (isNumber(token)) {
                outputQueue.add(token);
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
                    throw new Exception("Mismatched parentheses");
                }
                operatorStack.pop();
            } else {
                throw new Exception("Invalid token: " + token);
            }
        }

        while (!operatorStack.isEmpty()) {
            String op = operatorStack.pop();
            if (op.equals("(") || op.equals(")")) {
                throw new Exception("Mismatched parentheses");
            }
            outputQueue.add(op);
        }

        return outputQueue;
    }

    public static double evaluateRPN(Queue<String> rpnQueue) throws Exception {
        Stack<Double> stack = new Stack<>();
        while (!rpnQueue.isEmpty()) {
            String token = rpnQueue.poll();
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new Exception("Invalid expression");
                }
                double b = stack.pop();
                double a = stack.pop();
                stack.push(applyOperator(token, a, b));
            } else {
                throw new Exception("Invalid token in RPN expression: " + token);
            }
        }
        if (stack.size() != 1) {
            throw new Exception("Invalid expression");
        }
        return stack.pop();
    }

    private static double applyOperator(String op, double a, double b) throws Exception {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> {
                if (b == 0) throw new Exception("Division by zero");
                yield a / b;
            }
            default -> throw new Exception("Unknown operator: " + op);
        };
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

    private static List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isWhitespace(c)) {
                continue;
            }

            if (Character.isDigit(c) || c == '.') {
                sb.append(c);
            } else {
                if (sb.length() > 0) {
                    tokens.add(sb.toString());
                    sb.setLength(0);
                }
                if (isOperatorChar(c) || c == '(' || c == ')') {
                    tokens.add(String.valueOf(c));
                } else {
                    tokens.add(String.valueOf(c));
                }
            }
        }

        if (sb.length() > 0) {
            tokens.add(sb.toString());
        }

        return tokens;
    }

    private static boolean isOperatorChar(char c) {
        return OPERATORS.containsKey(String.valueOf(c));
    }
}