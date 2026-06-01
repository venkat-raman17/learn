package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PalindromePartitioningTest {

    private final PalindromePartitioning solution = new PalindromePartitioning();

    @Test
    void example1() {
        // "aab" -> [["a","a","b"],["aa","b"]]
        List<List<String>> result = solution.partition("aab");
        assertEquals(2, result.size());
        assertTrue(result.contains(List.of("a", "a", "b")));
        assertTrue(result.contains(List.of("aa", "b")));
    }

    @Test
    void example2_singleChar() {
        // "a" -> [["a"]]
        List<List<String>> result = solution.partition("a");
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of("a")));
    }

    @Test
    void allSameChars() {
        // "aaa" -> [["a","a","a"],["a","aa"],["aa","a"],["aaa"]]
        List<List<String>> result = solution.partition("aaa");
        assertEquals(4, result.size());
        assertTrue(result.contains(List.of("a", "a", "a")));
        assertTrue(result.contains(List.of("a", "aa")));
        assertTrue(result.contains(List.of("aa", "a")));
        assertTrue(result.contains(List.of("aaa")));
    }

    @Test
    void palindromeSplit() {
        // "racecar" must include the full word as a valid partition
        List<List<String>> result = solution.partition("racecar");
        assertTrue(result.contains(List.of("racecar")));
    }

    @Test
    void allPartitionsContainOnlyPalindromes() {
        List<List<String>> result = solution.partition("aab");
        for (List<String> partition : result) {
            for (String s : partition) {
                String rev = new StringBuilder(s).reverse().toString();
                assertEquals(s, rev, s + " is not a palindrome");
            }
        }
    }
}
