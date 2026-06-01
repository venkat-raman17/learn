package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EditDistanceTest {

    private final EditDistance sol = new EditDistance();

    // LeetCode example 1: "horse" -> "ros" -> 3
    @Test
    void example1() {
        assertEquals(3, sol.minDistance("horse", "ros"));
    }

    // LeetCode example 2: "intention" -> "execution" -> 5
    @Test
    void example2() {
        assertEquals(5, sol.minDistance("intention", "execution"));
    }

    // Identical strings -> 0 operations
    @Test
    void identical() {
        assertEquals(0, sol.minDistance("abc", "abc"));
    }

    // word1 empty -> insert all chars of word2
    @Test
    void word1Empty() {
        assertEquals(3, sol.minDistance("", "abc"));
    }

    // word2 empty -> delete all chars of word1
    @Test
    void word2Empty() {
        assertEquals(3, sol.minDistance("abc", ""));
    }

    // Both empty -> 0
    @Test
    void bothEmpty() {
        assertEquals(0, sol.minDistance("", ""));
    }

    // Single char same -> 0
    @Test
    void singleSame() {
        assertEquals(0, sol.minDistance("a", "a"));
    }

    // Single char different -> 1 (replace)
    @Test
    void singleDiff() {
        assertEquals(1, sol.minDistance("a", "b"));
    }

    // "kitten" -> "sitting" = 3
    @Test
    void kittenSitting() {
        assertEquals(3, sol.minDistance("kitten", "sitting"));
    }
}
