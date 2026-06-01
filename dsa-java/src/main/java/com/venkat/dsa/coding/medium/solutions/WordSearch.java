package com.venkat.dsa.coding.medium.solutions;

/**
 * Word Search (LeetCode 79)
 *
 * Approach: DFS/backtracking on the grid. For each cell that matches word[0], launch a
 * recursive search that walks in all four directions. To avoid re-visiting a cell within
 * the same path, temporarily mark it (e.g. by XORing with a sentinel value) then restore
 * it after the recursive call returns.
 *
 * Key insight: in-place marking with the '#' sentinel is O(1) extra space per call and
 * avoids a separate "visited" boolean grid, keeping space complexity O(L) for the call stack.
 *
 * Time:  O(m * n * 4^L) where m*n = grid size, L = word length.
 * Space: O(L) recursion depth.
 */
public class WordSearch {

    public boolean exist(char[][] board, String word) {
        int rows = board.length;
        int cols = board[0].length;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (dfs(board, word, r, c, 0)) return true;
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, String word, int r, int c, int index) {
        if (index == word.length()) return true; // matched all characters

        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return false;
        if (board[r][c] != word.charAt(index)) return false;

        char temp = board[r][c];
        board[r][c] = '#'; // mark visited

        boolean found = dfs(board, word, r + 1, c, index + 1)
                     || dfs(board, word, r - 1, c, index + 1)
                     || dfs(board, word, r, c + 1, index + 1)
                     || dfs(board, word, r, c - 1, index + 1);

        board[r][c] = temp; // restore (backtrack)
        return found;
    }
}
