package com.venkat.dsa.coding.medium.solutions;

import java.util.*;

/**
 * Walls and Gates (LeetCode 286)
 *
 * <p>Multi-source BFS starting simultaneously from all gates (cells with value 0). Each wave
 * propagates distance outward, filling every reachable empty room with the shortest distance to any
 * gate. Walls are skipped. Because BFS expands level-by-level, the first time a room is reached
 * its distance is already optimal.
 *
 * <p><b>Key insight:</b> Seeding BFS from all gates at once is equivalent to running Dijkstra from
 * each gate separately and taking the minimum — but runs in a single O(m*n) pass.
 *
 * <p><b>Time:</b> O(m * n).<br>
 * <b>Space:</b> O(m * n) for the queue.
 */
public class WallsAndGates {

    private static final int INF = Integer.MAX_VALUE;

    public void wallsAndGates(int[][] rooms) {
        if (rooms == null || rooms.length == 0) return;

        int rows = rooms.length, cols = rooms[0].length;
        Queue<int[]> queue = new ArrayDeque<>();

        // Seed: enqueue all gate positions
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (rooms[r][c] == 0) queue.offer(new int[]{r, c});
            }
        }

        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0], c = cell[1];
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                // Only update unvisited empty rooms
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && rooms[nr][nc] == INF) {
                    rooms[nr][nc] = rooms[r][c] + 1;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
    }
}
