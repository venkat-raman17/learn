package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class MergeIntervalsTest {

    private final MergeIntervals solution = new MergeIntervals();

    @Test
    void testMergeOverlapping() {
        int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] expected = {{1, 6}, {8, 10}, {15, 18}};
        assertArrayEquals(expected, solution.merge(intervals));
    }

    @Test
    void testMergeTouching() {
        int[][] intervals = {{1, 4}, {4, 5}};
        int[][] expected = {{1, 5}};
        assertArrayEquals(expected, solution.merge(intervals));
    }

    @Test
    void testMergeSingleInterval() {
        int[][] intervals = {{1, 4}};
        int[][] expected = {{1, 4}};
        assertArrayEquals(expected, solution.merge(intervals));
    }
}
