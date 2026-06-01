package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class CombinationSumIITest {

    private final CombinationSumII solution = new CombinationSumII();

    @Test
    public void testCombinationSum2_target8() {
        int[] candidates = {10, 1, 2, 7, 6, 1, 5};
        List<List<Integer>> result = solution.combinationSum2(candidates, 8);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.stream().anyMatch(c -> c.equals(List.of(1, 1, 6))));
        assertTrue(result.stream().anyMatch(c -> c.equals(List.of(1, 7))));
    }

    @Test
    public void testCombinationSum2_target5() {
        int[] candidates = {2, 5, 2, 1, 2};
        List<List<Integer>> result = solution.combinationSum2(candidates, 5);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.equals(List.of(1, 2, 2))));
        assertTrue(result.stream().anyMatch(c -> c.equals(List.of(5))));
    }

    @Test
    public void testCombinationSum2_noSolution() {
        int[] candidates = {2, 4};
        List<List<Integer>> result = solution.combinationSum2(candidates, 1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
