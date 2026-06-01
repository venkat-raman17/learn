package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Surrounded Regions
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Graphs
 * <p>URL: https://leetcode.com/problems/surrounded-regions/
 *
 * <p>Given an m x n board containing 'X' and 'O', capture all regions surrounded by 'X'.
 * A region is captured by flipping all 'O's into 'X's in that surrounded region.
 * 'O's on the border or connected to a border 'O' are NOT flipped.
 *
 * <p>Constraints:
 * <ul>
 *   <li>m == board.length, n == board[i].length</li>
 *   <li>1 <= m, n <= 200</li>
 *   <li>board[i][j] is 'X' or 'O'</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 * Input: board = [["X","X","X","X"],["X","O","O","X"],["X","X","O","X"],["X","O","X","X"]]
 * Output: [["X","X","X","X"],["X","X","X","X"],["X","X","X","X"],["X","O","X","X"]]
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 * Input: board = [["X"]]
 * Output: [["X"]]
 * </pre>
 *
 * <p>Target: Time O(m*n), Space O(m*n)
 *
 * <p>Hint 1: DFS/BFS from all border 'O' cells to mark safe 'O's (not surrounded).
 * <p>Hint 2: After marking, flip unmarked 'O's to 'X', then restore marked cells back to 'O'.
 */
public class SurroundedRegions {

    public void solve(char[][] board) {
        throw new UnsupportedOperationException("implement me");
    }
}
