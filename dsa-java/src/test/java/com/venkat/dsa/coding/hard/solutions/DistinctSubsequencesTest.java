package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DistinctSubsequencesTest {

    private final DistinctSubsequences sol = new DistinctSubsequences();

    // LeetCode example 1: s="rabbbit", t="rabbit" -> 3
    // Three 'b' positions to drop one 'b' from
    @Test
    void example1() {
        assertEquals(3, sol.numDistinct("rabbbit", "rabbit"));
    }

    // LeetCode example 2: s="babgbag", t="bag" -> 5
    @Test
    void example2() {
        assertEquals(5, sol.numDistinct("babgbag", "bag"));
    }

    // t empty -> 1 (empty subsequence always exists)
    @Test
    void tEmpty() {
        assertEquals(1, sol.numDistinct("abc", ""));
    }

    // s empty, t non-empty -> 0
    @Test
    void sEmpty() {
        assertEquals(0, sol.numDistinct("", "a"));
    }

    // Both empty -> 1
    @Test
    void bothEmpty() {
        assertEquals(1, sol.numDistinct("", ""));
    }

    // No match possible
    @Test
    void noMatch() {
        assertEquals(0, sol.numDistinct("abc", "d"));
    }

    // Exact match: "abc" in "abc" -> 1
    @Test
    void exactMatch() {
        assertEquals(1, sol.numDistinct("abc", "abc"));
    }

    // "aaa" in "aaaa" -> 4 (choose 3 of 4)
    @Test
    void repeatingChars() {
        assertEquals(4, sol.numDistinct("aaaa", "aaa"));
    }
}
