package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DesignAddAndSearchWordsDataStructureTest {

    // -----------------------------------------------------------------------
    // Official LeetCode example
    // -----------------------------------------------------------------------
    @Test
    void testOfficialExample() {
        DesignAddAndSearchWordsDataStructure ws = new DesignAddAndSearchWordsDataStructure();
        ws.addWord("bad");
        ws.addWord("dad");
        ws.addWord("mad");

        assertFalse(ws.search("pad"),  "'pad' was not added");
        assertTrue(ws.search("bad"),   "exact match 'bad'");
        assertTrue(ws.search(".ad"),   "'.ad' matches bad/dad/mad");
        assertTrue(ws.search("b.."),   "'b..' matches 'bad'");
    }

    // -----------------------------------------------------------------------
    // Single dot matches any single character
    // -----------------------------------------------------------------------
    @Test
    void testSingleDot() {
        DesignAddAndSearchWordsDataStructure ws = new DesignAddAndSearchWordsDataStructure();
        ws.addWord("a");
        ws.addWord("b");
        ws.addWord("c");

        assertTrue(ws.search("."),     "single '.' matches any single-char word");
        assertFalse(ws.search(".."),   "'..' needs length-2 word; none added");
    }

    // -----------------------------------------------------------------------
    // All dots pattern
    // -----------------------------------------------------------------------
    @Test
    void testAllDots() {
        DesignAddAndSearchWordsDataStructure ws = new DesignAddAndSearchWordsDataStructure();
        ws.addWord("abc");

        assertTrue(ws.search("..."),   "'...' matches 'abc'");
        assertFalse(ws.search("...."), "no 4-letter word exists");
    }

    // -----------------------------------------------------------------------
    // No false positives when trie has multiple word lengths
    // -----------------------------------------------------------------------
    @Test
    void testMixedLengths() {
        DesignAddAndSearchWordsDataStructure ws = new DesignAddAndSearchWordsDataStructure();
        ws.addWord("at");
        ws.addWord("bat");
        ws.addWord("cat");

        assertTrue(ws.search(".at"),   "'.at' matches 'bat' and 'cat'");
        assertTrue(ws.search("..t"),   "'..t' matches 'bat'/'cat'");
        assertTrue(ws.search("at"),    "exact 'at'");
        assertFalse(ws.search("a"),    "'a' not stored");
        assertFalse(ws.search("ba.t"), "length 4 — no match");
    }

    // -----------------------------------------------------------------------
    // Dot at end only
    // -----------------------------------------------------------------------
    @Test
    void testDotAtEnd() {
        DesignAddAndSearchWordsDataStructure ws = new DesignAddAndSearchWordsDataStructure();
        ws.addWord("hello");

        assertTrue(ws.search("hell."),  "dot at end matches 'o'");
        assertFalse(ws.search("hell.."), "two trailing dots — no 6-letter word");
    }

    // -----------------------------------------------------------------------
    // Empty data structure
    // -----------------------------------------------------------------------
    @Test
    void testEmptyStructure() {
        DesignAddAndSearchWordsDataStructure ws = new DesignAddAndSearchWordsDataStructure();
        assertFalse(ws.search("a"));
        assertFalse(ws.search("."));
    }

    // -----------------------------------------------------------------------
    // Word that is a prefix of another
    // -----------------------------------------------------------------------
    @Test
    void testPrefixVsFullWord() {
        DesignAddAndSearchWordsDataStructure ws = new DesignAddAndSearchWordsDataStructure();
        ws.addWord("run");
        ws.addWord("runner");

        assertTrue(ws.search("run"));
        assertTrue(ws.search("runner"));
        assertTrue(ws.search("r.."));
        assertFalse(ws.search("ru"));  // not stored
        assertTrue(ws.search("r....."), "'r.....' = 6 chars, matches 'runner'");
    }

    // -----------------------------------------------------------------------
    // Duplicate add is harmless
    // -----------------------------------------------------------------------
    @Test
    void testDuplicateAdd() {
        DesignAddAndSearchWordsDataStructure ws = new DesignAddAndSearchWordsDataStructure();
        ws.addWord("test");
        ws.addWord("test");
        assertTrue(ws.search("test"));
        assertTrue(ws.search("...."));
    }
}
