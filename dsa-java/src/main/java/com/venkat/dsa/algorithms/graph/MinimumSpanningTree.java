package com.venkat.dsa.algorithms.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Minimum Spanning Tree (MST) algorithms for undirected weighted graphs.
 *
 * <p><b>Definition:</b> A spanning tree of a connected graph is a subgraph that is a tree
 * connecting all vertices. The Minimum Spanning Tree is the spanning tree whose total
 * edge weight is minimised.</p>
 *
 * <p><b>Backing Representation:</b> Stateless utility class. Kruskal's uses a private
 * union-find (disjoint-set) structure. Prim's uses an adjacency list built from the
 * supplied edge array and a min-heap (PriorityQueue).</p>
 *
 * <p><b>Invariants:</b></p>
 * <ul>
 *   <li>The graph must be connected for an MST to exist; if it is not, both methods
 *       return the weight of the minimum spanning forest.</li>
 *   <li>Self-loops are ignored (u == v).</li>
 *   <li>Parallel edges are allowed; only the minimum-weight one will be chosen.</li>
 * </ul>
 *
 * <p><b>Operations table:</b></p>
 * <pre>
 * ┌─────────────────┬──────────────────────────┬───────────────┐
 * │ Operation       │ Time                     │ Space         │
 * ├─────────────────┼──────────────────────────┼───────────────┤
 * │ kruskal(n,e)    │ O(E log E) – sort edges  │ O(E + V)      │
 * │ prim(n,e)       │ O((V+E) log V) – heap    │ O(V + E)      │
 * └─────────────────┴──────────────────────────┴───────────────┘
 * where V = n vertices, E = edges.length
 * </pre>
 *
 * <p><b>When to use:</b></p>
 * <ul>
 *   <li>Kruskal's: sparse graphs (E ≈ V) – sort edges, add cheapest non-cycle edge.</li>
 *   <li>Prim's: dense graphs (E ≈ V²) – grow the tree one vertex at a time.</li>
 * </ul>
 *
 * <p><b>Interview notes:</b></p>
 * <ul>
 *   <li>Kruskal's requires a union-find; path compression + union by rank gives
 *       near-O(1) amortised per operation.</li>
 *   <li>Prim's with a Fibonacci heap achieves O(E + V log V) but PriorityQueue
 *       (binary heap) suffices for interviews.</li>
 *   <li>Both algorithms are greedy and proven correct via the cut property.</li>
 *   <li>MST is unique when all edge weights are distinct.</li>
 * </ul>
 */
public final class MinimumSpanningTree {

    private MinimumSpanningTree() {
        // utility class – no instances
    }

    // -----------------------------------------------------------------------
    // Kruskal's algorithm
    // -----------------------------------------------------------------------

    /**
     * Computes the total weight of the Minimum Spanning Tree (or forest) of an
     * undirected weighted graph using <b>Kruskal's algorithm</b>.
     *
     * <p>Algorithm: sort all edges by weight, then greedily add the cheapest edge
     * that does not form a cycle (detected via union-find).</p>
     *
     * <p><b>Time:</b> O(E log E) dominated by sorting.
     * <b>Space:</b> O(E + V) for the sorted copy and union-find arrays.</p>
     *
     * @param n     number of vertices (vertices are labelled 0 … n-1)
     * @param edges array of edges; each element is {@code {u, v, weight}}
     * @return total weight of the MST; 0 if {@code n <= 1} or {@code edges} is empty
     * @throws IllegalArgumentException if {@code n < 0}, or any edge references a
     *                                  vertex outside {@code [0, n-1]}, or any edge
     *                                  array does not have exactly 3 elements
     */
    public static long kruskal(int n, int[][] edges) {
        validateInputs(n, edges);
        if (n <= 1 || edges.length == 0) return 0L;

        // Sort edges by weight (ascending)
        int[][] sorted = edges.clone();
        Arrays.sort(sorted, Comparator.comparingInt(e -> e[2]));

        UnionFind uf = new UnionFind(n);
        long totalWeight = 0L;
        int edgesUsed = 0;

        for (int[] edge : sorted) {
            int u = edge[0], v = edge[1], w = edge[2];
            if (u == v) continue; // skip self-loops
            if (uf.union(u, v)) {
                totalWeight += w;
                edgesUsed++;
                if (edgesUsed == n - 1) break; // MST complete
            }
        }
        return totalWeight;
    }

