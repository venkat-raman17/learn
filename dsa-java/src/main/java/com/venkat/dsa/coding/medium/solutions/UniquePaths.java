package com.venkat.dsa.coding.medium.solutions;

/**
 * Unique Paths (LeetCode 62)
 *
 * Approach: 2-D DP where dp[i][j] = number of ways to reach cell (i,j).
 * Every cell in the first row or first column has exactly 1 path (only one direction
 * to travel from the start). For all other cells, dp[i][j] = dp[i-1][j] + dp[i][j-1].
 * We can reduce space to O(n) by keeping only one row.
 *
 * Time:  O(m * n)
 * Space: O(n) — single-row rolling array
 *
 * Key insight: the number of paths to any cell equals the sum of paths from the cell
 * above and the cell to the left, because those are the only two ways to arrive.
 */
public class UniquePaths {

    public int uniquePaths(int m, int n) {
        // dp[j] = number of unique paths to reach the current row, column j
        int[] dp = new int[n];
        // First row: only one path to each cell (move right only)
        java.util.Arrays.fill(dp, 1);

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                // dp[j] already holds "from above"; add dp[j-1] for "from left"
                dp[j] += dp[j - 1];
            }
        }
        return dp[n - 1];
    }
}
