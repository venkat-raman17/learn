package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinCostClimbingStairsTest {

    // Note: the solution mutates the input array, so each test must supply its own.

    private final MinCostClimbingStairs solution = new MinCostClimbingStairs();

    @Test
    void example1() {
        // cost=[10,15,20] -> min cost = 15 (start at index 1, pay 15, jump 2 to top)
        assertEquals(15, solution.minCostClimbingStairs(new int[]{10, 15, 20}));
    }

    @Test
    void example2() {
        // cost=[1,100,1,1,1,100,1,1,100,1] -> 6
        assertEquals(6, solution.minCostClimbingStairs(
                new int[]{1, 100, 1, 1, 1, 100, 1, 1, 100, 1}));
    }

    @Test
    void twoElements_pickMin() {
        // cost=[5,3] -> start at index 1 (cost 3), jump to top -> 3
        assertEquals(3, solution.minCostClimbingStairs(new int[]{5, 3}));
    }

    @Test
    void allZeros() {
        assertEquals(0, solution.minCostClimbingStairs(new int[]{0, 0, 0}));
    }

    @Test
    void singleHighAndLow() {
        // cost=[1,2,3,4] -> start at 0: 1+3=4, start at 1: 2+4=6 → optimal: pick 0 then skip by 2
        // path: 0->2->top: cost[0]+cost[2]=1+3=4; or 1->3->top: cost[1]+cost[3]=2+4=6
        // answer: 4
        assertEquals(4, solution.minCostClimbingStairs(new int[]{1, 2, 3, 4}));
    }
}
