package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Gas Station
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Greedy
 * <p>URL: https://leetcode.com/problems/gas-station/
 *
 * <p>There are {@code n} gas stations in a circle. Given arrays {@code gas} and {@code cost} where
 * {@code gas[i]} is the gas available at station {@code i} and {@code cost[i]} is the gas needed to
 * travel to the next station, return the starting station index if you can complete the circuit
 * once in the clockwise direction, or -1 if impossible. The solution is guaranteed to be unique if
 * it exists.
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>n == gas.length == cost.length</li>
 *   <li>1 &lt;= n &lt;= 10^5</li>
 *   <li>0 &lt;= gas[i], cost[i] &lt;= 10^4</li>
 * </ul>
 *
 * <p><b>Examples:</b>
 * <pre>
 * Input: gas = [1,2,3,4,5], cost = [3,4,5,1,2]  Output: 3
 * Input: gas = [2,3,4], cost = [3,4,3]           Output: -1
 * </pre>
 *
 * <p><b>Target:</b> O(n) time, O(1) space
 *
 * <p><b>Hint 1:</b> If the total gas is less than the total cost, there is no solution.
 * <p><b>Hint 2:</b> Greedily reset the starting candidate whenever the running tank drops below 0.
 */
public class GasStation {

    /**
     * Returns the index of the starting gas station for a complete circuit, or -1 if impossible.
     *
     * @param gas  gas available at each station
     * @param cost gas cost to travel to the next station
     * @return starting station index, or -1
     */
    public int canCompleteCircuit(int[] gas, int[] cost) {
        throw new UnsupportedOperationException("implement me");
    }
}
