package com.venkat.dsa.coding.medium.solutions;

import java.util.*;

/**
 * Course Schedule (LeetCode 207)
 *
 * <p>Models the prerequisites as a directed graph and detects a cycle using Kahn's algorithm
 * (topological sort via BFS). If all {@code numCourses} nodes are removed during the topological
 * sort (i.e., none remain in a cycle), all courses can be finished.
 *
 * <p><b>Key insight:</b> A valid course ordering exists if and only if the dependency graph is a
 * DAG (has no cycle). Kahn's algorithm detects this by checking whether the number of processed
 * nodes equals {@code numCourses}.
 *
 * <p><b>Time:</b> O(V + E) where V = numCourses and E = prerequisites.length.<br>
 * <b>Space:</b> O(V + E) for the adjacency list and in-degree array.
 */
public class CourseSchedule {

    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>();
        int[] inDegree = new int[numCourses];

        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());

        for (int[] pre : prerequisites) {
            adj.get(pre[1]).add(pre[0]); // pre[1] must come before pre[0]
            inDegree[pre[0]]++;
        }

        // Enqueue all nodes with no incoming edges (no prerequisites)
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) queue.offer(i);
        }

        int processed = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            processed++;
            for (int next : adj.get(course)) {
                // Reduce in-degree; if it drops to 0, the course is now schedulable
                if (--inDegree[next] == 0) queue.offer(next);
            }
        }

        return processed == numCourses; // true iff graph is a DAG (no cycle)
    }
}
