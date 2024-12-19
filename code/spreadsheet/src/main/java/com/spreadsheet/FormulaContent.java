package com.spreadsheet;

import java.util.Set;

public class FormulaContent extends Content {
    private Formula formula;

    public FormulaContent(Formula formula) {
        this.formula = formula;
    }

    public Formula getFormula() {
        return formula;
    }

    public double getValue(Spreadsheet spreadsheet, Set<String> visitedCells) throws Exception {
        return formula.evaluate(spreadsheet, visitedCells);
    }

    @Override
    public String toString() {
        return formula.getExpression();
    }
}