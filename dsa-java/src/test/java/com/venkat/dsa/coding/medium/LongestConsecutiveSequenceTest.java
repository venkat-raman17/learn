package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class LongestConsecutiveSequenceTest {

    private final LongestConsecutiveSequence solution = new LongestConsecutiveSequence();

    @Test
    public void testBasicCase() {
        assertEquals(4, solution.longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));
    }

    @Test
    public void testLongerSequence() {
        assertEquals(9, solution.longestConsecutive(new int[]{0, 3, 7, 2, 5, 8, 4, 6, 0, 1}));
    }

    @Test
    public void testEmptyArray() {
        assertEquals(0, solution.longestConsecutive(new int[]{}));
    }
}
