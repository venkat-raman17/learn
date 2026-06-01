package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Car Fleet
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Stack
 * <p>URL: https://leetcode.com/problems/car-fleet/
 *
 * <p>n cars are heading to the same destination. Each car has a starting position
 * and speed. A car fleet forms when one or more cars arrive at the destination
 * at the same time. Return the number of fleets that arrive at the destination.
 *
 * <p>Constraints:
 * <ul>
 *   <li>n == position.length == speed.length</li>
 *   <li>1 <= n <= 10^5</li>
 *   <li>0 < target <= 10^6</li>
 *   <li>0 <= position[i] < target; all positions are distinct</li>
 *   <li>0 < speed[i] <= 10^6</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   carFleet(12, [10,8,0,5,3], [2,4,1,1,3]) -> 3
 *   carFleet(10, [3],          [3])          -> 1
 * </pre>
 *
 * <p>Target: O(n log n) time, O(n) space
 *
 * <p>Hint 1: Sort cars by position descending; compute each car's time-to-target.
 * <p>Hint 2: Use a stack — if the current car's time is <= the top of the stack, it joins that fleet; otherwise it starts a new one.
 */
public class CarFleet {

    public int carFleet(int target, int[] position, int[] speed) {
        throw new UnsupportedOperationException("implement me");
    }
}
