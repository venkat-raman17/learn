package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class RegularExpressionMatchingTest {

    private final RegularExpressionMatching solution = new RegularExpressionMatching();

    @Test
    void testNoMatch() {
        assertFalse(solution.isMatch("aa", "a"));
    }

    @Test
    void testStarMatchesMultiple() {
        assertTrue(solution.isMatch("aa", "a*"));
    }

    @Test
    void testDotStarMatchesAll() {
        assertTrue(solution.isMatch("ab", ".*"));
    }
}
