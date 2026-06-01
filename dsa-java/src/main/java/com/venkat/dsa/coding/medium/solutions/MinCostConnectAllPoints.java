package com.venkat.dsa.coding.medium.solutions;

import java.util.*;

/**
 * Min Cost to Connect All Points (LeetCode 1584)
 *
 * <p>Applies Prim's algorithm (lazy variant) to find the Minimum Spanning Tree of the complete
 * graph where the edge weight between two points is their Manhattan distance. Starting from point 0,
 * we greedily pick the cheapest edge crossing the cut between visited and unvisited nodes.
 *
 * <p><b>Key insight:</b> In a dense complete graph Prim's with an adjacency-list-style priority
 * queue runs in O(N^2 log N), which is acceptable; alternatively an O(N^2) array-based Prim's
 * avoids the log factor but the PQ version is cleaner.
 *
 * <p><b>Time:</b> O(N^2 log N) — every edge is enqueued at most once.<br>
 * <b>Space:</b> O(N^2) worst-case heap entries.
 */
public class MinCostConnectAllPoints {

    public int minCostConnectPoints(int[][] points) {
        int n = points.length;
        boolean[] inMST = new boolean[n];
        // Min-heap: (cost, pointIndex)
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, 0}); // Start from point 0 with cost 0

        int totalCost = 0;
        int edgesUsed = 0;

        while (!pq.isEmpty() && edgesUsed < n) {
            int[] cur = pq.poll();
            int cost = cur[0], u = cur[1];

            if (inMST[u]) continue; // Already in MST
            inMST[u] = true;
            totalCost += cost;
            edgesUsed++;

            // Offer all edges from u to nodes not yet in MST
            for (int v = 0; v < n; v++) {
                if (!inMST[v]) {
                    int dist = Math.abs(points[u][0] - points[v][0])
                             + Math.abs(points[u][1] - points[v][1]);
                    pq.offer(new int[]{dist, v});
                }
            }
        }

        return totalCost;
    }
}
