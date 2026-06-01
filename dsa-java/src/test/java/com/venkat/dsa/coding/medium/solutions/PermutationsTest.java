package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PermutationsTest {

    private final Permutations solution = new Permutations();

    @Test
    void example1_threeDistinct() {
        // [1,2,3] -> 6 permutations
        List<List<Integer>> result = solution.permute(new int[]{1, 2, 3});
        assertEquals(6, result.size());
        assertTrue(result.contains(List.of(1, 2, 3)));
        assertTrue(result.contains(List.of(1, 3, 2)));
        assertTrue(result.contains(List.of(2, 1, 3)));
        assertTrue(result.contains(List.of(2, 3, 1)));
        assertTrue(result.contains(List.of(3, 1, 2)));
        assertTrue(result.contains(List.of(3, 2, 1)));
    }

    @Test
    void example2_twoElements() {
        List<List<Integer>> result = solution.permute(new int[]{0, 1});
        assertEquals(2, result.size());
        assertTrue(result.contains(List.of(0, 1)));
        assertTrue(result.contains(List.of(1, 0)));
    }

    @Test
    void example3_singleElement() {
        List<List<Integer>> result = solution.permute(new int[]{1});
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of(1)));
    }

    @Test
    void noDuplicatePermutations() {
        List<List<Integer>> result = solution.permute(new int[]{1, 2, 3});
        // Verify no duplicates
        long distinct = result.stream().distinct().count();
        assertEquals(result.size(), distinct);
    }
}
