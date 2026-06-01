package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TwoSumIIInputArrayIsSortedTest {

    private final TwoSumIIInputArrayIsSorted sol = new TwoSumIIInputArrayIsSorted();

    // Official LeetCode example 1: [2,7,11,15], target=9 -> [1,2]
    @Test
    void officialExample1() {
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{2, 7, 11, 15}, 9));
    }

    // Official LeetCode example 2: [2,3,4], target=6 -> [1,3]
    @Test
    void officialExample2() {
        assertArrayEquals(new int[]{1, 3}, sol.twoSum(new int[]{2, 3, 4}, 6));
    }

    // Official LeetCode example 3: [-1,0], target=-1 -> [1,2]
    @Test
    void officialExample3() {
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{-1, 0}, -1));
    }

    @Test
    void solutionAtMiddle() {
        // [1,2,3,4,5], target=7: two-pointer starts left=0(1),right=4(5) sum=6<7 -> left++;
        // then left=1(2),right=4(5) sum=7==7 -> return 1-indexed [2,5]
        assertArrayEquals(new int[]{2, 5}, sol.twoSum(new int[]{1, 2, 3, 4, 5}, 7));
    }

    @Test
    void negativeNumbers() {
        // [-5,-3,-1,0], target=-8 -> [-5,-3] -> [1,2]
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{-5, -3, -1, 0}, -8));
    }

    @Test
    void duplicateValues() {
        // [3,3], target=6 -> [1,2]
        assertArrayEquals(new int[]{1, 2}, sol.twoSum(new int[]{3, 3}, 6));
    }

    @Test
    void largeArray() {
        int[] arr = new int[1000];
        for (int i = 0; i < 1000; i++) arr[i] = i + 1; // [1..1000]
        // 1 + 1000 = 1001 -> [1, 1000]
        assertArrayEquals(new int[]{1, 1000}, sol.twoSum(arr, 1001));
    }
}
