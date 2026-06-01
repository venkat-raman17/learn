package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — Pacific Atlantic Water Flow
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Graphs
 * <p>URL: https://leetcode.com/problems/pacific-atlantic-water-flow/
 *
 * <p>Given an m x n island, rain water can flow to the Pacific (top/left) or Atlantic (bottom/right) ocean
 * if it can reach the border. Return all cells from which water can flow to both oceans.
 * Water flows from high to equal or lower elevation.
 *
 * <p>Constraints:
 * <ul>
 *   <li>m == heights.length, n == heights[i].length</li>
 *   <li>1 <= m, n <= 200</li>
 *   <li>0 <= heights[i][j] <= 10^5</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 * Input: heights = [[1,2,2,3,5],[3,2,3,4,4],[2,4,5,3,1],[6,7,1,4,5],[5,1,1,2,4]]
 * Output: [[0,4],[1,3],[1,4],[2,2],[3,0],[3,1],[4,0]]
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 * Input: heights = [[1]]
 * Output: [[0,0]]
 * </pre>
 *
 * <p>Target: Time O(m*n), Space O(m*n)
 *
 * <p>Hint 1: Reverse BFS/DFS — start from ocean borders and flow "uphill" (to equal or higher elevation).
 * <p>Hint 2: Find the intersection of cells reachable from Pacific borders and those reachable from Atlantic borders.
 */
public class PacificAtlanticWaterFlow {

    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        throw new UnsupportedOperationException("implement me");
    }
}
