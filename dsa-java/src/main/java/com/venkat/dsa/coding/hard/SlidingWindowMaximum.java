package com.venkat.dsa.coding.hard;

/**
 * NeetCode / LeetCode — Sliding Window Maximum
 *
 * <p>Difficulty: HARD
 * <p>Pattern: Sliding Window
 * <p>URL: https://leetcode.com/problems/sliding-window-maximum/
 *
 * <p>Given an integer array {@code nums} and a sliding window of size {@code k}, return an array
 * of the maximum value in each window position as it slides from left to right.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= nums.length &lt;= 10^5</li>
 *   <li>-10^4 &lt;= nums[i] &lt;= 10^4</li>
 *   <li>1 &lt;= k &lt;= nums.length</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  nums = [1,3,-1,-3,5,3,6,7], k = 3
 *   Output: [3,3,5,5,6,7]
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  nums = [1], k = 1
 *   Output: [1]
 * </pre>
 *
 * <p>Target: O(n) time, O(k) space
 *
 * <p>Hint 1: Use a monotonic deque (decreasing) that stores indices; the front always holds the index of the current window's maximum.
 * <p>Hint 2: Before adding a new element, pop from the back of the deque any indices whose values are less than or equal to the new element, since they can never be the maximum.
 */
public class SlidingWindowMaximum {

    public int[] maxSlidingWindow(int[] nums, int k) {
        throw new UnsupportedOperationException("implement me");
    }
}
