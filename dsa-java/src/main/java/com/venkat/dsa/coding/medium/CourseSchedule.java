package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Course Schedule
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Graphs
 * <p>URL: https://leetcode.com/problems/course-schedule/
 *
 * <p>Given numCourses and a list of prerequisite pairs [a, b] meaning b must be taken before a,
 * return true if it is possible to finish all courses (i.e., no cycle exists in the dependency graph).
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= numCourses <= 2000</li>
 *   <li>0 <= prerequisites.length <= 5000</li>
 *   <li>prerequisites[i].length == 2</li>
 *   <li>0 <= a, b < numCourses, a != b</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 * Input: numCourses = 2, prerequisites = [[1,0]]
 * Output: true
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 * Input: numCourses = 2, prerequisites = [[1,0],[0,1]]
 * Output: false
 * </pre>
 *
 * <p>Target: Time O(V+E), Space O(V+E)
 *
 * <p>Hint 1: Model as a directed graph and detect if a cycle exists using DFS with a "visiting" state set.
 * <p>Hint 2: Alternatively use Kahn's algorithm (topological sort via BFS with in-degree tracking).
 */
public class CourseSchedule {

    public boolean canFinish(int numCourses, int[][] prerequisites) {
        throw new UnsupportedOperationException("implement me");
    }
}
