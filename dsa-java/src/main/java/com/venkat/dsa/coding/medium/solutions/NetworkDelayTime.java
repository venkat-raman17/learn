package com.venkat.dsa.coding.medium.solutions;

import java.util.*;

/**
 * Network Delay Time (LeetCode 743)
 *
 * <p>Uses Dijkstra's algorithm from the source node {@code k} to find the shortest path to all
 * reachable nodes. After relaxation, the answer is the maximum distance among all {@code n} nodes;
 * if any node is unreachable the answer is {@code -1}.
 *
 * <p><b>Key insight:</b> The "network delay" is bounded by the slowest-to-reach node, so the
 * answer is {@code max(dist)} over all nodes once Dijkstra finishes.
 *
 * <p><b>Time:</b> O((V + E) log V) using a priority queue.<br>
 * <b>Space:</b> O(V + E) for the adjacency list and distance array.
 */
public class NetworkDelayTime {

    public int networkDelayTime(int[][] times, int n, int k) {
        // Build adjacency list: node -> list of (neighbor, weight)
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int[] t : times) {
            graph.computeIfAbsent(t[0], x -> new ArrayList<>()).add(new int[]{t[1], t[2]});
        }

        // dist[i] = shortest known distance to node i (1-indexed, use index 0 as dummy)
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[k] = 0;

        // Min-heap: (distance, node)
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, k});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int d = cur[0], u = cur[1];

            // Skip stale entries
            if (d > dist[u]) continue;

            List<int[]> neighbors = graph.getOrDefault(u, Collections.emptyList());
            for (int[] edge : neighbors) {
                int v = edge[0], w = edge[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.offer(new int[]{dist[v], v});
                }
            }
        }

        // Find the maximum distance across all nodes
        int maxDist = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == Integer.MAX_VALUE) return -1; // node unreachable
            maxDist = Math.max(maxDist, dist[i]);
        }
        return maxDist;
    }
}
