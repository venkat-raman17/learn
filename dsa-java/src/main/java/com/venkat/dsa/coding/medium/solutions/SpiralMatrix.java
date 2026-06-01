package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Spiral Matrix (LeetCode 54)
 *
 * <p>Traverses an m x n matrix in spiral order (right, down, left, up, repeat) using four
 * boundary variables (top, bottom, left, right) that shrink after each direction pass.
 * Stops as soon as all m*n elements have been collected.
 *
 * <p><b>Key insight:</b> After completing one direction, shrink the corresponding boundary
 * before switching direction; the total-element count guards against double-counting in
 * non-square matrices.
 *
 * <p><b>Time complexity:</b> O(m * n). <b>Space complexity:</b> O(1) extra (output list aside).
 */
public class SpiralMatrix {

    /**
     * Returns elements of {@code matrix} in spiral order.
     *
     * @param matrix m x n integer matrix
     * @return list of integers in spiral order
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        if (matrix == null || matrix.length == 0) return result;

        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;

        while (top <= bottom && left <= right) {
            // Traverse right along top row
            for (int col = left; col <= right; col++)
                result.add(matrix[top][col]);
            top++;

            // Traverse down along right column
            for (int row = top; row <= bottom; row++)
                result.add(matrix[row][right]);
            right--;

            // Traverse left along bottom row (guard: top may have passed bottom)
            if (top <= bottom) {
                for (int col = right; col >= left; col--)
                    result.add(matrix[bottom][col]);
                bottom--;
            }

            // Traverse up along left column (guard: left may have passed right)
            if (left <= right) {
                for (int row = bottom; row >= top; row--)
                    result.add(matrix[row][left]);
                left++;
            }
        }
        return result;
    }
}
