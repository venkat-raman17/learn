package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContainsDuplicateTest {

    private final ContainsDuplicate sol = new ContainsDuplicate();

    // LeetCode example 1: [1,2,3,1] -> true (1 appears twice)
    @Test
    void example1_hasDuplicate() {
        assertTrue(sol.containsDuplicate(new int[]{1, 2, 3, 1}));
    }

    // LeetCode example 2: [1,2,3,4] -> false (all distinct)
    @Test
    void example2_allDistinct() {
        assertFalse(sol.containsDuplicate(new int[]{1, 2, 3, 4}));
    }

    // LeetCode example 3: [1,1,1,3,3,4,3,2,4,2] -> true
    @Test
    void example3_multipleDuplicates() {
        assertTrue(sol.containsDuplicate(new int[]{1, 1, 1, 3, 3, 4, 3, 2, 4, 2}));
    }

    // Edge: single element — cannot have a duplicate
    @Test
    void singleElement_noDuplicate() {
        assertFalse(sol.containsDuplicate(new int[]{42}));
    }

    // Edge: two identical elements
    @Test
    void twoIdentical() {
        assertTrue(sol.containsDuplicate(new int[]{0, 0}));
    }

    // Edge: negative numbers with a duplicate
    @Test
    void negativeNumbers_hasDuplicate() {
        assertTrue(sol.containsDuplicate(new int[]{-1, -2, -3, -1}));
    }

    // Edge: all same value (large array)
    @Test
    void allSame() {
        assertTrue(sol.containsDuplicate(new int[]{7, 7, 7, 7, 7}));
    }
}
