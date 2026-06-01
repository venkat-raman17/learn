package com.venkat.dsa.coding.medium.solutions;

import java.util.*;

/**
 * Number of Islands (LeetCode 200)
 *
 * <p>Iterates through every cell; when a '1' is found, increments the island count and runs a
 * BFS/DFS flood-fill that marks all connected '1' cells as visited by overwriting them with '0'.
 * This avoids a separate visited array and keeps space usage proportional to the recursion depth
 * (DFS) or queue size (BFS).
 *
 * <p><b>Key insight:</b> Sinking each island in-place guarantees every land cell is counted exactly
 * once, so a single linear scan suffices.
 *
 * <p><b>Time:</b> O(m * n) — each cell is visited at most once.<br>
 * <b>Space:</b> O(min(m, n)) average BFS queue; O(m * n) worst case (all land, fully connected).
 */
public class NumberOfIslands {

    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        int rows = grid.length, cols = grid[0].length;
        int count = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == '1') {
                    count++;
                    bfs(grid, r, c, rows, cols);
                }
            }
        }
        return count;
    }

    private void bfs(char[][] grid, int startR, int startC, int rows, int cols) {
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{startR, startC});
        grid[startR][startC] = '0'; // mark visited immediately

        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0], c = cell[1];
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == '1') {
                    grid[nr][nc] = '0'; // sink before enqueuing to avoid duplicate visits
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
    }
}
