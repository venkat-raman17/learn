package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — Spiral Matrix
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Math & Geometry
 * <p>URL: https://leetcode.com/problems/spiral-matrix/
 *
 * <p>Given an m x n matrix, return all elements in spiral order (clockwise from the top-left).
 *
 * <p>Constraints:
 * <ul>
 *   <li>m == matrix.length</li>
 *   <li>n == matrix[i].length</li>
 *   <li>1 <= m, n <= 10</li>
 *   <li>-100 <= matrix[i][j] <= 100</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   spiralOrder([[1,2,3],[4,5,6],[7,8,9]]) -> [1,2,3,6,9,8,7,4,5]
 *   spiralOrder([[1,2,3,4],[5,6,7,8],[9,10,11,12]]) -> [1,2,3,4,8,12,11,10,9,5,6,7]
 * </pre>
 *
 * <p>Target: Time O(m*n), Space O(1) extra (output list itself is O(m*n)).
 *
 * <p>Hint 1: Maintain four boundaries (top, bottom, left, right) and shrink them after each pass.
 * <p>Hint 2: Process: left-to-right top row, top-to-bottom right col, right-to-left bottom row, bottom-to-top left col; repeat.
 */
public class SpiralMatrix {

    public List<Integer> spiralOrder(int[][] matrix) {
        throw new UnsupportedOperationException("implement me");
    }
}
