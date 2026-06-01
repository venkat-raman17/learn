package com.venkat.dsa.coding.hard.solutions;

/**
 * Longest Increasing Path in a Matrix (LeetCode 329)
 *
 * Approach: DFS with memoization (top-down DP). For each cell we DFS to all 4
 * neighbors that have a strictly greater value, returning 1 + max(neighbor paths).
 * Results are cached in a memo array to ensure each cell is computed only once.
 * Since the path must be strictly increasing, there are no cycles, so no visited
 * set is needed.
 *
 * Time:  O(m * n)  — each cell computed once
 * Space: O(m * n)  — memo array + recursion stack
 *
 * Key insight: the DAG structure (only increasing edges) guarantees that memoized
 * DFS correctly computes the longest path without cycle detection.
 */
public class LongestIncreasingPathInAMatrix {

    private static final int[][] DIRS = {{0,1},{0,-1},{1,0},{-1,0}};

    public int longestIncreasingPath(int[][] matrix) {
        if (matrix == null || matrix.length == 0) return 0;
        int m = matrix.length, n = matrix[0].length;
        int[][] memo = new int[m][n]; // 0 = unvisited
        int result = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result = Math.max(result, dfs(matrix, i, j, m, n, memo));
            }
        }
        return result;
    }

    private int dfs(int[][] matrix, int r, int c, int m, int n, int[][] memo) {
        if (memo[r][c] != 0) return memo[r][c]; // already computed

        int best = 1; // minimum path length is the cell itself
        for (int[] d : DIRS) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < m && nc >= 0 && nc < n
                    && matrix[nr][nc] > matrix[r][c]) { // strictly increasing
                best = Math.max(best, 1 + dfs(matrix, nr, nc, m, n, memo));
            }
        }
        memo[r][c] = best;
        return best;
    }
}
