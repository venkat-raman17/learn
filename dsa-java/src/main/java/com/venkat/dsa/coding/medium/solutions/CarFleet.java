package com.venkat.dsa.coding.medium.solutions;

import java.util.Arrays;
import java.util.Deque;
import java.util.ArrayDeque;

/**
 * Car Fleet (LeetCode #853)
 *
 * Approach: Pair each car's position with its speed, then sort the pairs by
 * position in descending order (closest to target first). For each car compute
 * the time it would take to reach the target if unobstructed. Use a stack to
 * track fleet arrival times: if a car's arrival time is greater than the car
 * already at the top (the car ahead), it cannot catch up and forms a new fleet.
 * Otherwise it merges into the fleet ahead and is ignored.
 *
 * Key insight: A faster car that starts behind a slower car will slow down once
 * it catches up. Sorting by position descending and comparing arrival times lets
 * us determine merges in a single linear pass.
 *
 * Time complexity:  O(n log n) — dominated by sorting.
 * Space complexity: O(n) — sorted array + stack.
 */
public class CarFleet {

    public int carFleet(int target, int[] position, int[] speed) {
        int n = position.length;
        if (n == 0) return 0;

        // Build and sort (position, speed) pairs by position descending
        int[][] cars = new int[n][2];
        for (int i = 0; i < n; i++) {
            cars[i][0] = position[i];
            cars[i][1] = speed[i];
        }
        Arrays.sort(cars, (a, b) -> b[0] - a[0]); // descending position

        // Stack stores arrival times; each entry on the stack represents a fleet
        Deque<Double> stack = new ArrayDeque<>();

        for (int[] car : cars) {
            double time = (double)(target - car[0]) / car[1];
            // If this car arrives later than the fleet ahead, it is a new fleet
            if (stack.isEmpty() || time > stack.peek()) {
                stack.push(time);
            }
            // Otherwise this car catches up and joins the fleet in front (discard)
        }

        return stack.size();
    }
}
