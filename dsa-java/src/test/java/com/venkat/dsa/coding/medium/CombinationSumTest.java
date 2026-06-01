package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class CombinationSumTest {

    private final CombinationSum solution = new CombinationSum();

    @Test
    public void testCombinationSum_target7() {
        int[] candidates = {2, 3, 6, 7};
        List<List<Integer>> result = solution.combinationSum(candidates, 7);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.equals(List.of(2, 2, 3))));
        assertTrue(result.stream().anyMatch(c -> c.equals(List.of(7))));
    }

    @Test
    public void testCombinationSum_target8() {
        int[] candidates = {2, 3, 5};
        List<List<Integer>> result = solution.combinationSum(candidates, 8);
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testCombinationSum_noSolution() {
        int[] candidates = {3, 5};
        List<List<Integer>> result = solution.combinationSum(candidates, 1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
