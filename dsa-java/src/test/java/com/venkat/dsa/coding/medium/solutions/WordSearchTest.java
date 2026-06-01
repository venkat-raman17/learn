package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordSearchTest {

    private final WordSearch solution = new WordSearch();

    @Test
    void example1_found() {
        char[][] board = {
            {'A','B','C','E'},
            {'S','F','C','S'},
            {'A','D','E','E'}
        };
        assertTrue(solution.exist(board, "ABCCED"));
    }

    @Test
    void example2_found() {
        char[][] board = {
            {'A','B','C','E'},
            {'S','F','C','S'},
            {'A','D','E','E'}
        };
        assertTrue(solution.exist(board, "SEE"));
    }

    @Test
    void example3_notFound() {
        char[][] board = {
            {'A','B','C','E'},
            {'S','F','C','S'},
            {'A','D','E','E'}
        };
        assertFalse(solution.exist(board, "ABCB"));
    }

    @Test
    void singleCellMatch() {
        char[][] board = {{'a'}};
        assertTrue(solution.exist(board, "a"));
    }

    @Test
    void singleCellNoMatch() {
        char[][] board = {{'a'}};
        assertFalse(solution.exist(board, "b"));
    }

    @Test
    void cannotReuseSameCell() {
        // word "AAA" cannot be formed when only one 'A' exists
        char[][] board = {{'A', 'B'}, {'C', 'D'}};
        assertFalse(solution.exist(board, "AAA"));
    }

    @Test
    void boardRestoredAfterSearch() {
        // Run two searches on same board to ensure backtracking restores state
        char[][] board = {
            {'A','B','C','E'},
            {'S','F','C','S'},
            {'A','D','E','E'}
        };
        assertTrue(solution.exist(board, "ABCCED"));
        assertTrue(solution.exist(board, "SEE")); // board must be intact
    }
}
