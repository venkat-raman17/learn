package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class PermutationsTest {

    private final Permutations solution = new Permutations();

    @Test
    public void testPermute_threeElements() {
        int[] nums = {1, 2, 3};
        List<List<Integer>> result = solution.permute(nums);
        assertNotNull(result);
        assertEquals(6, result.size());
        assertTrue(result.stream().anyMatch(p -> p.equals(List.of(1, 2, 3))));
        assertTrue(result.stream().anyMatch(p -> p.equals(List.of(3, 2, 1))));
    }

    @Test
    public void testPermute_twoElements() {
        int[] nums = {0, 1};
        List<List<Integer>> result = solution.permute(nums);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.equals(List.of(0, 1))));
        assertTrue(result.stream().anyMatch(p -> p.equals(List.of(1, 0))));
    }

    @Test
    public void testPermute_singleElement() {
        int[] nums = {7};
        List<List<Integer>> result = solution.permute(nums);
        assertEquals(1, result.size());
        assertEquals(List.of(7), result.get(0));
    }
}
