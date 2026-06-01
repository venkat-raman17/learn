package com.venkat.dsa.coding.medium.solutions;

/**
 * Rotate Image (LeetCode 48)
 *
 * <p>Rotates an n x n matrix 90 degrees clockwise in-place by composing two simple operations:
 * (1) transpose the matrix (swap matrix[i][j] with matrix[j][i]), then
 * (2) reverse each row left-to-right. Together these produce a clockwise rotation without extra space.
 *
 * <p><b>Key insight:</b> A 90-degree clockwise rotation equals transpose + horizontal flip.
 *
 * <p><b>Time complexity:</b> O(n^2). <b>Space complexity:</b> O(1).
 */
public class RotateImage {

    /**
     * Rotates {@code matrix} 90 degrees clockwise in-place.
     *
     * @param matrix n x n integer matrix
     */
    public void rotate(int[][] matrix) {
        int n = matrix.length;

        // Step 1: Transpose (reflect over main diagonal)
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        }

        // Step 2: Reverse each row
        for (int i = 0; i < n; i++) {
            int lo = 0, hi = n - 1;
            while (lo < hi) {
                int tmp = matrix[i][lo];
                matrix[i][lo] = matrix[i][hi];
                matrix[i][hi] = tmp;
                lo++;
                hi--;
            }
        }
    }
}
