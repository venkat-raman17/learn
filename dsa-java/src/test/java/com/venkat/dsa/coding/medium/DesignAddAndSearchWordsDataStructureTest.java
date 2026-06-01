package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class DesignAddAndSearchWordsDataStructureTest {

    @Test
    public void testExactSearch() {
        DesignAddAndSearchWordsDataStructure ws = new DesignAddAndSearchWordsDataStructure();
        ws.addWord("bad");
        ws.addWord("dad");
        ws.addWord("mad");
        assertFalse(ws.search("pad"));
        assertTrue(ws.search("bad"));
    }

    @Test
    public void testWildcardSearch() {
        DesignAddAndSearchWordsDataStructure ws = new DesignAddAndSearchWordsDataStructure();
        ws.addWord("bad");
        ws.addWord("dad");
        ws.addWord("mad");
        assertTrue(ws.search(".ad"));
        assertTrue(ws.search("b.."));
    }

    @Test
    public void testSingleCharAndDot() {
        DesignAddAndSearchWordsDataStructure ws = new DesignAddAndSearchWordsDataStructure();
        ws.addWord("a");
        assertTrue(ws.search("a"));
        assertTrue(ws.search("."));
        assertFalse(ws.search("aa"));
    }
}
