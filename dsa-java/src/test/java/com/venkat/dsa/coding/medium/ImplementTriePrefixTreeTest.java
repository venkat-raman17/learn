package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class ImplementTriePrefixTreeTest {

    @Test
    public void testInsertAndSearch() {
        ImplementTriePrefixTree trie = new ImplementTriePrefixTree();
        trie.insert("apple");
        assertTrue(trie.search("apple"));
        assertFalse(trie.search("app"));
    }

    @Test
    public void testStartsWith() {
        ImplementTriePrefixTree trie = new ImplementTriePrefixTree();
        trie.insert("apple");
        assertTrue(trie.startsWith("app"));
        assertFalse(trie.startsWith("apl"));
    }

    @Test
    public void testInsertThenSearchPrefix() {
        ImplementTriePrefixTree trie = new ImplementTriePrefixTree();
        trie.insert("apple");
        trie.insert("app");
        assertTrue(trie.search("app"));
        assertTrue(trie.startsWith("app"));
        assertFalse(trie.search("ap"));
    }
}
