package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductOfArrayExceptSelfTest {

    private final ProductOfArrayExceptSelf sol = new ProductOfArrayExceptSelf();

    // LeetCode example 1: [1,2,3,4] -> [24,12,8,6]
    // index 0: 2*3*4=24; index 1: 1*3*4=12; index 2: 1*2*4=8; index 3: 1*2*3=6
    @Test
    void example1() {
        assertArrayEquals(new int[]{24, 12, 8, 6},
                sol.productExceptSelf(new int[]{1, 2, 3, 4}));
    }

    // LeetCode example 2: [-1,1,0,-3,3] -> [0,0,9,0,0]
    // index 2 (the zero): product of [-1,1,-3,3] = 9; all others contain the zero so = 0
    @Test
    void example2_containsZero() {
        assertArrayEquals(new int[]{0, 0, 9, 0, 0},
                sol.productExceptSelf(new int[]{-1, 1, 0, -3, 3}));
    }

    // Two-element array: [a,b] -> [b,a]
    @Test
    void twoElements() {
        assertArrayEquals(new int[]{5, 3},
                sol.productExceptSelf(new int[]{3, 5}));
    }

    // All ones: product except self is always 1
    @Test
    void allOnes() {
        assertArrayEquals(new int[]{1, 1, 1, 1},
                sol.productExceptSelf(new int[]{1, 1, 1, 1}));
    }

    // Contains two zeros: every product is 0
    @Test
    void twoZeros() {
        assertArrayEquals(new int[]{0, 0, 0, 0},
                sol.productExceptSelf(new int[]{0, 0, 1, 2}));
    }

    // Negative numbers: [-1,-2,-3] -> [6,-3,2]
    // index 0: (-2)*(-3)=6; index 1: (-1)*(-3)=3; index 2: (-1)*(-2)=2
    @Test
    void negativeNumbers() {
        assertArrayEquals(new int[]{6, 3, 2},
                sol.productExceptSelf(new int[]{-1, -2, -3}));
    }

    // Single zero in array: [2,0,4] -> [0,8,0]
    @Test
    void singleZeroInMiddle() {
        assertArrayEquals(new int[]{0, 8, 0},
                sol.productExceptSelf(new int[]{2, 0, 4}));
    }
}
