package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidAnagramTest {

    private final ValidAnagram sol = new ValidAnagram();

    // LeetCode example 1: "anagram", "nagaram" -> true
    @Test
    void example1_validAnagram() {
        assertTrue(sol.isAnagram("anagram", "nagaram"));
    }

    // LeetCode example 2: "rat", "car" -> false
    @Test
    void example2_notAnagram() {
        assertFalse(sol.isAnagram("rat", "car"));
    }

    // Same string is its own anagram
    @Test
    void sameString() {
        assertTrue(sol.isAnagram("abc", "abc"));
    }

    // Different lengths can never be anagrams
    @Test
    void differentLengths() {
        assertFalse(sol.isAnagram("ab", "abc"));
    }

    // Single character match
    @Test
    void singleChar_match() {
        assertTrue(sol.isAnagram("a", "a"));
    }

    // Single character mismatch
    @Test
    void singleChar_noMatch() {
        assertFalse(sol.isAnagram("a", "b"));
    }

    // Empty strings are anagrams of each other
    @Test
    void bothEmpty() {
        assertTrue(sol.isAnagram("", ""));
    }

    // All same characters in different order
    @Test
    void allSameChars() {
        assertTrue(sol.isAnagram("aab", "baa"));
    }

    // One extra character makes it invalid
    @Test
    void offByOneLetter() {
        assertFalse(sol.isAnagram("aab", "bba"));
    }
}
