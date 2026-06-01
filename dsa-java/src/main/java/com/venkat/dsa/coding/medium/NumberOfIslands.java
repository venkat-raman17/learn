package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Number of Islands
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Graphs
 * <p>URL: https://leetcode.com/problems/number-of-islands/
 *
 * <p>Given an m x n 2D binary grid of '1's (land) and '0's (water), return the number of islands.
 * An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically.
 *
 * <p>Constraints:
 * <ul>
 *   <li>m == grid.length, n == grid[i].length</li>
 *   <li>1 <= m, n <= 300</li>
 *   <li>grid[i][j] is '0' or '1'</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 * Input: grid = [["1","1","1","1","0"],["1","1","0","1","0"],["1","1","0","0","0"],["0","0","0","0","0"]]
 * Output: 1
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 * Input: grid = [["1","1","0","0","0"],["1","1","0","0","0"],["0","0","1","0","0"],["0","0","0","1","1"]]
 * Output: 3
 * </pre>
 *
 * <p>Target: Time O(m*n), Space O(m*n)
 *
 * <p>Hint 1: Use DFS/BFS from each unvisited '1' cell and mark visited cells.
 * <p>Hint 2: Each DFS/BFS call that starts from a new '1' represents one island — count those calls.
 */
public class NumberOfIslands {

    public int numIslands(char[][] grid) {
        throw new UnsupportedOperationException("implement me");
    }
}
