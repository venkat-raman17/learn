package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class WordSearchIITest {

    @Test
    public void testExample1() {
        WordSearchII sol = new WordSearchII();
        char[][] board = {
            {'o','a','a','n'},
            {'e','t','a','e'},
            {'i','h','k','r'},
            {'i','f','l','v'}
        };
        String[] words = {"oath","pea","eat","rain"};
        List<String> result = sol.findWords(board, words);
        Set<String> resultSet = new HashSet<>(result);
        assertEquals(new HashSet<>(Arrays.asList("eat", "oath")), resultSet);
    }

    @Test
    public void testExample2NoMatch() {
        WordSearchII sol = new WordSearchII();
        char[][] board = {
            {'a','b'},
            {'c','d'}
        };
        String[] words = {"abcb"};
        List<String> result = sol.findWords(board, words);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSingleCellWord() {
        WordSearchII sol = new WordSearchII();
        char[][] board = {{'a'}};
        String[] words = {"a", "b"};
        List<String> result = sol.findWords(board, words);
        assertEquals(1, result.size());
        assertTrue(result.contains("a"));
    }
}
