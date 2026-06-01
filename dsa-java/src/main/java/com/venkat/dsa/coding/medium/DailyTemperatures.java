package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Daily Temperatures
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Stack
 * <p>URL: https://leetcode.com/problems/daily-temperatures/
 *
 * <p>Given an array of daily temperatures, return an array where each element
 * is the number of days until a warmer temperature. If no warmer temperature
 * exists in the future, put 0 for that day.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= temperatures.length <= 10^5</li>
 *   <li>30 <= temperatures[i] <= 100</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   dailyTemperatures([73,74,75,71,69,72,76,73]) -> [1,1,4,2,1,1,0,0]
 *   dailyTemperatures([30,40,50,60])             -> [1,1,1,0]
 * </pre>
 *
 * <p>Target: O(n) time, O(n) space
 *
 * <p>Hint 1: Use a monotonic decreasing stack storing indices; when a warmer day arrives, resolve all cooler days on the stack.
 * <p>Hint 2: The answer for each index is (current index - popped index).
 */
public class DailyTemperatures {

    public int[] dailyTemperatures(int[] temperatures) {
        throw new UnsupportedOperationException("implement me");
    }
}
