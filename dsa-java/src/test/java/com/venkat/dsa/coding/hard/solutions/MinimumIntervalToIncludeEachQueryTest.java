package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MinimumIntervalToIncludeEachQueryTest {

    private final MinimumIntervalToIncludeEachQuery sol = new MinimumIntervalToIncludeEachQuery();

    @Test
    void example1() {
        // intervals=[[1,4],[2,4],[3,6],[4,4]], queries=[2,3,4,5]
        // query 2: intervals covering 2 are [1,4](size4),[2,4](size3) -> min=3
        // query 3: [1,4](4),[2,4](3),[3,6](4) -> min=3
        // query 4: [1,4](4),[2,4](3),[3,6](4),[4,4](1) -> min=1
        // query 5: [3,6](4) -> 4
        int[] result = sol.minInterval(
                new int[][]{{1, 4}, {2, 4}, {3, 6}, {4, 4}},
                new int[]{2, 3, 4, 5});
        assertArrayEquals(new int[]{3, 3, 1, 4}, result);
    }

    @Test
    void example2() {
        // intervals=[[2,3],[2,5],[1,8],[20,25]], queries=[2,19,5,22]
        // query 2: [2,3](2),[2,5](4),[1,8](8) -> min=2
        // query 19: [1,8](8) nope (8<19 stale), [20,25] nope (20>19) -> -1
        // query 5: [2,5](4),[1,8](8) -> min=4
        // query 22: [20,25](6) -> 6
        int[] result = sol.minInterval(
                new int[][]{{2, 3}, {2, 5}, {1, 8}, {20, 25}},
                new int[]{2, 19, 5, 22});
        assertArrayEquals(new int[]{2, -1, 4, 6}, result);
    }

    @Test
    void singleIntervalCoversAll() {
        // interval [1,10], queries [1,5,10] all covered by size=10
        int[] result = sol.minInterval(new int[][]{{1, 10}}, new int[]{1, 5, 10});
        assertArrayEquals(new int[]{10, 10, 10}, result);
    }

    @Test
    void queryBeforeAllIntervals() {
        int[] result = sol.minInterval(new int[][]{{5, 10}}, new int[]{1});
        assertArrayEquals(new int[]{-1}, result);
    }

    @Test
    void queryAfterAllIntervals() {
        int[] result = sol.minInterval(new int[][]{{1, 3}}, new int[]{5});
        assertArrayEquals(new int[]{-1}, result);
    }

    @Test
    void multipleIntervalsPreferSmallest() {
        // intervals [1,10](10) and [3,4](2); query=3 should pick size 2
        int[] result = sol.minInterval(new int[][]{{1, 10}, {3, 4}}, new int[]{3});
        assertArrayEquals(new int[]{2}, result);
    }

    @Test
    void exactBoundaryQuery() {
        // interval [5,5] (size 1), query=5
        int[] result = sol.minInterval(new int[][]{{5, 5}}, new int[]{5});
        assertArrayEquals(new int[]{1}, result);
    }

    @Test
    void unsortedQueriesReturnCorrectOrder() {
        // queries in descending order — result must map back to original positions
        // intervals [[1,4],[5,8]], queries [7,2]
        // query 7 -> [5,8](4), query 2 -> [1,4](4)
        int[] result = sol.minInterval(new int[][]{{1, 4}, {5, 8}}, new int[]{7, 2});
        assertArrayEquals(new int[]{4, 4}, result);
    }
}
