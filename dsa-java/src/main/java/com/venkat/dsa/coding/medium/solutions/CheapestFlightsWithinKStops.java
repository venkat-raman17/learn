package com.venkat.dsa.coding.medium.solutions;

import java.util.*;

/**
 * Cheapest Flights Within K Stops (LeetCode 787)
 *
 * <p>Uses Bellman-Ford relaxation limited to {@code k+1} rounds (each round represents one
 * additional flight leg). After each round we only use prices computed in the previous round to
 * avoid counting more edges than allowed — this is the "copy prices" technique.
 *
 * <p><b>Key insight:</b> Standard Dijkstra does not handle the stop-count constraint cleanly;
 * Bellman-Ford with at-most-{@code k+1} edge-relaxation rounds directly encodes the limit.
 * Copying the array before each round prevents using edges discovered in the same round.
 *
 * <p><b>Time:</b> O(K * E) where E = number of flights.<br>
 * <b>Space:</b> O(N) for the distance arrays.
 */
public class CheapestFlightsWithinKStops {

    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        final int INF = Integer.MAX_VALUE / 2;
        int[] prices = new int[n];
        Arrays.fill(prices, INF);
        prices[src] = 0;

        // Run Bellman-Ford for at most k+1 rounds (k stops = k+1 edges)
        for (int i = 0; i <= k; i++) {
            // Copy to prevent using edges discovered in the current round
            int[] temp = Arrays.copyOf(prices, n);
            for (int[] flight : flights) {
                int u = flight[0], v = flight[1], w = flight[2];
                if (prices[u] != INF && prices[u] + w < temp[v]) {
                    temp[v] = prices[u] + w;
                }
            }
            prices = temp;
        }

        return prices[dst] == INF ? -1 : prices[dst];
    }
}
