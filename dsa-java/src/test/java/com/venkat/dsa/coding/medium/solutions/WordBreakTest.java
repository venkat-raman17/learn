package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordBreakTest {

    private final WordBreak solution = new WordBreak();

    @Test
    void example1() {
        // s="leetcode", dict=["leet","code"] -> true
        assertTrue(solution.wordBreak("leetcode", List.of("leet", "code")));
    }

    @Test
    void example2() {
        // s="applepenapple", dict=["apple","pen"] -> true
        assertTrue(solution.wordBreak("applepenapple", List.of("apple", "pen")));
    }

    @Test
    void example3_impossible() {
        // s="catsandog", dict=["cats","dog","sand","and","cat"] -> false
        assertFalse(solution.wordBreak("catsandog", List.of("cats", "dog", "sand", "and", "cat")));
    }

    @Test
    void singleWord_exactMatch() {
        assertTrue(solution.wordBreak("hello", List.of("hello")));
    }

    @Test
    void singleWord_noMatch() {
        assertFalse(solution.wordBreak("hello", List.of("world")));
    }

    @Test
    void repeatedWordUsed() {
        // s="aaaa", dict=["a","aa"] -> true
        assertTrue(solution.wordBreak("aaaa", List.of("a", "aa")));
    }

    @Test
    void emptyString() {
        // empty string is always segmentable (vacuously)
        assertTrue(solution.wordBreak("", List.of("a")));
    }
}
