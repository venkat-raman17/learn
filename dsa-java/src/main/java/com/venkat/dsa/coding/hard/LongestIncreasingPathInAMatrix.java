package com.venkat.dsa.coding.hard;

/**
 * NeetCode / LeetCode — Longest Increasing Path in a Matrix
 *
 * <p>Difficulty: HARD
 * <p>Pattern: 2-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/longest-increasing-path-in-a-matrix/
 *
 * <p>Given an m x n integer matrix, return the length of the longest strictly increasing
 * path. From each cell you can move in four directions (up, down, left, right) and you
 * cannot move diagonally or outside the boundary.
 *
 * <p>Constraints:
 * <ul>
 *   <li>m == matrix.length, n == matrix[i].length</li>
 *   <li>1 <= m, n <= 200</li>
 *   <li>0 <= matrix[i][j] <= 2^31 - 1</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   longestIncreasingPath([[9,9,4],[6,6,8],[2,1,1]]) => 4   // 1 -> 2 -> 6 -> 9
 *   longestIncreasingPath([[3,4,5],[3,2,6],[2,2,1]]) => 4   // 3 -> 4 -> 5 -> 6
 *   longestIncreasingPath([[1]])                     => 1
 * </pre>
 *
 * <p>Target Time: O(m * n), Space: O(m * n)
 *
 * <p>Hint 1: Use DFS with memoisation: memo[i][j] = length of the longest increasing path
 * starting at (i, j); recurse to neighbours only if their value is strictly greater.
 * <p>Hint 2: Alternatively, use topological sort (BFS from cells with no smaller
 * neighbours); the number of BFS levels equals the answer.
 */
public class LongestIncreasingPathInAMatrix {

    public int longestIncreasingPath(int[][] matrix) {
        throw new UnsupportedOperationException("implement me");
    }
}
