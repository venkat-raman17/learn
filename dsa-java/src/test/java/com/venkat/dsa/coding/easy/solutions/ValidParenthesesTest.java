package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidParenthesesTest {

    private final ValidParentheses solution = new ValidParentheses();

    // Official LeetCode examples
    @Test
    void example1_simpleParen() {
        assertTrue(solution.isValid("()"));
    }

    @Test
    void example2_mixedTypes() {
        assertTrue(solution.isValid("()[]{}"));
    }

    @Test
    void example3_mismatch() {
        assertFalse(solution.isValid("(]"));
    }

    // Edge cases
    @Test
    void singleOpenBracket() {
        assertFalse(solution.isValid("("));
    }

    @Test
    void singleCloseBracket() {
        assertFalse(solution.isValid(")"));
    }

    @Test
    void emptyString() {
        assertTrue(solution.isValid(""));
    }

    @Test
    void nestedValid() {
        assertTrue(solution.isValid("{[()]}"));
    }

    @Test
    void nestedInvalid() {
        assertFalse(solution.isValid("{[(])}"));
    }

    @Test
    void wrongOrder() {
        // Closing before opening
        assertFalse(solution.isValid("]["));
    }

    @Test
    void oddLength() {
        assertFalse(solution.isValid("(()"));
    }

    @Test
    void allSameTypeNested() {
        assertTrue(solution.isValid("((()))"));
    }

    @Test
    void closingWithEmptyStack() {
        assertFalse(solution.isValid(")))"));
    }
}
