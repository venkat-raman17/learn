package com.venkat.dsa.coding.hard.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * N-Queens (LeetCode 51)
 *
 * Approach: Backtracking row by row. For each row, try placing a queen in each column.
 * Use three boolean arrays to track which columns, left diagonals (\) and right
 * diagonals (/) are already occupied. A cell (r,c) is on left-diagonal index (r-c+n-1)
 * and right-diagonal index (r+c). When all n rows are filled, convert the column array
 * to the board representation and record it.
 *
 * Key insight: O(1) conflict detection via three sets/arrays (column, two diagonal
 * directions) instead of scanning the board each time, giving clean O(n!) traversal.
 *
 * Time:  O(n!) — at most n! valid placements to explore.
 * Space: O(n) for the three occupancy arrays + recursion depth.
 */
public class NQueens {

    public List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        int[] queens = new int[n];              // queens[row] = column of queen in that row
        boolean[] cols = new boolean[n];
        boolean[] leftDiag = new boolean[2 * n - 1];  // r - c + (n-1)
        boolean[] rightDiag = new boolean[2 * n - 1]; // r + c

        backtrack(0, n, queens, cols, leftDiag, rightDiag, result);
        return result;
    }

    private void backtrack(int row, int n, int[] queens,
                           boolean[] cols, boolean[] leftDiag, boolean[] rightDiag,
                           List<List<String>> result) {
        if (row == n) {
            result.add(buildBoard(queens, n));
            return;
        }

        for (int col = 0; col < n; col++) {
            int ld = row - col + n - 1;
            int rd = row + col;

            if (cols[col] || leftDiag[ld] || rightDiag[rd]) continue; // attacked cell

            queens[row] = col;
            cols[col] = leftDiag[ld] = rightDiag[rd] = true;

            backtrack(row + 1, n, queens, cols, leftDiag, rightDiag, result);

            cols[col] = leftDiag[ld] = rightDiag[rd] = false; // backtrack
        }
    }

    private List<String> buildBoard(int[] queens, int n) {
        List<String> board = new ArrayList<>();
        for (int row = 0; row < n; row++) {
            char[] line = new char[n];
            java.util.Arrays.fill(line, '.');
            line[queens[row]] = 'Q';
            board.add(new String(line));
        }
        return board;
    }
}
