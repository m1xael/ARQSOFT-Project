package com.spreadsheet;

public class FormulaContent extends Content {
    private Formula formula;

    public FormulaContent(Formula formula) {
        this.formula = formula;
    }

    public Formula getFormula() {
        return formula;
    }

    public double getValue() throws Exception {
        return formula.evaluate();
    }

    @Override
    public String toString() {
        return formula.getExpression();
    }
}