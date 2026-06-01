package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PermutationInStringTest {

    private final PermutationInString solution = new PermutationInString();

    // Official LeetCode examples
    @Test
    void example1_ab_in_eidbaooo() {
        // "ba" is a permutation of "ab" and is a substring of "eidbaooo"
        assertTrue(solution.checkInclusion("ab", "eidbaooo"));
    }

    @Test
    void example2_ab_in_eidboaoo() {
        // No permutation of "ab" exists as a contiguous substring
        assertFalse(solution.checkInclusion("ab", "eidboaoo"));
    }

    // Edge cases
    @Test
    void s1LongerThanS2() {
        assertFalse(solution.checkInclusion("hello", "hi"));
    }

    @Test
    void equalLengthMatch() {
        // s1 itself is in s2
        assertTrue(solution.checkInclusion("abc", "bca"));
    }

    @Test
    void equalLengthNoMatch() {
        assertFalse(solution.checkInclusion("abc", "def"));
    }

    @Test
    void singleCharMatch() {
        assertTrue(solution.checkInclusion("a", "a"));
    }

    @Test
    void singleCharNoMatch() {
        assertFalse(solution.checkInclusion("a", "b"));
    }

    @Test
    void permAtStart() {
        // "ba" at start of "bacd"
        assertTrue(solution.checkInclusion("ab", "bacd"));
    }

    @Test
    void permAtEnd() {
        // "cba" at end of "xyzabc" → "abc" is permutation of "abc"; answer true
        assertTrue(solution.checkInclusion("abc", "xyzabc"));
    }

    @Test
    void allSameChars() {
        // "aaa" in "aaabbb" — first window "aaa" matches
        assertTrue(solution.checkInclusion("aaa", "aaabbb"));
    }

    @Test
    void duplicateCharsNotEnough() {
        // Need 3 a's but s2 only has 2
        assertFalse(solution.checkInclusion("aaa", "aabbb"));
    }

    @Test
    void windowSlidesCorrectly() {
        // "abc" → permutation "cab" starting at index 1 of "dcab"
        assertTrue(solution.checkInclusion("abc", "dcab"));
    }
}
