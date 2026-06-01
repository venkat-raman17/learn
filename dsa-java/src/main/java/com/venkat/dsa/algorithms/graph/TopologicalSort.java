package com.venkat.dsa.algorithms.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Topological Sort — linear ordering of vertices in a Directed Acyclic Graph (DAG)
 * such that for every directed edge u → v, vertex u appears before v.
 *
 * <p><b>Backing representation:</b> Adjacency list ({@code List<List<Integer>>}) built
 * internally from the supplied edge list; per-vertex in-degree array for Kahn's algorithm.
 *
 * <p><b>Invariant(s):</b>
 * <ul>
 *   <li>Input graph must be a DAG. Any cycle causes {@link IllegalArgumentException}.</li>
 *   <li>Vertices are labelled {@code 0 … n-1}.</li>
 * </ul>
 *
 * <p><b>Operations:</b>
 * <pre>
 * ┌──────────────────────────┬────────────┬───────────┐
 * │ Operation                │ Time       │ Space     │
 * ├──────────────────────────┼────────────┼───────────┤
 * │ kahn(n, edges)           │ O(V + E)   │ O(V + E)  │
 * └──────────────────────────┴────────────┴───────────┘
 * </pre>
 *
 * <p><b>When to use:</b> Task scheduling, build dependency resolution, course prerequisite
 * ordering — any problem that requires processing nodes only after all their dependencies
 * have been processed in a DAG.
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Two canonical approaches: Kahn's BFS (in-degree) and DFS post-order (reverse finish time).</li>
 *   <li>Kahn's naturally detects cycles: if the result list size &lt; n, a cycle exists.</li>
 *   <li>Multiple valid orderings may exist; any is acceptable unless the problem specifies
 *       lexicographic smallest (use a {@link java.util.PriorityQueue} instead of a plain queue).</li>
 *   <li>Time complexity is O(V + E) — each vertex and edge is visited exactly once.</li>
 * </ul>
 */
public final class TopologicalSort {

    /** Utility class — no instances. */
    private TopologicalSort() {}

    /**
     * Returns a topological ordering of vertices {@code 0 … n-1} using Kahn's algorithm
     * (BFS / in-degree reduction).
     *
     * <p>Algorithm outline:
     * <ol>
     *   <li>Build adjacency list and compute in-degree for every vertex. O(V + E)</li>
     *   <li>Seed a queue with all vertices whose in-degree is 0. O(V)</li>
     *   <li>While the queue is non-empty: dequeue a vertex, append it to the result,
     *       decrement the in-degree of each neighbour; enqueue neighbours whose in-degree
     *       reaches 0. O(V + E)</li>
     *   <li>If the result contains fewer than {@code n} vertices, a cycle is present. O(1)</li>
     * </ol>
     *
     * <p><b>Big-O:</b> Time O(V + E), Space O(V + E).
     *
     * @param n     number of vertices (labelled {@code 0} to {@code n-1}); must be &ge; 0
     * @param edges directed edges as {@code int[]{u, v}} meaning u → v;
     *              each endpoint must be in {@code [0, n)}
     * @return an unmodifiable list of vertices in a valid topological order
     * @throws IllegalArgumentException if {@code n < 0}, if any endpoint is out of range,
     *                                  or if the graph contains a cycle
     */
    public static List<Integer> kahn(int n, List<int[]> edges) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0, got: " + n);
        }
        if (edges == null) {
            throw new IllegalArgumentException("edges list must not be null");
        }

        // Build adjacency list and in-degree array
        List<List<Integer>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        int[] inDegree = new int[n];

        for (int[] edge : edges) {
            if (edge == null || edge.length < 2) {
                throw new IllegalArgumentException("Each edge must be a non-null int[2]");
            }
            int u = edge[0], v = edge[1];
            if (u < 0 || u >= n || v < 0 || v >= n) {
                throw new IllegalArgumentException(
                        "Edge endpoint out of range [0, " + n + "): " + u + " -> " + v);
            }
            adj.get(u).add(v);
            inDegree[v]++;
        }

        // Seed queue with zero-in-degree vertices
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<Integer> order = new ArrayList<>(n);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);
            for (int v : adj.get(u)) {
                if (--inDegree[v] == 0) {
                    queue.offer(v);
                }
            }
        }

        // Fewer than n vertices processed → cycle detected
        if (order.size() < n) {
            throw new IllegalArgumentException(
                    "Graph contains a cycle; topological sort is not possible.");
        }

        return List.copyOf(order);
    }
}
