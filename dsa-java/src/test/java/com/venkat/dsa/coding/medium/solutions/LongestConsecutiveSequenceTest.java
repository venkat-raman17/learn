package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LongestConsecutiveSequenceTest {

    private final LongestConsecutiveSequence sol = new LongestConsecutiveSequence();

    // LeetCode example 1: [100,4,200,1,3,2] -> 4  (sequence 1,2,3,4)
    @Test
    void example1() {
        assertEquals(4, sol.longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));
    }

    // LeetCode example 2: [0,3,7,2,5,8,4,6,0,1] -> 9  (sequence 0-8)
    @Test
    void example2() {
        assertEquals(9, sol.longestConsecutive(new int[]{0, 3, 7, 2, 5, 8, 4, 6, 0, 1}));
    }

    // Empty array -> 0
    @Test
    void emptyArray() {
        assertEquals(0, sol.longestConsecutive(new int[]{}));
    }

    // Single element -> 1
    @Test
    void singleElement() {
        assertEquals(1, sol.longestConsecutive(new int[]{42}));
    }

    // All duplicates of same number -> 1
    @Test
    void allDuplicates() {
        assertEquals(1, sol.longestConsecutive(new int[]{5, 5, 5, 5}));
    }

    // Already sorted consecutive: [1,2,3,4,5] -> 5
    @Test
    void alreadyConsecutive() {
        assertEquals(5, sol.longestConsecutive(new int[]{1, 2, 3, 4, 5}));
    }

    // Negative numbers: [-5,-4,-3,0,1] -> 3  (sequence -5,-4,-3)
    @Test
    void negativeNumbers() {
        assertEquals(3, sol.longestConsecutive(new int[]{-5, -4, -3, 0, 1}));
    }

    // Two separate equal-length sequences: pick either length
    @Test
    void twoEqualSequences() {
        // [1,2,3] and [10,11,12] — both length 3
        assertEquals(3, sol.longestConsecutive(new int[]{1, 2, 3, 10, 11, 12}));
    }

    // Duplicates mixed in: [1,2,2,3] -> 3  (1,2,3)
    @Test
    void duplicatesInSequence() {
        assertEquals(3, sol.longestConsecutive(new int[]{1, 2, 2, 3}));
    }

    // Large gap between two single elements -> 1
    @Test
    void isolatedElements() {
        assertEquals(1, sol.longestConsecutive(new int[]{1, 1000}));
    }
}
