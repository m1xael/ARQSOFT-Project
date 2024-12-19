package com.spreadsheet;
import java.util.Queue;

public class Formula {
    private String expression;

    public Formula(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public double evaluate() throws Exception {
        String expr = expression.startsWith("=") ? expression.substring(1) : expression;
        Queue<String> rpn = ShuntingYardParser.infixToRPN(expr);
        return ShuntingYardParser.evaluateRPN(rpn);
    }

    @Override
    public String toString() {
        return expression;
    }
}