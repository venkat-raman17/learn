package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InterleavingStringTest {

    private final InterleavingString sol = new InterleavingString();

    // LeetCode example 1: s1="aabcc", s2="dbbca", s3="aadbbcbcac" -> true
    @Test
    void example1() {
        assertTrue(sol.isInterleave("aabcc", "dbbca", "aadbbcbcac"));
    }

    // LeetCode example 2: s1="aabcc", s2="dbbca", s3="aadbbbaccc" -> false
    @Test
    void example2() {
        assertFalse(sol.isInterleave("aabcc", "dbbca", "aadbbbaccc"));
    }

    // LeetCode example 3: s1="", s2="", s3="" -> true
    @Test
    void allEmpty() {
        assertTrue(sol.isInterleave("", "", ""));
    }

    // s1 empty, s3 = s2
    @Test
    void s1Empty() {
        assertTrue(sol.isInterleave("", "abc", "abc"));
    }

    // s2 empty, s3 = s1
    @Test
    void s2Empty() {
        assertTrue(sol.isInterleave("abc", "", "abc"));
    }

    // Length mismatch -> false
    @Test
    void lengthMismatch() {
        assertFalse(sol.isInterleave("a", "b", "abc"));
    }

    // Simple interleave: "ab" + "12" -> "a1b2" true
    @Test
    void simpleTrue() {
        assertTrue(sol.isInterleave("ab", "12", "a1b2"));
    }

    // "ab" + "12" -> "ab21" false (wrong order)
    @Test
    void simpleFalse() {
        assertFalse(sol.isInterleave("ab", "12", "ba12"));
    }
}
