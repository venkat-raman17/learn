package com.venkat.dsa.coding.medium.solutions;

/**
 * Gas Station (LeetCode #134)
 *
 * <p>Two observations drive the O(n) greedy solution: (1) A solution exists iff
 * {@code sum(gas) >= sum(cost)}. (2) If starting at index {@code s} causes the tank to go
 * negative at index {@code t}, then no index in [s, t] can be a valid starting point — so the
 * next candidate start is {@code t + 1}.
 *
 * <p><b>Key insight:</b> A single pass simultaneously checks feasibility and finds the start: once
 * total surplus is non-negative we know a unique solution exists; the candidate start we arrive at
 * is the answer.
 *
 * <p><b>Time complexity:</b> O(n) — two linear scans folded into one.<br>
 * <b>Space complexity:</b> O(1) — constant extra space.
 */
public class GasStation {

    public int canCompleteCircuit(int[] gas, int[] cost) {
        int totalSurplus = 0;
        int currentSurplus = 0;
        int start = 0;

        for (int i = 0; i < gas.length; i++) {
            int net = gas[i] - cost[i];
            totalSurplus += net;
            currentSurplus += net;

            // Can't reach next station from current start — reset
            if (currentSurplus < 0) {
                start = i + 1;
                currentSurplus = 0;
            }
        }

        // If total gas < total cost, no solution exists
        return totalSurplus >= 0 ? start : -1;
    }
}
