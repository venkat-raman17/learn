package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FindMinimumInRotatedSortedArrayTest {

    private final FindMinimumInRotatedSortedArray sol = new FindMinimumInRotatedSortedArray();

    // --- Official LeetCode examples ---

    @Test
    void example1() {
        // [3,4,5,1,2] => 1
        assertEquals(1, sol.findMin(new int[]{3, 4, 5, 1, 2}));
    }

    @Test
    void example2() {
        // [4,5,6,7,0,1,2] => 0
        assertEquals(0, sol.findMin(new int[]{4, 5, 6, 7, 0, 1, 2}));
    }

    @Test
    void example3() {
        // [11,13,15,17] (not rotated) => 11
        assertEquals(11, sol.findMin(new int[]{11, 13, 15, 17}));
    }

    // --- Edge cases ---

    @Test
    void singleElement() {
        assertEquals(5, sol.findMin(new int[]{5}));
    }

    @Test
    void twoElements_notRotated() {
        assertEquals(1, sol.findMin(new int[]{1, 2}));
    }

    @Test
    void twoElements_rotated() {
        assertEquals(1, sol.findMin(new int[]{2, 1}));
    }

    @Test
    void rotatedByOne() {
        // [2,3,4,5,1] => 1
        assertEquals(1, sol.findMin(new int[]{2, 3, 4, 5, 1}));
    }

    @Test
    void minAtMiddle() {
        // [3,4,1,2] => 1
        assertEquals(1, sol.findMin(new int[]{3, 4, 1, 2}));
    }

    @Test
    void alreadySorted() {
        assertEquals(1, sol.findMin(new int[]{1, 2, 3, 4, 5}));
    }

    @Test
    void negativeNumbers() {
        // [-3,-2,-1,-5,-4] rotated: min is -5
        // sorted: -5,-4,-3,-2,-1 rotated 3: [-3,-2,-1,-5,-4]
        assertEquals(-5, sol.findMin(new int[]{-3, -2, -1, -5, -4}));
    }
}
