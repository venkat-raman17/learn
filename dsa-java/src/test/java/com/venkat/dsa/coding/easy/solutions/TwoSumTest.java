package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TwoSumTest {

    private final TwoSum sol = new TwoSum();

    // LeetCode example 1: [2,7,11,15], target=9 -> [0,1]
    @Test
    void example1() {
        assertArrayEquals(new int[]{0, 1}, sol.twoSum(new int[]{2, 7, 11, 15}, 9));
    }

    // LeetCode example 2: [3,2,4], target=6 -> [1,2]
    @Test
    void example2() {
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{3, 2, 4}, 6));
    }

    // LeetCode example 3: [3,3], target=6 -> [0,1]
    @Test
    void example3_duplicateValues() {
        assertArrayEquals(new int[]{0, 1}, sol.twoSum(new int[]{3, 3}, 6));
    }

    // Negative numbers: [-1,-2,-3,-4,-5], target=-8 -> [2,4]  (-3 + -5 = -8)
    @Test
    void negativeNumbers() {
        assertArrayEquals(new int[]{2, 4}, sol.twoSum(new int[]{-1, -2, -3, -4, -5}, -8));
    }

    // Zero involved: [0,4,3,0], target=0 -> [0,3]
    @Test
    void zeroTarget() {
        assertArrayEquals(new int[]{0, 3}, sol.twoSum(new int[]{0, 4, 3, 0}, 0));
    }

    // Answer at end of array: [1,2,3,4,5], target=9 -> [3,4]  (4+5=9)
    @Test
    void answerAtEnd() {
        assertArrayEquals(new int[]{3, 4}, sol.twoSum(new int[]{1, 2, 3, 4, 5}, 9));
    }

    // Large negative + large positive: [-3,5], target=2 -> [0,1]
    @Test
    void mixedSigns() {
        assertArrayEquals(new int[]{0, 1}, sol.twoSum(new int[]{-3, 5}, 2));
    }
}
