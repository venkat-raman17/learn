package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TopKFrequentElementsTest {

    private final TopKFrequentElements sol = new TopKFrequentElements();

    /** Order-independent comparison helper. */
    private Set<Integer> toSet(int[] arr) {
        Set<Integer> s = new HashSet<>();
        for (int x : arr) s.add(x);
        return s;
    }

    // LeetCode example 1: [1,1,1,2,2,3], k=2 -> {1,2}
    @Test
    void example1() {
        int[] result = sol.topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2);
        assertEquals(2, result.length);
        assertEquals(Set.of(1, 2), toSet(result));
    }

    // LeetCode example 2: [1], k=1 -> {1}
    @Test
    void example2_singleElement() {
        int[] result = sol.topKFrequent(new int[]{1}, 1);
        assertArrayEquals(new int[]{1}, result);
    }

    // k equals total number of distinct elements
    @Test
    void kEqualsDistinct() {
        int[] result = sol.topKFrequent(new int[]{4, 4, 3, 3, 2}, 3);
        assertEquals(3, result.length);
        assertEquals(Set.of(2, 3, 4), toSet(result));
    }

    // All elements are unique — each has frequency 1; any k elements are valid
    @Test
    void allUnique_k1() {
        int[] result = sol.topKFrequent(new int[]{5, 3, 1, 2}, 1);
        assertEquals(1, result.length);
        assertTrue(Set.of(5, 3, 1, 2).contains(result[0]));
    }

    // Negative numbers present
    @Test
    void negativeNumbers() {
        int[] result = sol.topKFrequent(new int[]{-1, -1, 2, 2, 2, 3}, 2);
        assertEquals(2, result.length);
        assertEquals(Set.of(2, -1), toSet(result));
    }

    // Large frequency difference — most frequent element should always appear
    @Test
    void dominantElement() {
        int[] nums = new int[100];
        Arrays.fill(nums, 7);
        nums[0] = 99;
        int[] result = sol.topKFrequent(nums, 1);
        assertArrayEquals(new int[]{7}, result);
    }
}
