package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class EvaluateReversePolishNotationTest {

    private final EvaluateReversePolishNotation solution = new EvaluateReversePolishNotation();

    @Test
    public void testAddThenMultiply() {
        assertEquals(9, solution.evalRPN(new String[]{"2", "1", "+", "3", "*"}));
    }

    @Test
    public void testDivisionThenAdd() {
        assertEquals(6, solution.evalRPN(new String[]{"4", "13", "5", "/", "+"}));
    }

    @Test
    public void testMixedOperations() {
        assertEquals(22, solution.evalRPN(
            new String[]{"10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"}));
    }
}
