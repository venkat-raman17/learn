package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CombinationSumTest {

    private final CombinationSum solution = new CombinationSum();

    @Test
    void example1() {
        // candidates=[2,3,6,7], target=7 -> [[2,2,3],[7]]
        List<List<Integer>> result = solution.combinationSum(new int[]{2, 3, 6, 7}, 7);
        assertEquals(2, result.size());
        assertTrue(result.contains(List.of(2, 2, 3)));
        assertTrue(result.contains(List.of(7)));
    }

    @Test
    void example2() {
        // candidates=[2,3,5], target=8 -> [[2,2,2,2],[2,3,3],[3,5]]
        List<List<Integer>> result = solution.combinationSum(new int[]{2, 3, 5}, 8);
        assertEquals(3, result.size());
        assertTrue(result.contains(List.of(2, 2, 2, 2)));
        assertTrue(result.contains(List.of(2, 3, 3)));
        assertTrue(result.contains(List.of(3, 5)));
    }

    @Test
    void example3_noSolution() {
        // candidates=[2], target=1 -> []
        List<List<Integer>> result = solution.combinationSum(new int[]{2}, 1);
        assertTrue(result.isEmpty());
    }

    @Test
    void singleCandidateExactMatch() {
        List<List<Integer>> result = solution.combinationSum(new int[]{5}, 10);
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of(5, 5)));
    }
}
