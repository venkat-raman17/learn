package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HouseRobberIITest {

    private final HouseRobberII solution = new HouseRobberII();

    @Test
    void example1() {
        // [2,3,2] -> cannot rob 0 and 2 (adjacent in circle); max = rob index 1 = 3
        assertEquals(3, solution.rob(new int[]{2, 3, 2}));
    }

    @Test
    void example2() {
        // [1,2,3,1] -> range[0..2]=4 (rob 0+2), range[1..3]=4 (rob 1+3) -> 4
        assertEquals(4, solution.rob(new int[]{1, 2, 3, 1}));
    }

    @Test
    void singleHouse() {
        assertEquals(1, solution.rob(new int[]{1}));
    }

    @Test
    void twoHouses() {
        assertEquals(2, solution.rob(new int[]{1, 2}));
    }

    @Test
    void allSame() {
        // [5,5,5,5,5] circular. range[0..3]: rob 0+2+? -> linear rob of [5,5,5,5] = 10
        // range[1..4]: same = 10
        assertEquals(10, solution.rob(new int[]{5, 5, 5, 5, 5}));
    }

    @Test
    void largerExample() {
        // [1,2,3,4,5,1,2,7,1,9,1,1] (12 elements, circular)
        // range[0..10]=[1,2,3,4,5,1,2,7,1,9,1] -> linear rob = 25 (traced by hand)
        // range[1..11]=[2,3,4,5,1,2,7,1,9,1,1] -> linear rob = 25
        // result = max(25,25) = 25
        assertEquals(25, solution.rob(new int[]{1, 2, 3, 4, 5, 1, 2, 7, 1, 9, 1, 1}));
    }
}
