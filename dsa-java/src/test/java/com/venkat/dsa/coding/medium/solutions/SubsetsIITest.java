package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubsetsIITest {

    private final SubsetsII solution = new SubsetsII();

    @Test
    void example1_oneDuplicate() {
        // [1,2,2] -> [], [1], [1,2], [1,2,2], [2], [2,2]
        List<List<Integer>> result = solution.subsetsWithDup(new int[]{1, 2, 2});
        assertEquals(6, result.size());
        assertTrue(result.contains(List.of()));
        assertTrue(result.contains(List.of(1)));
        assertTrue(result.contains(List.of(2)));
        assertTrue(result.contains(List.of(1, 2)));
        assertTrue(result.contains(List.of(2, 2)));
        assertTrue(result.contains(List.of(1, 2, 2)));
    }

    @Test
    void example2_allSame() {
        // [0] -> [], [0]
        List<List<Integer>> result = solution.subsetsWithDup(new int[]{0});
        assertEquals(2, result.size());
    }

    @Test
    void allDuplicates() {
        // [1,1,1] -> [], [1], [1,1], [1,1,1] = 4 subsets
        List<List<Integer>> result = solution.subsetsWithDup(new int[]{1, 1, 1});
        assertEquals(4, result.size());
        assertTrue(result.contains(List.of()));
        assertTrue(result.contains(List.of(1)));
        assertTrue(result.contains(List.of(1, 1)));
        assertTrue(result.contains(List.of(1, 1, 1)));
    }

    @Test
    void noUniqueLoss() {
        // Distinct results count equals total when there are no dups
        List<List<Integer>> result = solution.subsetsWithDup(new int[]{1, 2, 3});
        assertEquals(8, result.size());
        long distinct = result.stream().distinct().count();
        assertEquals(8, distinct);
    }
}
