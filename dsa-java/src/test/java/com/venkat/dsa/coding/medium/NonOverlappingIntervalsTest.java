package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class NonOverlappingIntervalsTest {

    private final NonOverlappingIntervals solution = new NonOverlappingIntervals();

    @Test
    void testRemoveOneInterval() {
        int[][] intervals = {{1, 2}, {2, 3}, {3, 4}, {1, 3}};
        assertEquals(1, solution.eraseOverlapIntervals(intervals));
    }

    @Test
    void testRemoveTwoIdenticalIntervals() {
        int[][] intervals = {{1, 2}, {1, 2}, {1, 2}};
        assertEquals(2, solution.eraseOverlapIntervals(intervals));
    }

    @Test
    void testNoRemovalNeeded() {
        int[][] intervals = {{1, 2}, {2, 3}};
        assertEquals(0, solution.eraseOverlapIntervals(intervals));
    }
}
