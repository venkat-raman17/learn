package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Unique Paths
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 2-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/unique-paths/
 *
 * <p>A robot starts at the top-left corner of an m x n grid and wants to reach the
 * bottom-right corner. It can only move right or down. Count the number of unique paths.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= m, n <= 100</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   uniquePaths(3, 7) => 28
 *   uniquePaths(3, 2) => 3
 * </pre>
 *
 * <p>Target Time: O(m * n), Space: O(m * n) or O(n)
 *
 * <p>Hint 1: Use a 2-D DP table where dp[i][j] = dp[i-1][j] + dp[i][j-1].
 * <p>Hint 2: Optimise to a single 1-D row since you only need the row above.
 */
public class UniquePaths {

    public int uniquePaths(int m, int n) {
        throw new UnsupportedOperationException("implement me");
    }
}
