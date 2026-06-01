package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTest {

    private final BinarySearch sol = new BinarySearch();

    // --- Official LeetCode examples ---

    @Test
    void example1_targetFound() {
        // nums=[-1,0,3,5,9,12], target=9 => index 4
        int[] nums = {-1, 0, 3, 5, 9, 12};
        assertEquals(4, sol.search(nums, 9));
    }

    @Test
    void example2_targetNotPresent() {
        // nums=[-1,0,3,5,9,12], target=2 => -1
        int[] nums = {-1, 0, 3, 5, 9, 12};
        assertEquals(-1, sol.search(nums, 2));
    }

    // --- Edge cases ---

    @Test
    void singleElementFound() {
        assertEquals(0, sol.search(new int[]{5}, 5));
    }

    @Test
    void singleElementNotFound() {
        assertEquals(-1, sol.search(new int[]{5}, 3));
    }

    @Test
    void targetAtFirstIndex() {
        int[] nums = {1, 2, 3, 4, 5};
        assertEquals(0, sol.search(nums, 1));
    }

    @Test
    void targetAtLastIndex() {
        int[] nums = {1, 2, 3, 4, 5};
        assertEquals(4, sol.search(nums, 5));
    }

    @Test
    void negativeNumbers() {
        int[] nums = {-10, -5, 0, 5, 10};
        assertEquals(1, sol.search(nums, -5));
    }

    @Test
    void twoElementsFoundFirst() {
        assertEquals(0, sol.search(new int[]{3, 7}, 3));
    }

    @Test
    void twoElementsFoundSecond() {
        assertEquals(1, sol.search(new int[]{3, 7}, 7));
    }

    @Test
    void twoElementsNotFound() {
        assertEquals(-1, sol.search(new int[]{3, 7}, 5));
    }
}
