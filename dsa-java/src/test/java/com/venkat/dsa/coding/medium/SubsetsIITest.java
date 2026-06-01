package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class SubsetsIITest {

    private final SubsetsII solution = new SubsetsII();

    @Test
    public void testSubsetsWithDup_withDuplicates() {
        int[] nums = {1, 2, 2};
        List<List<Integer>> result = solution.subsetsWithDup(nums);
        assertNotNull(result);
        assertEquals(6, result.size());
        assertTrue(result.stream().anyMatch(List::isEmpty));
        assertTrue(result.stream().anyMatch(s -> s.equals(List.of(1, 2, 2))));
    }

    @Test
    public void testSubsetsWithDup_singleElement() {
        int[] nums = {0};
        List<List<Integer>> result = solution.subsetsWithDup(nums);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(List::isEmpty));
        assertTrue(result.stream().anyMatch(s -> s.equals(List.of(0))));
    }

    @Test
    public void testSubsetsWithDup_allSame() {
        int[] nums = {1, 1, 1};
        List<List<Integer>> result = solution.subsetsWithDup(nums);
        // Expected: [], [1], [1,1], [1,1,1]
        assertEquals(4, result.size());
    }
}
