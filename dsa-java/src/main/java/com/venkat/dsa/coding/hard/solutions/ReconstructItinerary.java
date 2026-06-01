package com.venkat.dsa.coding.hard.solutions;

import java.util.*;

/**
 * Reconstruct Itinerary (LeetCode 332)
 *
 * <p>Applies Hierholzer's algorithm for finding an Eulerian path in a directed multigraph. We sort
 * each adjacency list lexicographically so that when the DFS always picks the smallest destination
 * first, the resulting post-order reversal yields the lexicographically smallest valid itinerary.
 *
 * <p><b>Key insight:</b> In an Eulerian-path graph, a greedy DFS that always picks the smallest
 * neighbor and appends the current node to the result <em>after</em> all edges are consumed
 * (post-order) correctly reconstructs the path even when dead ends exist mid-path.
 *
 * <p><b>Time:</b> O(E log E) for sorting + O(E) for DFS = O(E log E).<br>
 * <b>Space:</b> O(V + E) for adjacency list and call/result stack.
 */
public class ReconstructItinerary {

    public List<String> findItinerary(List<List<String>> tickets) {
        // Build adjacency list with sorted (lexicographic) neighbors
        Map<String, LinkedList<String>> graph = new HashMap<>();
        for (List<String> ticket : tickets) {
            graph.computeIfAbsent(ticket.get(0), k -> new LinkedList<>()).add(ticket.get(1));
        }
        // Sort each adjacency list so we always pick smallest destination first
        for (LinkedList<String> list : graph.values()) {
            Collections.sort(list);
        }

        LinkedList<String> result = new LinkedList<>();
        dfs("JFK", graph, result);
        return result;
    }

    private void dfs(String airport, Map<String, LinkedList<String>> graph, LinkedList<String> result) {
        LinkedList<String> neighbors = graph.get(airport);
        // Visit all neighbors (consuming each edge exactly once)
        while (neighbors != null && !neighbors.isEmpty()) {
            dfs(neighbors.poll(), graph, result);
        }
        // Add current airport to front after all outgoing edges are exhausted (post-order)
        result.addFirst(airport);
    }
}
