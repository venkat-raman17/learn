package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidSudokuTest {

    private final ValidSudoku sol = new ValidSudoku();

    // LeetCode example 1: valid partially-filled board
    @Test
    void example1_valid() {
        char[][] board = {
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };
        assertTrue(sol.isValidSudoku(board));
    }

    // LeetCode example 2: invalid board — digit 8 appears twice in the same column
    @Test
    void example2_invalid() {
        char[][] board = {
            {'8','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };
        assertFalse(sol.isValidSudoku(board));
    }

    // All dots — empty board is valid
    @Test
    void emptyBoard_valid() {
        char[][] board = new char[9][9];
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                board[r][c] = '.';
        assertTrue(sol.isValidSudoku(board));
    }

    // Duplicate in first row
    @Test
    void duplicateInRow() {
        char[][] board = new char[9][9];
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                board[r][c] = '.';
        board[0][0] = '1';
        board[0][1] = '1';  // duplicate '1' in row 0
        assertFalse(sol.isValidSudoku(board));
    }

    // Duplicate in first column
    @Test
    void duplicateInColumn() {
        char[][] board = new char[9][9];
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                board[r][c] = '.';
        board[0][0] = '5';
        board[1][0] = '5';  // duplicate '5' in column 0
        assertFalse(sol.isValidSudoku(board));
    }

    // Duplicate in a 3x3 box
    @Test
    void duplicateInBox() {
        char[][] board = new char[9][9];
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                board[r][c] = '.';
        // Top-left box: positions (0,0) and (2,2)
        board[0][0] = '9';
        board[2][2] = '9';  // same box, same digit
        assertFalse(sol.isValidSudoku(board));
    }

    // Same digit in different rows, different columns, different boxes — valid
    @Test
    void samedigitDifferentBoxes_valid() {
        char[][] board = new char[9][9];
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                board[r][c] = '.';
        // Place '1' in (0,0) and (3,3) — different row, col, and box
        board[0][0] = '1';
        board[3][3] = '1';
        assertTrue(sol.isValidSudoku(board));
    }
}
