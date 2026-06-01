package com.venkat.dsa.coding.hard;

import java.util.List;

/**
 * NeetCode / LeetCode — N Queens
 *
 * <p>Difficulty: HARD
 * <p>Pattern: Backtracking
 * <p>URL: https://leetcode.com/problems/n-queens/
 *
 * <p>The n-queens puzzle is the problem of placing {@code n} queens on an {@code n x n} chessboard
 * such that no two queens attack each other (no two queens share the same row, column, or diagonal).
 * Given an integer {@code n}, return all distinct solutions to the n-queens puzzle. Each solution
 * contains a distinct board configuration where 'Q' represents a queen and '.' represents an empty
 * space.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= n &lt;= 9</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: n = 4
 *   Output: [[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]
 *
 *   Input: n = 1
 *   Output: [["Q"]]
 * </pre>
 *
 * <p>Target: Time O(n!), Space O(n^2)
 *
 * <p>Hint 1: Place queens one row at a time; for each row, try every column and check whether
 * the column and both diagonals (col, row-col, row+col) are free using three hash sets.
 * <p>Hint 2: After placing a queen, add its column and diagonals to the forbidden sets, recurse
 * to the next row, then remove them when backtracking.
 */
public class NQueens {

    public List<List<String>> solveNQueens(int n) {
        throw new UnsupportedOperationException("implement me");
    }
}
