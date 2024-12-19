package com.spreadsheet;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SpreadsheetTest {

    @Test
    public void testSetAndGetNumericalContent() throws Exception {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("A1", new NumericalContent(42));

        Object value = spreadsheet.getCellValue("A1");
        assertTrue(value instanceof Number);
        assertEquals(42.0, ((Number) value).doubleValue());
    }

    @Test
    public void testSetAndGetTextContent() throws Exception {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("A1", new TextContent("Hello World"));

        Object value = spreadsheet.getCellValue("A1");
        assertTrue(value instanceof String);
        assertEquals("Hello World", value);
    }

    @Test
    public void testSetAndGetEmptyTextContent() throws Exception {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("A1", new TextContent(""));

        Object value = spreadsheet.getCellValue("A1");
        assertTrue(value instanceof String);
        assertEquals("", value);
    }

    @Test
    public void testSimpleFormulaEvaluation() throws Exception {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("A1", new NumericalContent(5));
        spreadsheet.setCellContent("A2", new NumericalContent(10));
        spreadsheet.setCellContent("A3", new FormulaContent(new Formula("=A1 + A2")));

        Object value = spreadsheet.getCellValue("A3");
        assertTrue(value instanceof Double);
        assertEquals(15.0, (Double) value);
    }

    @Test
    public void testFormulaWithFunctionSUM() throws Exception {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("A1", new NumericalContent(3));
        spreadsheet.setCellContent("A2", new NumericalContent(7));
        spreadsheet.setCellContent("A3", new NumericalContent(2));
        spreadsheet.setCellContent("A4", new FormulaContent(new Formula("=SUM(A1;A2;A3;5)")));

        Object value = spreadsheet.getCellValue("A4");
        assertTrue(value instanceof Double);
        assertEquals(17.0, (Double) value);
    }

    @Test
    public void testFormulaWithFunctionMIN() throws Exception {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("B1", new NumericalContent(10));
        spreadsheet.setCellContent("B2", new NumericalContent(5));
        spreadsheet.setCellContent("B3", new NumericalContent(15));
        spreadsheet.setCellContent("B4", new FormulaContent(new Formula("=MIN(B1;B2;B3;3)")));

        Object value = spreadsheet.getCellValue("B4");
        assertTrue(value instanceof Double);
        assertEquals(3.0, (Double) value);
    }

    @Test
    public void testFormulaWithFunctionMAX() throws Exception {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("C1", new NumericalContent(10));
        spreadsheet.setCellContent("C2", new NumericalContent(20));
        spreadsheet.setCellContent("C3", new NumericalContent(15));
        spreadsheet.setCellContent("C4", new FormulaContent(new Formula("=MAX(C1;C2;C3;25)")));

        Object value = spreadsheet.getCellValue("C4");
        assertTrue(value instanceof Double);
        assertEquals(25.0, (Double) value);
    }

    @Test
    public void testFormulaWithFunctionAVG() throws Exception {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("D1", new NumericalContent(10));
        spreadsheet.setCellContent("D2", new NumericalContent(20));
        spreadsheet.setCellContent("D3", new NumericalContent(30));
        spreadsheet.setCellContent("D4", new FormulaContent(new Formula("=AVG(D1:D3;40)")));

        Object value = spreadsheet.getCellValue("D4");
        assertTrue(value instanceof Double);
        assertEquals(25.0, (Double) value); 
    }

    @Test
    public void testNestedFunctions() throws Exception {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("E1", new NumericalContent(2));
        spreadsheet.setCellContent("E2", new NumericalContent(3));
        spreadsheet.setCellContent("E3", new NumericalContent(4));
        spreadsheet.setCellContent("E4", new FormulaContent(new Formula("=SUM(E1;MAX(E2;E3);MIN(E1;E2))")));

        Object value = spreadsheet.getCellValue("E4");
        assertTrue(value instanceof Double);
        assertEquals(8.0, (Double) value); 
    }

    @Test
    public void testRangeInFormula() throws Exception {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("F1", new NumericalContent(1));
        spreadsheet.setCellContent("F2", new NumericalContent(2));
        spreadsheet.setCellContent("F3", new NumericalContent(3));
        spreadsheet.setCellContent("F4", new NumericalContent(4));
        spreadsheet.setCellContent("F5", new FormulaContent(new Formula("=SUM(F1:F4)")));

        Object value = spreadsheet.getCellValue("F5");
        assertTrue(value instanceof Double);
        assertEquals(10.0, (Double) value);
    }

    @Test
    public void testCircularDependencyDetection() {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("G1", new FormulaContent(new Formula("=G2 + 1")));
        spreadsheet.setCellContent("G2", new FormulaContent(new Formula("=G1 + 1")));

        Exception exception = assertThrows(Exception.class, () -> {
            spreadsheet.getCellValue("G1");
        });

        String expectedMessage = "Circular reference detected at cell G1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testDivisionByZero() {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("H1", new NumericalContent(10));
        spreadsheet.setCellContent("H2", new NumericalContent(0));
        spreadsheet.setCellContent("H3", new FormulaContent(new Formula("=H1 / H2")));

        Exception exception = assertThrows(Exception.class, () -> {
            spreadsheet.getCellValue("H3");
        });

        String expectedMessage = "Division by zero";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testInvalidFormulaSyntax() {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("I1", new FormulaContent(new Formula("=1 + (2 * 3")));

        Exception exception = assertThrows(Exception.class, () -> {
            spreadsheet.getCellValue("I1");
        });

        String expectedMessage = "Mismatched parentheses";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void testSaveAndLoadSpreadsheet() {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("A1", new NumericalContent(100));
        spreadsheet.setCellContent("B1", new TextContent("Sample"));
        spreadsheet.setCellContent("C1", new FormulaContent(new Formula("=A1 * 2")));
        spreadsheet.saveSpreadsheet("test_spreadsheet.txt");

        Spreadsheet loadedSpreadsheet = new Spreadsheet();
        loadedSpreadsheet.loadSpreadsheet("test_spreadsheet.txt");

        try {
            Object valueA1 = loadedSpreadsheet.getCellValue("A1");
            assertEquals(100.0, ((Number) valueA1).doubleValue());

            Object valueB1 = loadedSpreadsheet.getCellValue("B1");
            assertEquals("Sample", valueB1);

            Object valueC1 = loadedSpreadsheet.getCellValue("C1");
            assertEquals(200.0, (Double) valueC1);
        } catch (Exception e) {
            fail("Exception occurred during test: " + e.getMessage());
        }
    }

    @Test
    public void testEmptyCellValue() throws Exception {
        Spreadsheet spreadsheet = new Spreadsheet();

        Object value = spreadsheet.getCellValue("Z99");
        assertNull(value);
    }

    @Test
    public void testTextContentAsNumber() throws Exception {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("K1", new TextContent("123"));

        Object value = spreadsheet.getCellValue("K1");
        assertTrue(value instanceof String);
        assertEquals("123", value);
    }

    @Test
    public void testFunctionWithInvalidArgument() {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("M1", new TextContent("Invalid"));
        spreadsheet.setCellContent("M2", new FormulaContent(new Formula("=SUM(M1)")));

        Exception exception = assertThrows(Exception.class, () -> {
            spreadsheet.getCellValue("M2");
        });

        String expectedMessage = "Non-numeric value in cell M1 for SUM";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testRangeExpansion() throws Exception {
        Spreadsheet spreadsheet = new Spreadsheet();
        List<String> range = spreadsheet.expandRange("A1:B2");

        assertEquals(4, range.size());
        assertTrue(range.contains("A1"));
        assertTrue(range.contains("A2"));
        assertTrue(range.contains("B1"));
        assertTrue(range.contains("B2"));
    }

    @Test
    public void testInvalidRangeExpansion() {
        Spreadsheet spreadsheet = new Spreadsheet();
        Exception exception = assertThrows(Exception.class, () -> {
            spreadsheet.expandRange("B2:A1");
        });

        String expectedMessage = "Invalid range boundaries";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFunctionAsArgumentToFunction() throws Exception {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("O1", new NumericalContent(5));
        spreadsheet.setCellContent("O2", new NumericalContent(10));
        spreadsheet.setCellContent("O3", new FormulaContent(new Formula("=SUM(MIN(O1;O2);MAX(O1;O2))")));

        Object value = spreadsheet.getCellValue("O3");
        assertTrue(value instanceof Double);
        assertEquals(15.0, (Double) value);
    }

    @Test
    public void testSelfReferenceInFormula() {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setCellContent("P1", new FormulaContent(new Formula("=P1 + 1")));

        Exception exception = assertThrows(Exception.class, () -> {
            spreadsheet.getCellValue("P1");
        });

        String expectedMessage = "Circular reference detected at cell P1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}