    // -----------------------------------------------------------------------
    // Prim's algorithm
    // -----------------------------------------------------------------------

    /**
     * Computes the total weight of the Minimum Spanning Tree (or forest) of an
     * undirected weighted graph using <b>Prim's algorithm</b>.
     *
     * <p>Algorithm: start from vertex 0, maintain a min-heap of candidate edges
     * crossing the frontier, and greedily pick the minimum-weight edge that
     * connects a new vertex.</p>
     *
     * <p><b>Time:</b> O((V + E) log V) with a binary heap.
     * <b>Space:</b> O(V + E) for the adjacency list and heap.</p>
     *
     * @param n     number of vertices (vertices are labelled 0 … n-1)
     * @param edges array of edges; each element is {@code {u, v, weight}}
     * @return total weight of the MST; 0 if {@code n <= 1} or {@code edges} is empty
     * @throws IllegalArgumentException if {@code n < 0}, or any edge references a
     *                                  vertex outside {@code [0, n-1]}, or any edge
     *                                  array does not have exactly 3 elements
     */
    public static long prim(int n, int[][] edges) {
        validateInputs(n, edges);
        if (n <= 1 || edges.length == 0) return 0L;

        // Build adjacency list: vertex -> list of {neighbour, weight}
        List<List<int[]>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            if (u == v) continue; // skip self-loops
            adj.get(u).add(new int[]{v, w});
            adj.get(v).add(new int[]{u, w});
        }

        boolean[] inMST = new boolean[n];
        // min-heap: {weight, vertex}
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        long totalWeight = 0L;
        int verticesAdded = 0;

        // To handle disconnected graphs, try each component
        for (int start = 0; start < n; start++) {
            if (inMST[start]) continue;
            minHeap.offer(new int[]{0, start});

            while (!minHeap.isEmpty() && verticesAdded < n) {
                int[] curr = minHeap.poll();
                int weight = curr[0], u = curr[1];
                if (inMST[u]) continue;

                inMST[u] = true;
                totalWeight += weight;
                verticesAdded++;

                for (int[] neighbour : adj.get(u)) {
                    int v = neighbour[0], w = neighbour[1];
                    if (!inMST[v]) {
                        minHeap.offer(new int[]{w, v});
                    }
                }
            }
        }
        return totalWeight;
    }

    // -----------------------------------------------------------------------
    // Input validation
    // -----------------------------------------------------------------------

    private static void validateInputs(int n, int[][] edges) {
        if (n < 0) throw new IllegalArgumentException("n must be non-negative, got: " + n);
        if (edges == null) throw new IllegalArgumentException("edges must not be null");
        for (int[] edge : edges) {
            if (edge == null || edge.length != 3) {
                throw new IllegalArgumentException(
                        "Each edge must be an int[3] {u, v, weight}");
            }
            if (edge[0] < 0 || edge[0] >= n || edge[1] < 0 || edge[1] >= n) {
                throw new IllegalArgumentException(
                        "Edge vertex out of range [0, " + (n - 1) + "]: " +
                        Arrays.toString(edge));
            }
        }
    }

    // -----------------------------------------------------------------------
    // Private Union-Find (Disjoint Set Union) helper
    // -----------------------------------------------------------------------

    private static final class UnionFind {
        private final int[] parent;
        private final int[] rank;

        UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        /** Path-compressed find. */
        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        /**
         * Union by rank. Returns {@code true} if the two nodes were in different
         * components (i.e. the edge was added to the MST).
         */
        boolean union(int x, int y) {
            int rx = find(x), ry = find(y);
            if (rx == ry) return false;
            if (rank[rx] < rank[ry]) {
                parent[rx] = ry;
            } else if (rank[rx] > rank[ry]) {
                parent[ry] = rx;
            } else {
                parent[ry] = rx;
                rank[rx]++;
            }
            return true;
        }
    }
}
