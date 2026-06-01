package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AlienDictionaryTest {

    private final AlienDictionary sol = new AlienDictionary();

    /**
     * Validates that {@code order} is a valid topological ordering of the characters
     * implied by the sorted alien word list.
     */
    private boolean isValidOrder(String[] words, String order) {
        if (order.isEmpty()) return false;
        // Build position map
        Map<Character, Integer> pos = new HashMap<>();
        for (int i = 0; i < order.length(); i++) pos.put(order.charAt(i), i);

        // Every character in words must appear in order
        for (String w : words) {
            for (char c : w.toCharArray()) {
                if (!pos.containsKey(c)) return false;
            }
        }

        // For each adjacent pair, the first differing character must be ordered correctly
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i], w2 = words[i + 1];
            int minLen = Math.min(w1.length(), w2.length());
            boolean foundDiff = false;
            for (int j = 0; j < minLen; j++) {
                char c1 = w1.charAt(j), c2 = w2.charAt(j);
                if (c1 != c2) {
                    if (pos.get(c1) >= pos.get(c2)) return false;
                    foundDiff = true;
                    break;
                }
            }
            if (!foundDiff && w1.length() > w2.length()) return false;
        }
        return true;
    }

    @Test
    void testExample1() {
        // LeetCode example 1: ["wrt","wrf","er","ett","rftt"]
        // Derived edges: t->f, w->e, r->t, e->r
        // Valid order: w,e,r,t,f
        String[] words = {"wrt", "wrf", "er", "ett", "rftt"};
        String result = sol.alienOrder(words);
        assertTrue(isValidOrder(words, result),
                "Expected a valid alien order but got: " + result);
        assertEquals(5, result.length()); // All 5 distinct chars
    }

    @Test
    void testExample2() {
        // LeetCode example 2: ["z","x"] -> z before x
        String[] words = {"z", "x"};
        String result = sol.alienOrder(words);
        assertTrue(isValidOrder(words, result));
        // z must come before x
        assertTrue(result.indexOf('z') < result.indexOf('x'));
    }

    @Test
    void testCycleReturnEmpty() {
        // z->x and x->z creates a cycle
        String[] words = {"z", "x", "z"};
        assertEquals("", sol.alienOrder(words));
    }

    @Test
    void testInvalidPrefixOrder() {
        // "abc" before "ab" is invalid (prefix rule)
        String[] words = {"abc", "ab"};
        assertEquals("", sol.alienOrder(words));
    }

    @Test
    void testSingleWord() {
        // Only one word: characters have no relative ordering from comparisons
        String[] words = {"abc"};
        String result = sol.alienOrder(words);
        // Result must contain exactly a, b, c (in some valid order — no constraints)
        assertEquals(3, result.length());
        assertTrue(result.contains("a") && result.contains("b") && result.contains("c"));
    }

    @Test
    void testAllSameWords() {
        // No ordering information when all words are identical
        String[] words = {"aa", "aa"};
        String result = sol.alienOrder(words);
        // Only one unique char 'a', no edges, valid result is "a"
        assertEquals("a", result);
    }

    @Test
    void testSingleCharWords() {
        // ["b","a","c"] -> b before a, a before c
        String[] words = {"b", "a", "c"};
        String result = sol.alienOrder(words);
        assertTrue(isValidOrder(words, result));
        assertTrue(result.indexOf('b') < result.indexOf('a'));
        assertTrue(result.indexOf('a') < result.indexOf('c'));
    }

    @Test
    void testNoRelativeOrdering() {
        // Words only differ in length, same prefix — no character ordering constraints
        // ["a","aa","aaa"]: all same prefix, longer word comes later — valid, no edges
        String[] words = {"a", "aa", "aaa"};
        String result = sol.alienOrder(words);
        assertEquals("a", result); // Only one unique character
    }
}
