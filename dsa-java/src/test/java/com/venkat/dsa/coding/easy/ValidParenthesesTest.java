package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class ValidParenthesesTest {

    private final ValidParentheses solution = new ValidParentheses();

    @Test
    public void testValidMixed() {
        assertTrue(solution.isValid("()[]{}"));
    }

    @Test
    public void testInvalidMismatch() {
        assertFalse(solution.isValid("(]"));
    }

    @Test
    public void testNestedValid() {
        assertTrue(solution.isValid("{[()]}"));
    }
}
