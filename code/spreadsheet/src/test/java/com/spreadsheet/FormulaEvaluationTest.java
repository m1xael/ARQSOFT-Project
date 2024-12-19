package com.spreadsheet;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FormulaEvaluationTest {

    @Test
    public void testSimpleAddition() throws Exception {
        Formula formula = new Formula("=1+2");
        double result = formula.evaluate();
        assertEquals(3.0, result, 0.0001);
    }

    @Test
    public void testMultiplicationAndAddition() throws Exception {
        Formula formula = new Formula("=3*5+4");
        double result = formula.evaluate();
        assertEquals(19.0, result, 0.0001);
    }

    @Test
    public void testParenthesesHandling() throws Exception {
        Formula formula = new Formula("=3*(5+4)");
        double result = formula.evaluate();
        assertEquals(27.0, result, 0.0001);
    }

    @Test
    public void testDivision() throws Exception {
        Formula formula = new Formula("=20/5");
        double result = formula.evaluate();
        assertEquals(4.0, result, 0.0001);
    }

    @Test
    public void testComplexExpression() throws Exception {
        Formula formula = new Formula("=10 - 2*3 + (4/2)");
        double result = formula.evaluate();
        assertEquals(6.0, result, 0.0001);
    }

    @Test
    public void testDecimalNumbers() throws Exception {
        Formula formula = new Formula("=2.5 * 4");
        double result = formula.evaluate();
        assertEquals(10.0, result, 0.0001);
    }

    @Test
    public void testDivisionByZero() {
        Formula formula = new Formula("=10/0");
        try {
            formula.evaluate();
            fail("Expected an Exception to be thrown");
        } catch (Exception e) {
            assertEquals("Division by zero", e.getMessage());
        }
    }

    @Test
    public void testInvalidExpression() {
        Formula formula = new Formula("=10 +");
        try {
            formula.evaluate();
            fail("Expected an Exception to be thrown");
        } catch (Exception e) {
            assertNotNull(e);
            assertTrue(e instanceof Exception);
            assertNotNull("Exception message should not be null", e.getMessage());
        }
    }
}