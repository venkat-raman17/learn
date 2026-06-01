package com.venkat.dsa.coding.medium.solutions;

import java.util.*;

/**
 * Course Schedule II (LeetCode 210)
 *
 * <p>Performs a topological sort using Kahn's BFS algorithm. Courses with zero in-degree are
 * processed first; as each course is dequeued, it is appended to the result order and its
 * successors' in-degrees are decremented. If all courses are included in the result, the order is
 * valid; otherwise a cycle exists and an empty array is returned.
 *
 * <p><b>Key insight:</b> Kahn's algorithm naturally produces a topological order as a side effect
 * of cycle detection — the dequeue order is the valid course schedule.
 *
 * <p><b>Time:</b> O(V + E).<br>
 * <b>Space:</b> O(V + E) for the adjacency list, in-degree array, and result.
 */
public class CourseScheduleII {

    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>();
        int[] inDegree = new int[numCourses];

        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());

        for (int[] pre : prerequisites) {
            adj.get(pre[1]).add(pre[0]);
            inDegree[pre[0]]++;
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) queue.offer(i);
        }

        int[] order = new int[numCourses];
        int idx = 0;

        while (!queue.isEmpty()) {
            int course = queue.poll();
            order[idx++] = course;
            for (int next : adj.get(course)) {
                if (--inDegree[next] == 0) queue.offer(next);
            }
        }

        // If not all courses are in the order, a cycle exists
        return idx == numCourses ? order : new int[0];
    }
}
