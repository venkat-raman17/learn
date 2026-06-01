package com.venkat.dsa.coding.hard.solutions;

import java.util.*;

/**
 * Swim in Rising Water (LeetCode 778)
 *
 * <p>Models the problem as finding the path from (0,0) to (n-1,n-1) that minimises the maximum
 * cell value encountered. Uses a modified Dijkstra where the "distance" to a cell is the maximum
 * grid value on the best path to that cell (min-bottleneck path). A min-heap ordered by this
 * bottleneck value gives the optimal greedy expansion.
 *
 * <p><b>Key insight:</b> The answer is the minimum over all paths of the maximum elevation on
 * that path — i.e., the minimax path. Dijkstra with relaxation rule
 * {@code dist[v] = min(dist[v], max(dist[u], grid[v]))} solves this exactly.
 *
 * <p><b>Time:</b> O(N^2 log N) — each cell processed once via the heap.<br>
 * <b>Space:</b> O(N^2) for the distance and visited arrays.
 */
public class SwimInRisingWater {

    private static final int[][] DIRS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    public int swimInWater(int[][] grid) {
        int n = grid.length;
        int[][] dist = new int[n][n];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        dist[0][0] = grid[0][0];

        // Min-heap: (bottleneck_value, row, col)
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{grid[0][0], 0, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int d = cur[0], r = cur[1], c = cur[2];

            if (d > dist[r][c]) continue; // Stale entry
            if (r == n - 1 && c == n - 1) return d; // Reached destination

            for (int[] dir : DIRS) {
                int nr = r + dir[0], nc = c + dir[1];
                if (nr < 0 || nr >= n || nc < 0 || nc >= n) continue;
                // Bottleneck on this path = max of current bottleneck and next cell's elevation
                int newDist = Math.max(d, grid[nr][nc]);
                if (newDist < dist[nr][nc]) {
                    dist[nr][nc] = newDist;
                    pq.offer(new int[]{newDist, nr, nc});
                }
            }
        }

        return dist[n - 1][n - 1];
    }
}
