package com.venkat.dsa.coding.medium.solutions;

/**
 * Redundant Connection (LeetCode 684)
 *
 * <p>Processes edges in order using Union-Find. The first edge whose two endpoints already share
 * the same root (i.e., are already connected) is the redundant edge that creates a cycle and is
 * therefore the answer. The problem guarantees exactly one such edge.
 *
 * <p><b>Key insight:</b> In an undirected graph that forms a tree plus one extra edge, the extra
 * edge is the first one encountered (in input order) that connects two already-connected nodes —
 * Union-Find detects this in near-constant time per edge.
 *
 * <p><b>Time:</b> O(n * alpha(n)) ≈ O(n).<br>
 * <b>Space:</b> O(n) for parent and rank arrays.
 */
public class RedundantConnection {

    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;
        int[] parent = new int[n + 1]; // nodes are 1-indexed
        int[] rank = new int[n + 1];
        for (int i = 1; i <= n; i++) parent[i] = i;

        for (int[] edge : edges) {
            int rootA = find(parent, edge[0]);
            int rootB = find(parent, edge[1]);
            if (rootA == rootB) return edge; // adding this edge creates a cycle
            union(parent, rank, rootA, rootB);
        }

        return new int[0]; // unreachable per problem constraints
    }

    private int find(int[] parent, int x) {
        if (parent[x] != x) parent[x] = find(parent, parent[x]);
        return parent[x];
    }

    private void union(int[] parent, int[] rank, int a, int b) {
        if (rank[a] < rank[b]) { int tmp = a; a = b; b = tmp; }
        parent[b] = a;
        if (rank[a] == rank[b]) rank[a]++;
    }
}
