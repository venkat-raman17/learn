package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Word Search
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Backtracking
 * <p>URL: https://leetcode.com/problems/word-search/
 *
 * <p>Given an {@code m x n} grid of characters {@code board} and a string {@code word}, return
 * {@code true} if the word exists in the grid. The word can be constructed from letters of
 * sequentially adjacent cells (horizontally or vertically), and the same cell may not be used
 * more than once.
 *
 * <p>Constraints:
 * <ul>
 *   <li>m == board.length, n == board[i].length</li>
 *   <li>1 &lt;= m, n &lt;= 6</li>
 *   <li>1 &lt;= word.length &lt;= 15</li>
 *   <li>board and word consist of only lowercase and uppercase English letters.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
 *   Output: true
 *
 *   Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "SEE"
 *   Output: true
 *
 *   Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCB"
 *   Output: false
 * </pre>
 *
 * <p>Target: Time O(m * n * 4^L) where L = word.length, Space O(L)
 *
 * <p>Hint 1: For each cell, start a DFS if the cell matches word[0]; mark cells as visited
 * by temporarily modifying the board (e.g., replace with '#') and restore on backtrack.
 * <p>Hint 2: Prune early by returning false when the current character does not match
 * the expected character in the word.
 */
public class WordSearch {

    public boolean exist(char[][] board, String word) {
        throw new UnsupportedOperationException("implement me");
    }
}
