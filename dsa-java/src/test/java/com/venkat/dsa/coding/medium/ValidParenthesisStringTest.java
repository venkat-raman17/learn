package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class ValidParenthesisStringTest {

    private final ValidParenthesisString solution = new ValidParenthesisString();

    @Test
    void testSimpleValid() {
        assertTrue(solution.checkValidString("()"));
    }

    @Test
    void testStarAsEmpty() {
        assertTrue(solution.checkValidString("(*)"));
    }

    @Test
    void testStarAsClose() {
        assertTrue(solution.checkValidString("(*))"));
    }
}
