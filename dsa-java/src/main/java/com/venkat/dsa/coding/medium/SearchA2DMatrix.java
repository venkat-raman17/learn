package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Search a 2D Matrix
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Binary Search
 * <p>URL: https://leetcode.com/problems/search-a-2d-matrix/
 *
 * <p>You are given an m x n integer matrix {@code matrix} with the following properties:
 * each row is sorted in non-decreasing order, and the first integer of each row is greater
 * than the last integer of the previous row. Return {@code true} if {@code target} exists
 * in the matrix, or {@code false} otherwise.
 *
 * <p>Constraints:
 * <ul>
 *   <li>m == matrix.length, n == matrix[i].length</li>
 *   <li>1 &lt;= m, n &lt;= 100</li>
 *   <li>-10^4 &lt;= matrix[i][j], target &lt;= 10^4</li>
 * </ul>
 *
 * <p>Example 1: matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3 → true
 * <p>Example 2: matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 13 → false
 *
 * <p>Target: O(log(m*n)) time, O(1) space.
 *
 * <p>Hint 1: Treat the 2D matrix as a flattened 1D sorted array of length m*n and binary search on it.
 * <p>Hint 2: Convert a 1D index {@code mid} to 2D coordinates via {@code row = mid/n}, {@code col = mid%n}.
 */
public class SearchA2DMatrix {

    public boolean searchMatrix(int[][] matrix, int target) {
        throw new UnsupportedOperationException("implement me");
    }
}
