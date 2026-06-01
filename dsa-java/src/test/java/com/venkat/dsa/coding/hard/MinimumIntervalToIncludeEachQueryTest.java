package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class MinimumIntervalToIncludeEachQueryTest {

    private final MinimumIntervalToIncludeEachQuery solution = new MinimumIntervalToIncludeEachQuery();

    @Test
    void testExample1() {
        int[][] intervals = {{1, 4}, {2, 4}, {3, 6}, {4, 4}};
        int[] queries = {2, 3, 4, 5};
        int[] expected = {3, 3, 1, 4};
        assertArrayEquals(expected, solution.minInterval(intervals, queries));
    }

    @Test
    void testExample2() {
        int[][] intervals = {{2, 3}, {2, 5}, {1, 8}, {20, 25}};
        int[] queries = {2, 19, 5, 22};
        int[] expected = {2, -1, 4, 6};
        assertArrayEquals(expected, solution.minInterval(intervals, queries));
    }

    @Test
    void testNoContainingInterval() {
        int[][] intervals = {{5, 10}};
        int[] queries = {1, 3};
        int[] expected = {-1, -1};
        assertArrayEquals(expected, solution.minInterval(intervals, queries));
    }
}
