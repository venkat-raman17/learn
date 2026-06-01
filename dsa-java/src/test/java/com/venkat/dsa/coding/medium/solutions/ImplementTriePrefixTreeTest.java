package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ImplementTriePrefixTreeTest {

    // -----------------------------------------------------------------------
    // Official LeetCode example
    // -----------------------------------------------------------------------
    @Test
    void testOfficialExample() {
        ImplementTriePrefixTree trie = new ImplementTriePrefixTree();
        trie.insert("apple");

        assertTrue(trie.search("apple"),      "exact word 'apple' should be found");
        assertFalse(trie.search("app"),        "'app' not inserted, should not be found");
        assertTrue(trie.startsWith("app"),     "'apple' starts with 'app'");

        trie.insert("app");
        assertTrue(trie.search("app"),         "'app' inserted, should now be found");
    }

    // -----------------------------------------------------------------------
    // Word inserted then searched exactly
    // -----------------------------------------------------------------------
    @Test
    void testInsertAndSearchExact() {
        ImplementTriePrefixTree trie = new ImplementTriePrefixTree();
        trie.insert("hello");
        assertTrue(trie.search("hello"));
        assertFalse(trie.search("hell"));
        assertFalse(trie.search("helloo"));
    }

    // -----------------------------------------------------------------------
    // startsWith checks
    // -----------------------------------------------------------------------
    @Test
    void testStartsWith() {
        ImplementTriePrefixTree trie = new ImplementTriePrefixTree();
        trie.insert("interview");
        assertTrue(trie.startsWith("i"));
        assertTrue(trie.startsWith("inter"));
        assertTrue(trie.startsWith("interview"));
        assertFalse(trie.startsWith("x"));
        assertFalse(trie.startsWith("interviews")); // longer than inserted word
    }

    // -----------------------------------------------------------------------
    // Single character word
    // -----------------------------------------------------------------------
    @Test
    void testSingleCharWord() {
        ImplementTriePrefixTree trie = new ImplementTriePrefixTree();
        trie.insert("a");
        assertTrue(trie.search("a"));
        assertTrue(trie.startsWith("a"));
        assertFalse(trie.search("ab"));
    }

    // -----------------------------------------------------------------------
    // Multiple words sharing prefix
    // -----------------------------------------------------------------------
    @Test
    void testSharedPrefix() {
        ImplementTriePrefixTree trie = new ImplementTriePrefixTree();
        trie.insert("car");
        trie.insert("card");
        trie.insert("care");
        trie.insert("careful");

        assertTrue(trie.search("car"));
        assertTrue(trie.search("card"));
        assertTrue(trie.search("care"));
        assertTrue(trie.search("careful"));
        assertFalse(trie.search("ca"));
        assertFalse(trie.search("cares"));

        assertTrue(trie.startsWith("ca"));
        assertTrue(trie.startsWith("car"));
        assertTrue(trie.startsWith("care"));
        assertFalse(trie.startsWith("z"));
    }

    // -----------------------------------------------------------------------
    // Empty trie: search and startsWith return false
    // -----------------------------------------------------------------------
    @Test
    void testEmptyTrie() {
        ImplementTriePrefixTree trie = new ImplementTriePrefixTree();
        assertFalse(trie.search("anything"));
        assertFalse(trie.startsWith("any"));
    }

    // -----------------------------------------------------------------------
    // Re-inserting the same word does not break anything
    // -----------------------------------------------------------------------
    @Test
    void testDuplicateInsert() {
        ImplementTriePrefixTree trie = new ImplementTriePrefixTree();
        trie.insert("java");
        trie.insert("java");
        assertTrue(trie.search("java"));
        assertFalse(trie.search("jav"));
    }

    // -----------------------------------------------------------------------
    // Long word
    // -----------------------------------------------------------------------
    @Test
    void testLongWord() {
        ImplementTriePrefixTree trie = new ImplementTriePrefixTree();
        String word = "abcdefghijklmnopqrstuvwxyz";
        trie.insert(word);
        assertTrue(trie.search(word));
        assertTrue(trie.startsWith("abcdefghij"));
        assertFalse(trie.search("abcdefghij"));
    }
}
