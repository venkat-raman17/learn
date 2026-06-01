package com.venkat.dsa.algorithms.graph;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Dijkstra's Shortest Path Algorithm.
 *
 * <p><b>Definition:</b> Finds the shortest path from a single source vertex to all
 * other vertices in a weighted directed graph with non-negative edge weights.
 *
 * <p><b>Backing Representation:</b> Adjacency list represented as an array of
 * {@code int[][]} lists built from the edge input. A min-heap ({@link PriorityQueue})
 * drives the greedy relaxation loop.
 *
 * <p><b>Invariant(s):</b>
 * <ul>
 *   <li>All edge weights must be &gt;= 0 (negative weights break correctness).</li>
 *   <li>Once a vertex is extracted from the priority queue with a settled distance,
 *       that distance is final.</li>
 *   <li>Unreachable vertices retain {@code Integer.MAX_VALUE} in the result array.</li>
 * </ul>
 *
 * <p><b>Operations:</b>
 * <pre>
 * ┌───────────────────┬──────────────────────┬────────────┐
 * │ Operation         │ Time                 │ Space      │
 * ├───────────────────┼──────────────────────┼────────────┤
 * │ shortestPath      │ O((V + E) log V)     │ O(V + E)   │
 * │   - build adj     │ O(V + E)             │ O(V + E)   │
 * │   - PQ operations │ O(E log V)           │ O(V)       │
 * └───────────────────┴──────────────────────┴────────────┘
 * V = number of vertices, E = number of edges.
 * </pre>
 *
 * <p><b>When to use:</b> Single-source shortest paths on sparse graphs with
 * non-negative weights. Prefer Bellman-Ford when negative weights are possible;
 * prefer BFS (unweighted) or A* (heuristic-guided) for other scenarios.
 *
 * <p><b>Interview Notes:</b>
 * <ul>
 *   <li>Classic greedy algorithm — always relaxes the currently cheapest unvisited node.</li>
 *   <li>Binary-heap PQ gives O((V+E) log V); Fibonacci heap gives O(E + V log V) but
 *       is rarely used in practice due to constant-factor overhead.</li>
 *   <li>The "lazy deletion" pattern used here allows duplicate PQ entries; a visited
 *       array (or distance check) skips already-settled nodes.</li>
 *   <li>Cannot handle negative-weight edges — use Bellman-Ford or SPFA instead.</li>
 *   <li>For dense graphs consider an O(V²) array-based implementation.</li>
 * </ul>
 */
public final class Dijkstra {

    private Dijkstra() {
        // utility class — no instances
    }

    /**
     * Computes shortest distances from {@code source} to every vertex in a weighted
     * directed graph.
     *
     * <p>Time: O((V + E) log V) using a binary-heap priority queue.<br>
     * Space: O(V + E) for the adjacency list and O(V) for the distance array and PQ.
     *
     * @param n      number of vertices, labelled 0 .. n-1
     * @param edges  array of directed edges where each element is {u, v, w}:
     *               u is the source vertex, v is the destination vertex,
     *               and w is the non-negative weight
     * @param source the starting vertex (0-indexed)
     * @return an {@code int[]} of length {@code n} where {@code result[i]} is the
     *         shortest distance from {@code source} to vertex {@code i}, or
     *         {@link Integer#MAX_VALUE} if vertex {@code i} is unreachable
     * @throws IllegalArgumentException if {@code n <= 0}, if {@code source} is out of
     *                                  range [0, n-1], if any edge array does not have
     *                                  exactly three elements, or if any edge weight is
     *                                  negative
     */
    @SuppressWarnings("unchecked")
    public static int[] shortestPath(int n, int[][] edges, int source) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive, got: " + n);
        }
        if (source < 0 || source >= n) {
            throw new IllegalArgumentException(
                    "source " + source + " out of range [0, " + (n - 1) + "]");
        }

        // Build adjacency list: adj[u] holds int[] {v, w} entries
        java.util.List<int[]>[] adj = new java.util.ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new java.util.ArrayList<>();
        }

        if (edges != null) {
            for (int[] edge : edges) {
                if (edge == null || edge.length != 3) {
                    throw new IllegalArgumentException(
                            "Each edge must be an int[3] {u, v, w}");
                }
                int u = edge[0], v = edge[1], w = edge[2];
                if (u < 0 || u >= n || v < 0 || v >= n) {
                    throw new IllegalArgumentException(
                            "Edge vertex out of range: {" + u + ", " + v + ", " + w + "}");
                }
                if (w < 0) {
                    throw new IllegalArgumentException(
                            "Negative edge weight not supported: " + w);
                }
                adj[u].add(new int[]{v, w});
            }
        }

        // Distance array — initialise to MAX_VALUE (unreachable)
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        // Min-heap ordered by distance: int[] {distance, vertex}
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                java.util.Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, source});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int d = current[0];
            int u = current[1];

            // Lazy deletion: skip if we have already found a better path
            if (d > dist[u]) {
                continue;
            }

            for (int[] neighbour : adj[u]) {
                int v = neighbour[0];
                int w = neighbour[1];

                // Overflow-safe relaxation: dist[u] != MAX_VALUE here (it equals d)
                long newDist = (long) dist[u] + w;
                if (newDist < dist[v]) {
                    dist[v] = (int) newDist;
                    pq.offer(new int[]{dist[v], v});
                }
            }
        }

        return dist;
    }
}
