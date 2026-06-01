package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Jump Game II
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Greedy
 * <p>URL: https://leetcode.com/problems/jump-game-ii/
 *
 * <p>Given a 0-indexed array {@code nums} of integers where {@code nums[i]} is the max jump length
 * from index {@code i}, return the minimum number of jumps to reach the last index. You can always
 * reach the last index.
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>1 &lt;= nums.length &lt;= 10^4</li>
 *   <li>0 &lt;= nums[i] &lt;= 1000</li>
 *   <li>It is guaranteed you can reach nums[n-1]</li>
 * </ul>
 *
 * <p><b>Examples:</b>
 * <pre>
 * Input: nums = [2,3,1,1,4]  Output: 2  (jump to index 1 then to last)
 * Input: nums = [2,3,0,1,4]  Output: 2
 * </pre>
 *
 * <p><b>Target:</b> O(n) time, O(1) space
 *
 * <p><b>Hint 1:</b> Use a greedy window: track the end of the current jump range and the furthest
 * you can reach within that range.
 * <p><b>Hint 2:</b> When you reach the end of the current window, increment the jump count and
 * extend the window to the furthest reachable index.
 */
public class JumpGameII {

    /**
     * Returns the minimum number of jumps to reach the last index.
     *
     * @param nums array of maximum jump lengths
     * @return minimum jump count
     */
    public int jump(int[] nums) {
        throw new UnsupportedOperationException("implement me");
    }
}
