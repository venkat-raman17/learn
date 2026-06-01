package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LongestIncreasingSubsequenceTest {

    private final LongestIncreasingSubsequence solution = new LongestIncreasingSubsequence();

    @Test
    void example1() {
        // [10,9,2,5,3,7,101,18] -> [2,3,7,18] or [2,5,7,101] etc. -> length 4
        assertEquals(4, solution.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
    }

    @Test
    void example2() {
        // [0,1,0,3,2,3] -> [0,1,2,3] -> length 4
        assertEquals(4, solution.lengthOfLIS(new int[]{0, 1, 0, 3, 2, 3}));
    }

    @Test
    void example3_allSame() {
        // [7,7,7,7,7] -> LIS length 1 (strictly increasing)
        assertEquals(1, solution.lengthOfLIS(new int[]{7, 7, 7, 7, 7}));
    }

    @Test
    void singleElement() {
        assertEquals(1, solution.lengthOfLIS(new int[]{42}));
    }

    @Test
    void alreadySorted() {
        // [1,2,3,4,5] -> length 5
        assertEquals(5, solution.lengthOfLIS(new int[]{1, 2, 3, 4, 5}));
    }

    @Test
    void reverselySorted() {
        // [5,4,3,2,1] -> length 1
        assertEquals(1, solution.lengthOfLIS(new int[]{5, 4, 3, 2, 1}));
    }

    @Test
    void withNegatives() {
        // [-3,-2,-1,0,1] -> length 5
        assertEquals(5, solution.lengthOfLIS(new int[]{-3, -2, -1, 0, 1}));
    }
}
