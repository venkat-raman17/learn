package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubsetsTest {

    private final Subsets solution = new Subsets();

    @Test
    void example1_threeElements() {
        List<List<Integer>> result = solution.subsets(new int[]{1, 2, 3});
        assertEquals(8, result.size()); // 2^3
        // All 8 subsets must be present
        assertTrue(result.contains(List.of()));
        assertTrue(result.contains(List.of(1)));
        assertTrue(result.contains(List.of(2)));
        assertTrue(result.contains(List.of(3)));
        assertTrue(result.contains(List.of(1, 2)));
        assertTrue(result.contains(List.of(1, 3)));
        assertTrue(result.contains(List.of(2, 3)));
        assertTrue(result.contains(List.of(1, 2, 3)));
    }

    @Test
    void example2_singleElement() {
        List<List<Integer>> result = solution.subsets(new int[]{0});
        assertEquals(2, result.size());
        assertTrue(result.contains(List.of()));
        assertTrue(result.contains(List.of(0)));
    }

    @Test
    void emptyArray() {
        List<List<Integer>> result = solution.subsets(new int[]{});
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of()));
    }

    @Test
    void twoElements() {
        List<List<Integer>> result = solution.subsets(new int[]{4, 5});
        assertEquals(4, result.size());
        assertTrue(result.contains(List.of()));
        assertTrue(result.contains(List.of(4)));
        assertTrue(result.contains(List.of(5)));
        assertTrue(result.contains(List.of(4, 5)));
    }
}
