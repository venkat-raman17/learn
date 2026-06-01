package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LongestPalindromicSubstringTest {

    private final LongestPalindromicSubstring solution = new LongestPalindromicSubstring();

    @Test
    void example1() {
        // "babad" -> "bab" or "aba" (both length 3 are valid)
        String result = solution.longestPalindrome("babad");
        assertTrue(result.equals("bab") || result.equals("aba"));
    }

    @Test
    void example2() {
        // "cbbd" -> "bb"
        assertEquals("bb", solution.longestPalindrome("cbbd"));
    }

    @Test
    void singleChar() {
        assertEquals("a", solution.longestPalindrome("a"));
    }

    @Test
    void entireStringIsPalindrome() {
        assertEquals("racecar", solution.longestPalindrome("racecar"));
    }

    @Test
    void evenLengthPalindrome() {
        assertEquals("abba", solution.longestPalindrome("xabbayz"));
    }

    @Test
    void allSameChars() {
        assertEquals("aaaa", solution.longestPalindrome("aaaa"));
    }

    @Test
    void noPalindromeLongerThanOne() {
        // "abcd" -> any single character, length 1
        String result = solution.longestPalindrome("abcd");
        assertEquals(1, result.length());
    }
}
