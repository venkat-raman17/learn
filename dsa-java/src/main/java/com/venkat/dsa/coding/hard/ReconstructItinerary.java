package com.venkat.dsa.coding.hard;

import java.util.*;

/**
 * NeetCode / LeetCode — Reconstruct Itinerary
 *
 * <p>Difficulty: HARD
 * <p>Pattern: Advanced Graphs
 * <p>URL: https://leetcode.com/problems/reconstruct-itinerary/
 *
 * <p>Given a list of airline {@code tickets} represented by pairs of departure and arrival airports
 * {@code [from, to]}, reconstruct the itinerary in order. The itinerary must begin with "JFK"
 * and all tickets must be used exactly once. If multiple valid itineraries exist, return the one
 * with the smallest lexical order.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= tickets.length &lt;= 300</li>
 *   <li>tickets[i].length == 2</li>
 *   <li>from_i.length == 3; to_i.length == 3</li>
 *   <li>from_i and to_i consist of uppercase English letters.</li>
 *   <li>from_i != to_i</li>
 *   <li>The input is guaranteed to have a valid itinerary.</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  tickets = [["MUC","LHR"],["JFK","MUC"],["SFO","SJC"],["LHR","SFO"]]
 *   Output: ["JFK","MUC","LHR","SFO","SJC"]
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  tickets = [["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]]
 *   Output: ["JFK","ATL","JFK","SFO","ATL","SFO"]
 * </pre>
 *
 * <p>Target Time: O(E log E) &nbsp; Space: O(V + E)
 *
 * <p>Hint 1: Use Hierholzer's algorithm for an Eulerian path — sort adjacency lists
 *            so destinations are visited in lexicographic order.
 * <p>Hint 2: Perform a DFS; only add an airport to the result after all its outgoing
 *            edges have been traversed, then reverse the result at the end.
 */
public class ReconstructItinerary {

    /**
     * Returns the reconstructed itinerary starting from "JFK" using all tickets exactly once,
     * choosing the lexicographically smallest valid ordering.
     *
     * @param tickets list of [from, to] ticket pairs
     * @return ordered list of airports forming the full itinerary
     */
    public List<String> findItinerary(List<List<String>> tickets) {
        throw new UnsupportedOperationException("implement me");
    }
}
