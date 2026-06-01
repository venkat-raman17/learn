package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LongestSubstringWithoutRepeatingCharactersTest {

    private final LongestSubstringWithoutRepeatingCharacters solution =
            new LongestSubstringWithoutRepeatingCharacters();

    // Official LeetCode examples
    @Test
    void example1_abcabcbb() {
        // "abc" = 3
        assertEquals(3, solution.lengthOfLongestSubstring("abcabcbb"));
    }

    @Test
    void example2_bbbbb() {
        // only single "b" = 1
        assertEquals(1, solution.lengthOfLongestSubstring("bbbbb"));
    }

    @Test
    void example3_pwwkew() {
        // "wke" = 3
        assertEquals(3, solution.lengthOfLongestSubstring("pwwkew"));
    }

    // Edge cases
    @Test
    void emptyString() {
        assertEquals(0, solution.lengthOfLongestSubstring(""));
    }

    @Test
    void singleChar() {
        assertEquals(1, solution.lengthOfLongestSubstring("a"));
    }

    @Test
    void allUnique() {
        assertEquals(4, solution.lengthOfLongestSubstring("abcd"));
    }

    @Test
    void duplicateNotInCurrentWindow() {
        // "dvdf": window "vdf" = 3; first 'd' is outside window when second 'd' arrives
        assertEquals(3, solution.lengthOfLongestSubstring("dvdf"));
    }

    @Test
    void spacesAndSpecialChars() {
        // " " (space) → length 1
        assertEquals(1, solution.lengthOfLongestSubstring(" "));
    }

    @Test
    void mixedWithSpaces() {
        // "a b" → "a b" (space counts), length 3
        assertEquals(3, solution.lengthOfLongestSubstring("a b"));
    }

    @Test
    void longNonRepeating() {
        assertEquals(26, solution.lengthOfLongestSubstring("abcdefghijklmnopqrstuvwxyz"));
    }

    @Test
    void repeatAtEnd() {
        // "aab" → "ab" = 2
        assertEquals(2, solution.lengthOfLongestSubstring("aab"));
    }
}
