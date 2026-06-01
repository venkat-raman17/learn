package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Jump Game
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Greedy
 * <p>URL: https://leetcode.com/problems/jump-game/
 *
 * <p>Given an array {@code nums} where each element represents your maximum jump length at that
 * position, determine if you can reach the last index starting from index 0.
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>1 &lt;= nums.length &lt;= 10^4</li>
 *   <li>0 &lt;= nums[i] &lt;= 10^5</li>
 * </ul>
 *
 * <p><b>Examples:</b>
 * <pre>
 * Input: nums = [2,3,1,1,4]  Output: true   (jump 1 step to index 1, then 3 steps to the last)
 * Input: nums = [3,2,1,0,4]  Output: false  (always stuck at index 3)
 * </pre>
 *
 * <p><b>Target:</b> O(n) time, O(1) space
 *
 * <p><b>Hint 1:</b> Track the furthest index reachable so far as you iterate left to right.
 * <p><b>Hint 2:</b> If the current index ever exceeds the furthest reachable index, return false immediately.
 */
public class JumpGame {

    /**
     * Returns true if you can reach the last index from index 0.
     *
     * @param nums array of maximum jump lengths
     * @return whether the last index is reachable
     */
    public boolean canJump(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
