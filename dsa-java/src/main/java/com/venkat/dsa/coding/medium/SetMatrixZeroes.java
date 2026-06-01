package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Set Matrix Zeroes
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Math & Geometry
 * <p>URL: https://leetcode.com/problems/set-matrix-zeroes/
 *
 * <p>Given an m x n integer matrix, if an element is 0 set its entire row and column to 0.
 * This must be done in-place.
 *
 * <p>Constraints:
 * <ul>
 *   <li>m == matrix.length</li>
 *   <li>n == matrix[0].length</li>
 *   <li>1 <= m, n <= 200</li>
 *   <li>-2^31 <= matrix[i][j] <= 2^31 - 1</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input:  [[1,1,1],[1,0,1],[1,1,1]]
 *   Output: [[1,0,1],[0,0,0],[1,0,1]]
 *
 *   Input:  [[0,1,2,0],[3,4,5,2],[1,3,1,5]]
 *   Output: [[0,0,0,0],[0,4,5,0],[0,3,1,0]]
 * </pre>
 *
 * <p>Target: Time O(m*n), Space O(1) using the first row/column as markers.
 *
 * <p>Hint 1: Use the first row and first column themselves as flag arrays to avoid O(m+n) extra space.
 * <p>Hint 2: Handle the first row/column's own zero status with a separate boolean before overwriting.
 */
public class SetMatrixZeroes {

    public void setZeroes(int[][] matrix) {
        throw new UnsupportedOperationException("implement me");
    }
}
