package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CombinationSumIITest {

    private final CombinationSumII solution = new CombinationSumII();

    @Test
    void example1() {
        // candidates=[10,1,2,7,6,1,5], target=8
        // Expected: [[1,1,6],[1,2,5],[1,7],[2,6]]
        List<List<Integer>> result = solution.combinationSum2(new int[]{10, 1, 2, 7, 6, 1, 5}, 8);
        assertEquals(4, result.size());
        assertTrue(result.contains(List.of(1, 1, 6)));
        assertTrue(result.contains(List.of(1, 2, 5)));
        assertTrue(result.contains(List.of(1, 7)));
        assertTrue(result.contains(List.of(2, 6)));
    }

    @Test
    void example2() {
        // candidates=[2,5,2,1,2], target=5
        // Expected: [[1,2,2],[5]]
        List<List<Integer>> result = solution.combinationSum2(new int[]{2, 5, 2, 1, 2}, 5);
        assertEquals(2, result.size());
        assertTrue(result.contains(List.of(1, 2, 2)));
        assertTrue(result.contains(List.of(5)));
    }

    @Test
    void noSolution() {
        List<List<Integer>> result = solution.combinationSum2(new int[]{1, 1}, 3);
        assertTrue(result.isEmpty());
    }

    @Test
    void noDuplicateCombinations() {
        List<List<Integer>> result = solution.combinationSum2(new int[]{2, 5, 2, 1, 2}, 5);
        long distinct = result.stream().distinct().count();
        assertEquals(result.size(), distinct);
    }
}
