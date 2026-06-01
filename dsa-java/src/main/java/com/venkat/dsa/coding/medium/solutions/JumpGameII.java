package com.venkat.dsa.coding.medium.solutions;

/**
 * Jump Game II (LeetCode #45)
 *
 * <p>Greedy BFS-level scan: treat each "level" as the range of indices reachable within the same
 * number of jumps. Track {@code currentEnd} (end of the current level) and {@code farthest} (the
 * farthest index reachable from any position in the current level). When we exhaust the current
 * level, increment the jump count and advance to the next level.
 *
 * <p><b>Key insight:</b> We greedily choose the level boundary that maximises reach, identical to
 * a BFS on an implicit graph — each "layer" costs one jump.
 *
 * <p><b>Time complexity:</b> O(n) — single pass.<br>
 * <b>Space complexity:</b> O(1) — constant extra space.
 */
public class JumpGameII {

    public int jump(int[] nums) {
        int jumps = 0;
        int currentEnd = 0;   // end of the current BFS level
        int farthest = 0;     // farthest reachable from this level

        // We don't need to process the last index — we just need to reach it
        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);

            // Reached the end of the current level — must jump
            if (i == currentEnd) {
                jumps++;
                currentEnd = farthest;

                // Early exit if we can already reach the end
                if (currentEnd >= nums.length - 1) {
                    break;
                }
            }
        }

        return jumps;
    }
}
