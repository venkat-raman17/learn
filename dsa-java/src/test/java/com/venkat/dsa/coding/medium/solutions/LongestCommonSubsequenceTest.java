package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LongestCommonSubsequenceTest {

    private final LongestCommonSubsequence sol = new LongestCommonSubsequence();

    // LeetCode example 1: "abcde" vs "ace" -> 3 ("ace")
    @Test
    void example1() {
        assertEquals(3, sol.longestCommonSubsequence("abcde", "ace"));
    }

    // LeetCode example 2: "abc" vs "abc" -> 3 (identical)
    @Test
    void example2() {
        assertEquals(3, sol.longestCommonSubsequence("abc", "abc"));
    }

    // LeetCode example 3: "abc" vs "def" -> 0 (no common characters)
    @Test
    void example3() {
        assertEquals(0, sol.longestCommonSubsequence("abc", "def"));
    }

    // One empty string -> 0
    @Test
    void oneEmpty() {
        assertEquals(0, sol.longestCommonSubsequence("", "abc"));
    }

    // Both empty -> 0
    @Test
    void bothEmpty() {
        assertEquals(0, sol.longestCommonSubsequence("", ""));
    }

    // Classic example: "AGGTAB" vs "GXTXAYB" -> 4 ("GTAB")
    @Test
    void classic() {
        assertEquals(4, sol.longestCommonSubsequence("AGGTAB", "GXTXAYB"));
    }

    // Single character match
    @Test
    void singleCharMatch() {
        assertEquals(1, sol.longestCommonSubsequence("a", "a"));
    }

    // Single character no match
    @Test
    void singleCharNoMatch() {
        assertEquals(0, sol.longestCommonSubsequence("a", "b"));
    }
}
