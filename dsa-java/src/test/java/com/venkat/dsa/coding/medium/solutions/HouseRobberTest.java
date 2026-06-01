package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HouseRobberTest {

    private final HouseRobber solution = new HouseRobber();

    @Test
    void example1() {
        // [1,2,3,1] -> rob house 0 (1) + house 2 (3) = 4
        assertEquals(4, solution.rob(new int[]{1, 2, 3, 1}));
    }

    @Test
    void example2() {
        // [2,7,9,3,1] -> rob house 0 (2) + house 2 (9) + house 4 (1) = 12
        assertEquals(12, solution.rob(new int[]{2, 7, 9, 3, 1}));
    }

    @Test
    void singleHouse() {
        assertEquals(5, solution.rob(new int[]{5}));
    }

    @Test
    void twoHouses_pickLarger() {
        assertEquals(10, solution.rob(new int[]{3, 10}));
    }

    @Test
    void allSameValue() {
        // [4,4,4,4] -> rob index 0 and 2 -> 8
        assertEquals(8, solution.rob(new int[]{4, 4, 4, 4}));
    }

    @Test
    void increasingValues() {
        // [1,2,3] -> max(rob 0+2=4, rob 1=2) = 4
        assertEquals(4, solution.rob(new int[]{1, 2, 3}));
    }
}
