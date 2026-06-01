package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MaximumSubarrayTest {

    private final MaximumSubarray solution = new MaximumSubarray();

    @Test
    void example1_mixedArray() {
        // [-2,1,-3,4,-1,2,1,-5,4] -> subarray [4,-1,2,1] = 6
        assertEquals(6, solution.maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
    }

    @Test
    void example2_singleElement() {
        assertEquals(1, solution.maxSubArray(new int[]{1}));
    }

    @Test
    void example3_allPositive() {
        // entire array is the answer: 5+4+3 = 12 (wait: [5,4,-1,7,8] = 23)
        assertEquals(23, solution.maxSubArray(new int[]{5, 4, -1, 7, 8}));
    }

    @Test
    void allNegative() {
        // Must pick at least one element; answer is -1 (the largest single element)
        assertEquals(-1, solution.maxSubArray(new int[]{-3, -1, -2}));
    }

    @Test
    void singleNegative() {
        assertEquals(-5, solution.maxSubArray(new int[]{-5}));
    }

    @Test
    void twoElementsPickBetter() {
        assertEquals(3, solution.maxSubArray(new int[]{-1, 3}));
    }

    @Test
    void allSamePositive() {
        assertEquals(9, solution.maxSubArray(new int[]{3, 3, 3}));
    }

    @Test
    void largeNegativeAtStart() {
        // -100 then positives: sum of positives = 6
        assertEquals(6, solution.maxSubArray(new int[]{-100, 1, 2, 3}));
    }

    @Test
    void zeroInArray() {
        assertEquals(3, solution.maxSubArray(new int[]{0, -1, 3, -2, 0}));
    }
}
