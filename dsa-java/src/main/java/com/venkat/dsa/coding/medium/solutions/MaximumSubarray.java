package com.venkat.dsa.coding.medium.solutions;

/**
 * Maximum Subarray (LeetCode #53)
 *
 * <p>Uses Kadane's Algorithm: iterate through the array, at each position either extend the
 * current subarray or start fresh from the current element (whichever is larger). Track the
 * global maximum seen so far.
 *
 * <p><b>Key insight:</b> If the running sum drops below 0 it can never help any future subarray,
 * so reset it to 0 (effectively starting a new subarray at the next element).
 *
 * <p><b>Time complexity:</b> O(n) — single pass.<br>
 * <b>Space complexity:</b> O(1) — constant extra space.
 */
public class MaximumSubarray {

    public int maxSubArray(int[] nums) {
        int maxSum = nums[0];
        int current = 0;

        for (int num : nums) {
            // If current running sum is negative, discard it — start fresh
            if (current < 0) {
                current = 0;
            }
            current += num;
            maxSum = Math.max(maxSum, current);
        }

        return maxSum;
    }
}
