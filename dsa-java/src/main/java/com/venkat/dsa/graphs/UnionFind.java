package com.venkat.dsa.graphs;

import java.util.Arrays;

/**
 * Union-Find (Disjoint Set Union) data structure over {@code n} elements indexed {@code 0..n-1}.
 *
 * <p><b>Definition:</b> A data structure that efficiently tracks a partition of a set into disjoint
 * subsets, supporting fast union and find operations.
 *
 * <p><b>Backing representation:</b> Two parallel integer arrays of length {@code n}:
 * <ul>
 *   <li>{@code parent[i]} – the parent of element {@code i} in its tree (root when {@code parent[i] == i})</li>
 *   <li>{@code rank[i]}   – upper bound on the height of the subtree rooted at {@code i} (used for union by rank)</li>
 * </ul>
 *
 * <p><b>Invariants:</b>
 * <ol>
 *   <li>Every element belongs to exactly one component.</li>
 *   <li>Path compression in {@code find} keeps trees nearly flat without changing logical membership.</li>
 *   <li>{@code rank[i]} never decreases and is only incremented when two trees of equal rank are merged.</li>
 *   <li>{@code components} equals the number of distinct roots.</li>
 * </ol>
 *
 * <p><b>Operations table:</b>
 * <pre>
 * Operation          Time (amortised)     Space
 * ─────────────────────────────────────────────
 * find(x)            O(α(n)) ≈ O(1)       O(1)
 * union(x, y)        O(α(n)) ≈ O(1)       O(1)
 * connected(x, y)    O(α(n)) ≈ O(1)       O(1)
 * count()            O(1)                 O(1)
 * Construction       O(n)                 O(n)
 * </pre>
 * where α is the inverse Ackermann function, effectively constant for all practical inputs.
 *
 * <p><b>When to use:</b> Problems involving dynamic connectivity, cycle detection in undirected graphs,
 * building minimum spanning trees (Kruskal's algorithm), grouping / clustering, and network connectivity queries.
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Always mention both optimisations: path compression + union by rank/size.</li>
 *   <li>Without either optimisation, find is O(n) worst case.</li>
 *   <li>Path compression alone gives O(log n) amortised; both together give O(α(n)).</li>
 *   <li>Union by rank preserves O(log n) tree height as an upper bound.</li>
 *   <li>This implementation is not thread-safe.</li>
 * </ul>
 */
public class UnionFind {

    private final int[] parent;
    private final int[] rank;
    private int components;

    /**
     * Initialises a Union-Find structure for {@code n} elements {@code 0..n-1},
     * each in its own singleton component.
     *
     * <p>Time: O(n) &nbsp; Space: O(n)
     *
     * @param n number of elements; must be &gt; 0
     * @throws IllegalArgumentException if {@code n} is not positive
     */
    public UnionFind(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive, got: " + n);
        }
        parent = new int[n];
        rank = new int[n];
        components = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i; // every element is its own root
        }
        // rank array is already zero-initialised by Java
    }

    /**
     * Returns the representative (root) of the component containing {@code x}.
     * Applies path compression: every node on the path to the root is made a direct child of the root.
     *
     * <p>Time: O(α(n)) amortised
     *
     * @param x element index
     * @return root of the component containing {@code x}
     * @throws IllegalArgumentException if {@code x} is out of range
     */
    public int find(int x) {
        validate(x);
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // path compression (recursive halving to root)
        }
        return parent[x];
    }

    /**
     * Merges the components containing {@code x} and {@code y}.
     * Uses union by rank to keep trees shallow.
     *
     * <p>Time: O(α(n)) amortised
     *
     * @param x first element
     * @param y second element
     * @return {@code true} if the two elements were in different components and a merge occurred;
     *         {@code false} if they were already connected
     * @throws IllegalArgumentException if {@code x} or {@code y} is out of range
     */
    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) {
            return false; // already in the same component
        }
        // Union by rank: attach smaller-rank tree under larger-rank tree
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            // Equal rank: pick rootX as new root and increment its rank
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        components--;
        return true;
    }

    /**
     * Returns {@code true} if {@code x} and {@code y} belong to the same component.
     *
     * <p>Time: O(α(n)) amortised
     *
     * @param x first element
     * @param y second element
     * @return {@code true} iff {@code find(x) == find(y)}
     * @throws IllegalArgumentException if {@code x} or {@code y} is out of range
     */
    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    /**
     * Returns the current number of disjoint components.
     *
     * <p>Time: O(1)
     *
     * @return number of components
     */
    public int count() {
        return components;
    }

    /**
     * Returns the total number of elements managed by this structure.
     *
     * <p>Time: O(1)
     *
     * @return {@code n} as passed to the constructor
     */
    public int size() {
        return parent.length;
    }

    // ── internal helpers ─────────────────────────────────────────────────────

    private void validate(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IllegalArgumentException(
                    "Index " + x + " out of range [0, " + (parent.length - 1) + "]");
        }
    }
}
