package com.venkat.dsa.coding.medium.solutions;

import java.util.*;

/**
 * Rotting Oranges (LeetCode 994)
 *
 * <p>Multi-source BFS from all initially rotten oranges simultaneously. Each BFS level represents
 * one minute of spreading. After BFS completes, check if any fresh orange (value 1) remains; if
 * so, return -1, otherwise return the number of BFS levels elapsed.
 *
 * <p><b>Key insight:</b> Multi-source BFS guarantees that every fresh orange is reached by the
 * nearest rotten orange first, mirroring the simultaneous spreading semantics of the problem.
 *
 * <p><b>Time:</b> O(m * n) — each cell enqueued at most once.<br>
 * <b>Space:</b> O(m * n) for the BFS queue.
 */
public class RottingOranges {

    public int orangesRotting(int[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        Queue<int[]> queue = new ArrayDeque<>();
        int fresh = 0;

        // Collect initial state
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 2) queue.offer(new int[]{r, c});
                else if (grid[r][c] == 1) fresh++;
            }
        }

        if (fresh == 0) return 0; // no fresh oranges to rot

        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        int minutes = 0;

        while (!queue.isEmpty() && fresh > 0) {
            minutes++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] cell = queue.poll();
                int r = cell[0], c = cell[1];
                for (int[] d : dirs) {
                    int nr = r + d[0], nc = c + d[1];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == 1) {
                        grid[nr][nc] = 2; // rot the orange
                        fresh--;
                        queue.offer(new int[]{nr, nc});
                    }
                }
            }
        }

        return fresh == 0 ? minutes : -1;
    }
}
