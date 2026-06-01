package com.venkat.dsa.coding.medium.solutions;

import java.util.*;

/**
 * Graph Valid Tree (LeetCode 261)
 *
 * <p>A graph on {@code n} nodes is a valid tree if and only if it has exactly {@code n-1} edges
 * and is fully connected (no isolated components). Uses Union-Find (Disjoint Set Union) to process
 * each edge: if both endpoints are already in the same component, adding the edge creates a cycle.
 * Finally checks that a single component covers all nodes.
 *
 * <p><b>Key insight:</b> The two tree conditions (n-1 edges, connected) map directly to the
 * Union-Find invariants: each successful union reduces the component count by 1; a redundant edge
 * means the graph has a cycle and cannot be a tree.
 *
 * <p><b>Time:</b> O(n * alpha(n)) ≈ O(n) with path compression and union-by-rank.<br>
 * <b>Space:</b> O(n) for parent and rank arrays.
 */
public class GraphValidTree {

    public boolean validTree(int n, int[][] edges) {
        // A tree must have exactly n-1 edges
        if (edges.length != n - 1) return false;

        int[] parent = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        for (int[] edge : edges) {
            int rootA = find(parent, edge[0]);
            int rootB = find(parent, edge[1]);
            if (rootA == rootB) return false; // cycle detected
            union(parent, rank, rootA, rootB);
        }

        return true; // n-1 edges and no cycle => connected tree
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
