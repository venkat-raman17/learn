package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Rotting Oranges
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Graphs
 * <p>URL: https://leetcode.com/problems/rotting-oranges/
 *
 * <p>In a grid, 0 = empty, 1 = fresh orange, 2 = rotten orange. Each minute, rotten oranges spread
 * to adjacent fresh oranges (4-directionally). Return the minimum minutes until no fresh oranges remain,
 * or -1 if impossible.
 *
 * <p>Constraints:
 * <ul>
 *   <li>m == grid.length, n == grid[i].length</li>
 *   <li>1 <= m, n <= 10</li>
 *   <li>grid[i][j] is 0, 1, or 2</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 * Input: grid = [[2,1,1],[1,1,0],[0,1,1]]
 * Output: 4
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 * Input: grid = [[2,1,1],[0,1,1],[1,0,1]]
 * Output: -1
 * </pre>
 *
 * <p>Target: Time O(m*n), Space O(m*n)
 *
 * <p>Hint 1: Multi-source BFS starting from all rotten oranges simultaneously.
 * <p>Hint 2: Track fresh orange count; after BFS, if any fresh remain return -1, else return elapsed minutes.
 */
public class RottingOranges {

    public int orangesRotting(int[][] grid) {
        throw new UnsupportedOperationException("implement me");
    }
}
