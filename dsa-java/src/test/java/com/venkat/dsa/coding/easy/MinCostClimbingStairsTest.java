package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class MinCostClimbingStairsTest {

    private final MinCostClimbingStairs solution = new MinCostClimbingStairs();

    @Test
    public void testThreeSteps() {
        assertEquals(15, solution.minCostClimbingStairs(new int[]{10, 15, 20}));
    }

    @Test
    public void testLongerStaircase() {
        assertEquals(6, solution.minCostClimbingStairs(new int[]{1, 100, 1, 1, 1, 100, 1, 1, 100, 1}));
    }

    @Test
    public void testTwoSteps() {
        assertEquals(0, solution.minCostClimbingStairs(new int[]{0, 0}));
    }
}
