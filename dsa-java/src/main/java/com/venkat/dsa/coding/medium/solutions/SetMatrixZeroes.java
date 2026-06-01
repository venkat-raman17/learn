package com.venkat.dsa.coding.medium.solutions;

/**
 * Set Matrix Zeroes (LeetCode 73)
 *
 * <p>Uses the first row and first column of the matrix itself as marker arrays, needing only
 * two boolean flags to remember whether the first row/column originally contained a zero.
 * In a first pass mark zeros into the first row/column; in a second pass propagate zeros to
 * interior cells; finally zero out the first row/column if originally needed.
 *
 * <p><b>Key insight:</b> Reusing row-0 and col-0 as flags reduces extra space from O(m+n) to O(1).
 *
 * <p><b>Time complexity:</b> O(m * n). <b>Space complexity:</b> O(1).
 */
public class SetMatrixZeroes {

    /**
     * Modifies {@code matrix} in-place: any cell that is 0 causes its entire row and column to be 0.
     *
     * @param matrix m x n integer matrix
     */
    public void setZeroes(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        boolean firstRowZero = false, firstColZero = false;

        // Check if first row has a zero
        for (int j = 0; j < n; j++) {
            if (matrix[0][j] == 0) { firstRowZero = true; break; }
        }
        // Check if first column has a zero
        for (int i = 0; i < m; i++) {
            if (matrix[i][0] == 0) { firstColZero = true; break; }
        }

        // Use first row/col as markers for interior zeros
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0; // mark row
                    matrix[0][j] = 0; // mark col
                }
            }
        }

        // Zero out interior cells based on markers
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }

        // Handle first row
        if (firstRowZero) {
            for (int j = 0; j < n; j++) matrix[0][j] = 0;
        }
        // Handle first column
        if (firstColZero) {
            for (int i = 0; i < m; i++) matrix[i][0] = 0;
        }
    }
}
