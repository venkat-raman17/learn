package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class PalindromePartitioningTest {

    private final PalindromePartitioning solution = new PalindromePartitioning();

    @Test
    public void testPartition_aab() {
        List<List<String>> result = solution.partition("aab");
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.equals(List.of("a", "a", "b"))));
        assertTrue(result.stream().anyMatch(p -> p.equals(List.of("aa", "b"))));
    }

    @Test
    public void testPartition_singleChar() {
        List<List<String>> result = solution.partition("a");
        assertEquals(1, result.size());
        assertEquals(List.of("a"), result.get(0));
    }

    @Test
    public void testPartition_aba() {
        List<List<String>> result = solution.partition("aba");
        assertNotNull(result);
        // Expected: [["a","b","a"],["aba"]]
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.equals(List.of("aba"))));
    }
}
