package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class RottingOrangesTest {

    private final RottingOranges solution = new RottingOranges();

    @Test
    public void testFourMinutes() {
        int[][] grid = {{2,1,1},{1,1,0},{0,1,1}};
        assertEquals(4, solution.orangesRotting(grid));
    }

    @Test
    public void testImpossible() {
        int[][] grid = {{2,1,1},{0,1,1},{1,0,1}};
        assertEquals(-1, solution.orangesRotting(grid));
    }

    @Test
    public void testAlreadyRottenOrEmpty() {
        int[][] grid = {{0,2}};
        assertEquals(0, solution.orangesRotting(grid));
    }
}
