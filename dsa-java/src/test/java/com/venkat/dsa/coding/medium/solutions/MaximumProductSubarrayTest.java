package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaximumProductSubarrayTest {

    private final MaximumProductSubarray solution = new MaximumProductSubarray();

    @Test
    void example1() {
        // [2,3,-2,4] -> subarray [2,3] has product 6
        assertEquals(6, solution.maxProduct(new int[]{2, 3, -2, 4}));
    }

    @Test
    void example2() {
        // [-2,0,-1] -> 0 (the subarray [0] gives 0, which is the max)
        assertEquals(0, solution.maxProduct(new int[]{-2, 0, -1}));
    }

    @Test
    void singleElement_positive() {
        assertEquals(3, solution.maxProduct(new int[]{3}));
    }

    @Test
    void singleElement_negative() {
        assertEquals(-2, solution.maxProduct(new int[]{-2}));
    }

    @Test
    void twoNegatives_productPositive() {
        // [-3,-2] -> product 6
        assertEquals(6, solution.maxProduct(new int[]{-3, -2}));
    }

    @Test
    void containsZero_isolatesSubarrays() {
        // [3,-1,4,0,2] -> max is 4 (just [4]) or 2 ([2]) -> 4
        assertEquals(4, solution.maxProduct(new int[]{3, -1, 4, 0, 2}));
    }

    @Test
    void allNegatives_oddCount() {
        // [-2,-3,-4] -> best subarray is [-2,-3]=6 or [-3,-4]=12 -> 12
        assertEquals(12, solution.maxProduct(new int[]{-2, -3, -4}));
    }

    @Test
    void allNegatives_evenCount() {
        // [-1,-2,-3,-4] -> entire array = 24
        assertEquals(24, solution.maxProduct(new int[]{-1, -2, -3, -4}));
    }
}
