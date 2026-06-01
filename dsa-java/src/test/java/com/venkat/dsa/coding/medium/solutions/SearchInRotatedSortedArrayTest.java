package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SearchInRotatedSortedArrayTest {

    private final SearchInRotatedSortedArray sol = new SearchInRotatedSortedArray();

    // --- Official LeetCode examples ---

    @Test
    void example1_targetFound() {
        // [4,5,6,7,0,1,2], target=0 => 4
        assertEquals(4, sol.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0));
    }

    @Test
    void example2_targetNotFound() {
        // [4,5,6,7,0,1,2], target=3 => -1
        assertEquals(-1, sol.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 3));
    }

    @Test
    void example3_singleElement_notFound() {
        assertEquals(-1, sol.search(new int[]{1}, 0));
    }

    // --- Edge cases ---

    @Test
    void singleElement_found() {
        assertEquals(0, sol.search(new int[]{5}, 5));
    }

    @Test
    void notRotated_found() {
        assertEquals(2, sol.search(new int[]{1, 2, 3, 4, 5}, 3));
    }

    @Test
    void targetAtFirst() {
        assertEquals(0, sol.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 4));
    }

    @Test
    void targetAtLast() {
        assertEquals(6, sol.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 2));
    }

    @Test
    void rotationAtEnd() {
        // [2,3,4,5,1], target=1 => 4
        assertEquals(4, sol.search(new int[]{2, 3, 4, 5, 1}, 1));
    }

    @Test
    void rotationAtBeginning() {
        // [1,3,5] (no rotation), target=3 => 1
        assertEquals(1, sol.search(new int[]{1, 3, 5}, 3));
    }

    @Test
    void twoElements_rotated() {
        // [2,1], target=1 => 1
        assertEquals(1, sol.search(new int[]{2, 1}, 1));
        // target=2 => 0
        assertEquals(0, sol.search(new int[]{2, 1}, 2));
    }

    @Test
    void negativeNumbers() {
        // [-5,-3,-1,-4,-2], sorted: -5,-4,-3,-2,-1 rotated 3: [-1,-4,-2,-5,-3]?
        // Let's use a clean example: [-3,-1,0,-5,-4] — not valid ascending.
        // Use: [0,1,2,-2,-1] sorted: -2,-1,0,1,2 rotated 3 => [-2,-1,0,1,2] rotated by 3 => [0,1,2,-2,-1]
        assertEquals(2, sol.search(new int[]{0, 1, 2, -2, -1}, 2));
        assertEquals(4, sol.search(new int[]{0, 1, 2, -2, -1}, -1));
        assertEquals(-1, sol.search(new int[]{0, 1, 2, -2, -1}, 5));
    }
}
