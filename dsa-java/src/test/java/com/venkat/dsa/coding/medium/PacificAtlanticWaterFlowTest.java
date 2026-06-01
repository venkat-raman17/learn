package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Disabled("practice — delete when you start")
public class PacificAtlanticWaterFlowTest {

    private final PacificAtlanticWaterFlow solution = new PacificAtlanticWaterFlow();

    @Test
    public void testExample1() {
        int[][] heights = {
            {1,2,2,3,5},
            {3,2,3,4,4},
            {2,4,5,3,1},
            {6,7,1,4,5},
            {5,1,1,2,4}
        };
        List<List<Integer>> result = solution.pacificAtlantic(heights);
        assertNotNull(result);
        assertEquals(7, result.size());
    }

    @Test
    public void testSingleCell() {
        int[][] heights = {{1}};
        List<List<Integer>> result = solution.pacificAtlantic(heights);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Arrays.asList(0, 0), result.get(0));
    }

    @Test
    public void testFlatGrid() {
        int[][] heights = {{1,1},{1,1}};
        List<List<Integer>> result = solution.pacificAtlantic(heights);
        assertNotNull(result);
        assertEquals(4, result.size());
    }
}
