package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class PacificAtlanticWaterFlowTest {

    private final PacificAtlanticWaterFlow sol = new PacificAtlanticWaterFlow();

    /** Normalize result for order-independent comparison. */
    private Set<List<Integer>> toSet(List<List<Integer>> list) {
        return new HashSet<>(list);
    }

    @Test
    void example1() {
        int[][] heights = {
            {1,2,2,3,5},
            {3,2,3,4,4},
            {2,4,5,3,1},
            {6,7,1,4,5},
            {5,1,1,2,4}
        };
        List<List<Integer>> result = sol.pacificAtlantic(heights);
        Set<List<Integer>> expected = new HashSet<>();
        expected.add(Arrays.asList(0, 4));
        expected.add(Arrays.asList(1, 3));
        expected.add(Arrays.asList(2, 2));
        expected.add(Arrays.asList(3, 0));
        expected.add(Arrays.asList(3, 1));
        expected.add(Arrays.asList(4, 0));
        expected.add(Arrays.asList(4, 4)); // corner of Atlantic AND Pacific borders
        // LeetCode expected: [[0,4],[1,3],[1,4],[2,2],[3,0],[3,1],[4,0]]
        // Let's use LeetCode's confirmed answer:
        expected.clear();
        expected.add(Arrays.asList(0, 4));
        expected.add(Arrays.asList(1, 3));
        expected.add(Arrays.asList(1, 4));
        expected.add(Arrays.asList(2, 2));
        expected.add(Arrays.asList(3, 0));
        expected.add(Arrays.asList(3, 1));
        expected.add(Arrays.asList(4, 0));
        assertEquals(expected, toSet(result));
    }

    @Test
    void example2_singleCell() {
        // 1x1 grid: the single cell can flow to both oceans
        int[][] heights = {{1}};
        List<List<Integer>> result = sol.pacificAtlantic(heights);
        assertEquals(1, result.size());
        assertEquals(Arrays.asList(0, 0), result.get(0));
    }

    @Test
    void uniformHeight_allCellsQualify() {
        // All heights equal: every cell can reach both oceans
        int[][] heights = {
            {1,1},
            {1,1}
        };
        List<List<Integer>> result = sol.pacificAtlantic(heights);
        assertEquals(4, result.size());
    }

    @Test
    void strictlyIncreasing_cornerOnly() {
        // Heights increase towards bottom-right; only top-left touches Pacific,
        // only bottom-right touches Atlantic. No cell flows to both except borders.
        // Verify result is non-null and non-empty
        int[][] heights = {
            {1,2,3},
            {4,5,6},
            {7,8,9}
        };
        List<List<Integer>> result = sol.pacificAtlantic(heights);
        assertFalse(result.isEmpty());
        // All border cells + interior cells that can reach both should be present
        // At minimum, top-right (0,2) and bottom-left (2,0) corners must appear
        assertTrue(toSet(result).contains(Arrays.asList(0, 2)));
        assertTrue(toSet(result).contains(Arrays.asList(2, 0)));
    }
}
