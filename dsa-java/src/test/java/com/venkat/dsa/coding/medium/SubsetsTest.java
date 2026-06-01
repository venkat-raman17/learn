package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class SubsetsTest {

    private final Subsets solution = new Subsets();

    @Test
    public void testSubsets_threeElements() {
        int[] nums = {1, 2, 3};
        List<List<Integer>> result = solution.subsets(nums);
        assertNotNull(result);
        assertEquals(8, result.size());
    }

    @Test
    public void testSubsets_singleElement() {
        int[] nums = {0};
        List<List<Integer>> result = solution.subsets(nums);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(List::isEmpty));
        assertTrue(result.stream().anyMatch(s -> s.equals(List.of(0))));
    }

    @Test
    public void testSubsets_emptyIncluded() {
        int[] nums = {1, 2};
        List<List<Integer>> result = solution.subsets(nums);
        assertEquals(4, result.size());
        assertTrue(result.stream().anyMatch(List::isEmpty));
    }
}
