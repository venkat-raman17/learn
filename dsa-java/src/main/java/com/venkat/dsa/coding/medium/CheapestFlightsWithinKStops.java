package com.venkat.dsa.coding.medium;

import java.util.*;

/**
 * NeetCode / LeetCode — Cheapest Flights Within K Stops
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Advanced Graphs
 * <p>URL: https://leetcode.com/problems/cheapest-flights-within-k-stops/
 *
 * <p>Given {@code n} cities connected by directed flights {@code flights[i] = [from, to, price]},
 * find the cheapest price from city {@code src} to city {@code dst} using at most {@code k} stops.
 * Return -1 if no such route exists.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= n &lt;= 100</li>
 *   <li>0 &lt;= flights.length &lt;= (n * (n - 1) / 2)</li>
 *   <li>flights[i].length == 3</li>
 *   <li>0 &lt;= from, to &lt; n; from != to</li>
 *   <li>1 &lt;= price &lt;= 10^4</li>
 *   <li>0 &lt;= k &lt;= n</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  n=4, flights=[[0,1,100],[1,2,100],[2,0,100],[1,3,600],[2,3,200]],
 *           src=0, dst=3, k=1
 *   Output: 700
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  n=3, flights=[[0,1,100],[1,2,100],[0,2,500]], src=0, dst=2, k=1
 *   Output: 200
 * </pre>
 *
 * <p>Target Time: O(k * E) &nbsp; Space: O(n)
 *
 * <p>Hint 1: Use Bellman-Ford limited to {@code k+1} relaxation rounds (one per edge traversal).
 * <p>Hint 2: Keep a copy of the previous round's distances so that each round only
 *            extends paths by exactly one edge, not more.
 */
public class CheapestFlightsWithinKStops {

    /**
     * Returns the cheapest price from src to dst with at most k intermediate stops,
     * or -1 if no valid route exists.
     *
     * @param n       number of cities
     * @param flights directed edges [from, to, price]
     * @param src     source city
     * @param dst     destination city
     * @param k       maximum number of stops
     * @return minimum price, or -1
     */
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        throw new UnsupportedOperationException("implement me");
    }
}
