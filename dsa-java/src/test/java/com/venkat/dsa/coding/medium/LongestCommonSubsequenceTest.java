package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class LongestCommonSubsequenceTest {

    private final LongestCommonSubsequence solution = new LongestCommonSubsequence();

    @Test
    void testExample1() {
        assertEquals(3, solution.longestCommonSubsequence("abcde", "ace"));
    }

    @Test
    void testIdenticalStrings() {
        assertEquals(3, solution.longestCommonSubsequence("abc", "abc"));
    }

    @Test
    void testNoCommon() {
        assertEquals(0, solution.longestCommonSubsequence("abc", "def"));
    }
}
