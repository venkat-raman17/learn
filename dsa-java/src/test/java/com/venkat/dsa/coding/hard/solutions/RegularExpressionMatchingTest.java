package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegularExpressionMatchingTest {

    private final RegularExpressionMatching sol = new RegularExpressionMatching();

    // LeetCode example 1: s="aa", p="a" -> false
    @Test
    void example1() {
        assertFalse(sol.isMatch("aa", "a"));
    }

    // LeetCode example 2: s="aa", p="a*" -> true (a* matches "aa")
    @Test
    void example2() {
        assertTrue(sol.isMatch("aa", "a*"));
    }

    // LeetCode example 3: s="ab", p=".*" -> true (.* matches anything)
    @Test
    void example3() {
        assertTrue(sol.isMatch("ab", ".*"));
    }

    // s="aab", p="c*a*b" -> true (c* matches "", a* matches "aa", b matches "b")
    @Test
    void example4() {
        assertTrue(sol.isMatch("aab", "c*a*b"));
    }

    // s="mississippi", p="mis*is*p*." -> false
    @Test
    void mississippi() {
        assertFalse(sol.isMatch("mississippi", "mis*is*p*."));
    }

    // Both empty
    @Test
    void bothEmpty() {
        assertTrue(sol.isMatch("", ""));
    }

    // Empty string, pattern "a*" -> true (zero a's)
    @Test
    void emptyStrStarPattern() {
        assertTrue(sol.isMatch("", "a*"));
    }

    // Empty string, pattern "." -> false
    @Test
    void emptyStrDotPattern() {
        assertFalse(sol.isMatch("", "."));
    }

    // s="a", p="." -> true
    @Test
    void dotMatch() {
        assertTrue(sol.isMatch("a", "."));
    }

    // s="a", p="b" -> false
    @Test
    void literalMismatch() {
        assertFalse(sol.isMatch("a", "b"));
    }

    // s="abc", p="a.c" -> true
    @Test
    void dotInMiddle() {
        assertTrue(sol.isMatch("abc", "a.c"));
    }

    // s="aaa", p="a*a" -> true
    @Test
    void starFollowedByLiteral() {
        assertTrue(sol.isMatch("aaa", "a*a"));
    }

    // s="", p="a*b*c*" -> true (all zero occurrences)
    @Test
    void allStarsMatchEmpty() {
        assertTrue(sol.isMatch("", "a*b*c*"));
    }
}
