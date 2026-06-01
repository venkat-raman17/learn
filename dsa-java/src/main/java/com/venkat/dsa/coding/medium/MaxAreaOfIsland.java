package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Max Area of Island
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Graphs
 * <p>URL: https://leetcode.com/problems/max-area-of-island/
 *
 * <p>Given an m x n binary matrix grid, return the maximum area of an island in grid.
 * An island is formed by connecting adjacent 1s horizontally or vertically. The area is the number of cells.
 *
 * <p>Constraints:
 * <ul>
 *   <li>m == grid.length, n == grid[i].length</li>
 *   <li>1 <= m, n <= 50</li>
 *   <li>grid[i][j] is 0 or 1</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 * Input: grid = [[0,0,1,0,0,0,0,1,0,0,0,0,0],[0,0,0,0,0,0,0,1,1,1,0,0,0],[0,1,1,0,1,0,0,0,0,0,0,0,0],...]
 * Output: 6
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 * Input: grid = [[0,0,0,0,0,0,0,0]]
 * Output: 0
 * </pre>
 *
 * <p>Target: Time O(m*n), Space O(m*n)
 *
 * <p>Hint 1: DFS from each unvisited 1 cell, returning the size of the island found.
 * <p>Hint 2: Mark cells as visited (set to 0) during DFS to avoid revisiting.
 */
public class MaxAreaOfIsland {

    public int maxAreaOfIsland(int[][] grid) {
        throw new UnsupportedOperationException("implement me");
    }
}
