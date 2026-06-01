package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Walls and Gates
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Graphs
 * <p>URL: https://leetcode.com/problems/walls-and-gates/
 *
 * <p>Fill each empty room in an m x n grid with the distance to its nearest gate.
 * INF = 2147483647 means empty room, -1 means wall, 0 means gate.
 * If a room cannot be reached, leave it as INF.
 *
 * <p>Constraints:
 * <ul>
 *   <li>m == rooms.length, n == rooms[i].length</li>
 *   <li>1 <= m, n <= 250</li>
 *   <li>rooms[i][j] is -1, 0, or 2147483647</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 * Input: rooms = [[INF,-1,0,INF],[INF,INF,INF,-1],[INF,-1,INF,-1],[0,-1,INF,INF]]
 * Output: [[3,-1,0,1],[2,2,1,-1],[1,-1,2,-1],[0,-1,3,4]]
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 * Input: rooms = [[-1]]
 * Output: [[-1]]
 * </pre>
 *
 * <p>Target: Time O(m*n), Space O(m*n)
 *
 * <p>Hint 1: Multi-source BFS — enqueue ALL gates first, then expand outward level by level.
 * <p>Hint 2: When you reach an INF cell, assign the current distance and enqueue it; already-valued cells are skipped.
 */
public class WallsAndGates {

    public void wallsAndGates(int[][] rooms) {
        throw new UnsupportedOperationException("implement me");
    }
}
