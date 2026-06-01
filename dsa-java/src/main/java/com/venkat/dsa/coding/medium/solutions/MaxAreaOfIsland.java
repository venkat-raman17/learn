package com.venkat.dsa.coding.medium.solutions;

import java.util.*;

/**
 * Max Area of Island (LeetCode 695)
 *
 * <p>Iterates every cell; when a '1' is found, performs a DFS flood-fill that marks cells visited
 * (setting them to 0) while accumulating the area. Tracks the global maximum area seen.
 *
 * <p><b>Key insight:</b> Sinking each cell to 0 during DFS prevents double-counting, so the area
 * counter incremented per visited cell gives the exact island size.
 *
 * <p><b>Time:</b> O(m * n) — each cell processed at most once.<br>
 * <b>Space:</b> O(m * n) worst-case recursion stack (all land).
 */
public class MaxAreaOfIsland {

    public int maxAreaOfIsland(int[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        int rows = grid.length, cols = grid[0].length;
        int maxArea = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 1) {
                    maxArea = Math.max(maxArea, dfs(grid, r, c, rows, cols));
                }
            }
        }
        return maxArea;
    }

    private int dfs(int[][] grid, int r, int c, int rows, int cols) {
        // Boundary or water check
        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] == 0) return 0;

        grid[r][c] = 0; // sink to mark visited
        // Sum 1 (current cell) + all four directional expansions
        return 1
                + dfs(grid, r + 1, c, rows, cols)
                + dfs(grid, r - 1, c, rows, cols)
                + dfs(grid, r, c + 1, rows, cols)
                + dfs(grid, r, c - 1, rows, cols);
    }
}
