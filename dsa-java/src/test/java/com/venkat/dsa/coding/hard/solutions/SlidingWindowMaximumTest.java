package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlidingWindowMaximumTest {

    private final SlidingWindowMaximum solution = new SlidingWindowMaximum();

    // Official LeetCode examples
    @Test
    void example1_standard() {
        // nums=[1,3,-1,-3,5,3,6,7], k=3
        // windows: [1,3,-1]→3, [3,-1,-3]→3, [-1,-3,5]→5, [-3,5,3]→5, [5,3,6]→6, [3,6,7]→7
        assertArrayEquals(new int[]{3, 3, 5, 5, 6, 7},
                solution.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
    }

    @Test
    void example2_singleElement() {
        assertArrayEquals(new int[]{1}, solution.maxSlidingWindow(new int[]{1}, 1));
    }

    // Edge cases
    @Test
    void k1_eachElementIsItsOwnMax() {
        assertArrayEquals(new int[]{1, 3, 2, 5},
                solution.maxSlidingWindow(new int[]{1, 3, 2, 5}, 1));
    }

    @Test
    void kEqualsLength_singleWindowMax() {
        // Window covers entire array → max of {4,2,7,1,3} = 7
        assertArrayEquals(new int[]{7},
                solution.maxSlidingWindow(new int[]{4, 2, 7, 1, 3}, 5));
    }

    @Test
    void decreasingArray() {
        // [5,4,3,2,1] k=3 → [5,4,3]→5, [4,3,2]→4, [3,2,1]→3
        assertArrayEquals(new int[]{5, 4, 3},
                solution.maxSlidingWindow(new int[]{5, 4, 3, 2, 1}, 3));
    }

    @Test
    void increasingArray() {
        // [1,2,3,4,5] k=3 → [1,2,3]→3, [2,3,4]→4, [3,4,5]→5
        assertArrayEquals(new int[]{3, 4, 5},
                solution.maxSlidingWindow(new int[]{1, 2, 3, 4, 5}, 3));
    }

    @Test
    void allSameValues() {
        assertArrayEquals(new int[]{4, 4, 4},
                solution.maxSlidingWindow(new int[]{4, 4, 4, 4, 4}, 3));
    }

    @Test
    void negativeNumbers() {
        // [-5,-3,-1,-4,-2] k=2 → [-5,-3]→-3, [-3,-1]→-1, [-1,-4]→-1, [-4,-2]→-2
        assertArrayEquals(new int[]{-3, -1, -1, -2},
                solution.maxSlidingWindow(new int[]{-5, -3, -1, -4, -2}, 2));
    }

    @Test
    void maxAtFrontOfWindow() {
        // [9,1,1,1,1] k=3 → [9,1,1]→9, [1,1,1]→1, [1,1,1]→1
        assertArrayEquals(new int[]{9, 1, 1},
                solution.maxSlidingWindow(new int[]{9, 1, 1, 1, 1}, 3));
    }

    @Test
    void twoElements_k2() {
        assertArrayEquals(new int[]{5},
                solution.maxSlidingWindow(new int[]{3, 5}, 2));
    }
}
