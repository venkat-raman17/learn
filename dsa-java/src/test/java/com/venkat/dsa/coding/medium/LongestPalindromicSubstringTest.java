package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class LongestPalindromicSubstringTest {

    private final LongestPalindromicSubstring solution = new LongestPalindromicSubstring();

    @Test
    public void testBabad() {
        String result = solution.longestPalindrome("babad");
        assertTrue(result.equals("bab") || result.equals("aba"),
                "Expected 'bab' or 'aba' but got: " + result);
    }

    @Test
    public void testCbbd() {
        assertEquals("bb", solution.longestPalindrome("cbbd"));
    }

    @Test
    public void testSingleChar() {
        assertEquals("a", solution.longestPalindrome("a"));
    }
}
