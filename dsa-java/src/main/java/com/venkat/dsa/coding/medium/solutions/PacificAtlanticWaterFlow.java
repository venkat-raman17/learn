package com.venkat.dsa.coding.medium.solutions;

import java.util.*;

/**
 * Pacific Atlantic Water Flow (LeetCode 417)
 *
 * <p>Instead of simulating water flow downhill from each cell (expensive), reverses the direction:
 * performs two multi-source BFS/DFS passes — one from all Pacific-border cells and one from all
 * Atlantic-border cells — moving to neighbors with equal or greater height. The answer is every
 * cell reachable in both passes.
 *
 * <p><b>Key insight:</b> Reversing the flow direction (going uphill from the ocean) avoids the
 * O(m*n) per-cell simulation and reduces the problem to two standard graph reachability queries.
 *
 * <p><b>Time:</b> O(m * n) — each cell visited at most twice.<br>
 * <b>Space:</b> O(m * n) for the two visited arrays and queues.
 */
public class PacificAtlanticWaterFlow {

    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        int rows = heights.length, cols = heights[0].length;
        boolean[][] pacific = new boolean[rows][cols];
        boolean[][] atlantic = new boolean[rows][cols];

        Queue<int[]> pacQueue = new ArrayDeque<>();
        Queue<int[]> atlQueue = new ArrayDeque<>();

        // Seed border cells
        for (int r = 0; r < rows; r++) {
            pacQueue.offer(new int[]{r, 0});
            pacific[r][0] = true;
            atlQueue.offer(new int[]{r, cols - 1});
            atlantic[r][cols - 1] = true;
        }
        for (int c = 0; c < cols; c++) {
            pacQueue.offer(new int[]{0, c});
            pacific[0][c] = true;
            atlQueue.offer(new int[]{rows - 1, c});
            atlantic[rows - 1][c] = true;
        }

        bfs(heights, pacQueue, pacific, rows, cols);
        bfs(heights, atlQueue, atlantic, rows, cols);

        List<List<Integer>> result = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (pacific[r][c] && atlantic[r][c]) {
                    result.add(Arrays.asList(r, c));
                }
            }
        }
        return result;
    }

    private void bfs(int[][] heights, Queue<int[]> queue, boolean[][] visited, int rows, int cols) {
        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0], c = cell[1];
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                // Move to neighbor only if within bounds, not yet visited, and height >= current
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                        && !visited[nr][nc]
                        && heights[nr][nc] >= heights[r][c]) {
                    visited[nr][nc] = true;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
    }
}
