package com.venkat.dsa.coding.medium.solutions;

/**
 * Number of Connected Components in an Undirected Graph (LeetCode 323)
 *
 * <p>Uses Union-Find (Disjoint Set Union) with path compression and union-by-rank. Starts with
 * {@code n} components (one per node); each successful union of two previously separate components
 * decrements the count by one. The final count is the number of connected components.
 *
 * <p><b>Key insight:</b> Union-Find is the natural data structure for dynamic connectivity: each
 * union operation merges two components, and the total reduction from n gives the component count
 * directly without an explicit graph traversal.
 *
 * <p><b>Time:</b> O(n + e * alpha(n)) ≈ O(n + e).<br>
 * <b>Space:</b> O(n) for parent and rank arrays.
 */
public class NumberOfConnectedComponents {

    public int countComponents(int n, int[][] edges) {
        int[] parent = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        int components = n;
        for (int[] edge : edges) {
            int rootA = find(parent, edge[0]);
            int rootB = find(parent, edge[1]);
            if (rootA != rootB) {
                union(parent, rank, rootA, rootB);
                components--; // merge reduces component count by 1
            }
        }
        return components;
    }

    private int find(int[] parent, int x) {
        if (parent[x] != x) parent[x] = find(parent, parent[x]); // path compression
        return parent[x];
    }

    private void union(int[] parent, int[] rank, int a, int b) {
        if (rank[a] < rank[b]) { int tmp = a; a = b; b = tmp; }
        parent[b] = a;
        if (rank[a] == rank[b]) rank[a]++;
    }
}
