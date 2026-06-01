package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NonOverlappingIntervalsTest {

    private final NonOverlappingIntervals sol = new NonOverlappingIntervals();

    @Test
    void example1() {
        // [[1,2],[2,3],[3,4],[1,3]] -> 1 (remove [1,3])
        assertEquals(1, sol.eraseOverlapIntervals(new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 3}}));
    }

    @Test
    void example2() {
        // [[1,2],[1,2],[1,2]] -> 2 (keep one, remove two)
        assertEquals(2, sol.eraseOverlapIntervals(new int[][]{{1, 2}, {1, 2}, {1, 2}}));
    }

    @Test
    void example3() {
        // [[1,2],[2,3]] -> 0 (adjacent, no overlap)
        assertEquals(0, sol.eraseOverlapIntervals(new int[][]{{1, 2}, {2, 3}}));
    }

    @Test
    void single() {
        assertEquals(0, sol.eraseOverlapIntervals(new int[][]{{0, 5}}));
    }

    @Test
    void allOverlap() {
        // [[0,10],[0,5],[0,3]] sorted by end: [0,3],[0,5],[0,10] -> keep [0,3], remove 2
        assertEquals(2, sol.eraseOverlapIntervals(new int[][]{{0, 10}, {0, 5}, {0, 3}}));
    }

    @Test
    void noOverlap() {
        assertEquals(0, sol.eraseOverlapIntervals(new int[][]{{1, 2}, {3, 4}, {5, 6}}));
    }

    @Test
    void largerExample() {
        // [[1,100],[11,22],[1,11],[2,12]] -> keep [1,11],[11,22] = 2 kept, remove 2
        assertEquals(2, sol.eraseOverlapIntervals(new int[][]{{1, 100}, {11, 22}, {1, 11}, {2, 12}}));
    }
}
