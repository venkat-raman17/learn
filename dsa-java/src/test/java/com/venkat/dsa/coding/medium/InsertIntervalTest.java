package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class InsertIntervalTest {

    private final InsertInterval solution = new InsertInterval();

    @Test
    void testInsertWithMerge() {
        int[][] intervals = {{1, 3}, {6, 9}};
        int[] newInterval = {2, 5};
        int[][] expected = {{1, 5}, {6, 9}};
        assertArrayEquals(expected, solution.insert(intervals, newInterval));
    }

    @Test
    void testInsertWithMultipleMerges() {
        int[][] intervals = {{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}};
        int[] newInterval = {4, 8};
        int[][] expected = {{1, 2}, {3, 10}, {12, 16}};
        assertArrayEquals(expected, solution.insert(intervals, newInterval));
    }

    @Test
    void testInsertNoOverlap() {
        int[][] intervals = {{1, 5}};
        int[] newInterval = {6, 8};
        int[][] expected = {{1, 5}, {6, 8}};
        assertArrayEquals(expected, solution.insert(intervals, newInterval));
    }
}
