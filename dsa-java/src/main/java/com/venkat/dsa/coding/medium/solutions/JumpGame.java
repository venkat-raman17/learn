package com.venkat.dsa.coding.medium.solutions;

/**
 * Jump Game (LeetCode #55)
 *
 * <p>Greedy: maintain the farthest index reachable as we scan left to right. At each index {@code
 * i}, if {@code i > maxReach} the current position is unreachable and we return false. Otherwise
 * update {@code maxReach = max(maxReach, i + nums[i])}.
 *
 * <p><b>Key insight:</b> We never need to track exact jump sequences — only whether the frontier
 * of reachability ever covers the last index.
 *
 * <p><b>Time complexity:</b> O(n) — single pass.<br>
 * <b>Space complexity:</b> O(1) — constant extra space.
 */
public class JumpGame {

    public boolean canJump(int[] nums) {
        int maxReach = 0;

        for (int i = 0; i < nums.length; i++) {
            // If current index is beyond what we can reach, it's impossible
            if (i > maxReach) {
                return false;
            }
            // Extend the furthest reachable index
            maxReach = Math.max(maxReach, i + nums[i]);
        }

        return true;
    }
}
