package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EvaluateReversePolishNotationTest {

    private final EvaluateReversePolishNotation solution = new EvaluateReversePolishNotation();

    // Official LeetCode examples
    @Test
    void example1_additionAndMultiplication() {
        // (2 + 1) * 3 = 9
        assertEquals(9, solution.evalRPN(new String[]{"2", "1", "+", "3", "*"}));
    }

    @Test
    void example2_divisionAndAddition() {
        // 4 + (13 / 5) = 4 + 2 = 6  (integer division)
        assertEquals(6, solution.evalRPN(new String[]{"4", "13", "5", "/", "+"}));
    }

    @Test
    void example3_complex() {
        // ((10 * (6 / ((9 + 3) * -11))) + 17) + 5
        // 12 * -11 = -132; 6 / -132 = 0; 10 * 0 = 0; 0 + 17 = 17; 17 + 5 = 22
        String[] tokens = {"10", "6", "9", "3", "+", "11", "*", "/", "*", "17", "+", "5", "+"};
        assertEquals(22, solution.evalRPN(tokens));
    }

    // Edge cases
    @Test
    void singleNumber() {
        assertEquals(42, solution.evalRPN(new String[]{"42"}));
    }

    @Test
    void subtraction() {
        // 5 - 3 = 2
        assertEquals(2, solution.evalRPN(new String[]{"5", "3", "-"}));
    }

    @Test
    void subtractionOrder() {
        // 3 - 5 = -2 (order matters)
        assertEquals(-2, solution.evalRPN(new String[]{"3", "5", "-"}));
    }

    @Test
    void divisionTruncatesPositive() {
        // 7 / 2 = 3 (not 3.5)
        assertEquals(3, solution.evalRPN(new String[]{"7", "2", "/"}));
    }

    @Test
    void divisionTruncatesTowardZeroNegative() {
        // -7 / 2 = -3 (truncate toward zero, not -4)
        assertEquals(-3, solution.evalRPN(new String[]{"-7", "2", "/"}));
    }

    @Test
    void negativeNumbers() {
        // -3 + -2 = -5
        assertEquals(-5, solution.evalRPN(new String[]{"-3", "-2", "+"}));
    }

    @Test
    void multiplicationByZero() {
        assertEquals(0, solution.evalRPN(new String[]{"100", "0", "*"}));
    }
}
