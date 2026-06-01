package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class WordSearchTest {

    private final WordSearch solution = new WordSearch();

    @Test
    public void testExist_wordFound_ABCCED() {
        char[][] board = {
            {'A', 'B', 'C', 'E'},
            {'S', 'F', 'C', 'S'},
            {'A', 'D', 'E', 'E'}
        };
        assertTrue(solution.exist(board, "ABCCED"));
    }

    @Test
    public void testExist_wordFound_SEE() {
        char[][] board = {
            {'A', 'B', 'C', 'E'},
            {'S', 'F', 'C', 'S'},
            {'A', 'D', 'E', 'E'}
        };
        assertTrue(solution.exist(board, "SEE"));
    }

    @Test
    public void testExist_wordNotFound_ABCB() {
        char[][] board = {
            {'A', 'B', 'C', 'E'},
            {'S', 'F', 'C', 'S'},
            {'A', 'D', 'E', 'E'}
        };
        assertFalse(solution.exist(board, "ABCB"));
    }
}
