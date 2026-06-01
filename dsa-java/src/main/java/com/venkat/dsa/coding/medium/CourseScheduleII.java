package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Course Schedule II
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Graphs
 * <p>URL: https://leetcode.com/problems/course-schedule-ii/
 *
 * <p>Given numCourses and prerequisite pairs, return the ordering of courses to finish all.
 * If impossible (cycle exists), return an empty array.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= numCourses <= 2000</li>
 *   <li>0 <= prerequisites.length <= numCourses * (numCourses - 1)</li>
 *   <li>prerequisites[i].length == 2</li>
 *   <li>0 <= a, b < numCourses, a != b</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 * Input: numCourses = 2, prerequisites = [[1,0]]
 * Output: [0,1]
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 * Input: numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
 * Output: [0,2,1,3] or [0,1,2,3]
 * </pre>
 *
 * <p>Target: Time O(V+E), Space O(V+E)
 *
 * <p>Hint 1: Topological sort — DFS post-order appends the course after all its dependencies are visited.
 * <p>Hint 2: Use three states (unvisited, visiting, visited) to detect back edges (cycles).
 */
public class CourseScheduleII {

    public int[] findOrder(int numCourses, int[][] prerequisites) {
        throw new UnsupportedOperationException("implement me");
    }
}
