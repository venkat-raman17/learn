package com.venkat.dsa.coding.medium.solutions;

import java.util.*;

/**
 * Surrounded Regions (LeetCode 130)
 *
 * <p>Instead of finding captured regions directly, marks "safe" 'O' cells first: BFS/DFS from
 * every 'O' on the border, temporarily marking reachable 'O' cells as '#'. Then a single pass
 * converts remaining 'O' (surrounded) to 'X' and '#' (safe) back to 'O'.
 *
 * <p><b>Key insight:</b> Any 'O' connected to a border 'O' cannot be captured. Marking border-
 * connected 'O' cells with a sentinel flips the problem from "find surrounded" to "find safe".
 *
 * <p><b>Time:</b> O(m * n).<br>
 * <b>Space:</b> O(m * n) for the BFS queue in the worst case.
 */
public class SurroundedRegions {

    public void solve(char[][] board) {
        if (board == null || board.length == 0) return;

        int rows = board.length, cols = board[0].length;

        // Flood-fill from all border 'O' cells, marking them safe with '#'
        for (int r = 0; r < rows; r++) {
            if (board[r][0] == 'O') bfs(board, r, 0, rows, cols);
            if (board[r][cols - 1] == 'O') bfs(board, r, cols - 1, rows, cols);
        }
        for (int c = 0; c < cols; c++) {
            if (board[0][c] == 'O') bfs(board, 0, c, rows, cols);
            if (board[rows - 1][c] == 'O') bfs(board, rows - 1, c, rows, cols);
        }

        // Finalize: 'O' -> 'X' (captured), '#' -> 'O' (safe restore)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == 'O') board[r][c] = 'X';
                else if (board[r][c] == '#') board[r][c] = 'O';
            }
        }
    }

    private void bfs(char[][] board, int startR, int startC, int rows, int cols) {
        Queue<int[]> queue = new ArrayDeque<>();
        board[startR][startC] = '#';
        queue.offer(new int[]{startR, startC});

        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0], c = cell[1];
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && board[nr][nc] == 'O') {
                    board[nr][nc] = '#';
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
    }
}
