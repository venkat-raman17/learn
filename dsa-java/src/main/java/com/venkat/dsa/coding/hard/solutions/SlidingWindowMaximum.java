package com.venkat.dsa.coding.hard.solutions;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Sliding Window Maximum (LeetCode 239) — Hard
 *
 * <p>Approach: Monotonic decreasing deque (stores <em>indices</em>). For each new element
 * at {@code right}: (1) pop from the back any indices whose values are &lt;= the current
 * value — they can never be a future maximum while the current element is in the window;
 * (2) push {@code right} to the back; (3) if the front index is outside the window
 * ({@code deque.front < right - k + 1}), pop it. After the first full window is formed
 * ({@code right >= k - 1}), the front of the deque is the index of the window maximum.
 *
 * <p><b>Time complexity:</b> O(n) — each index is pushed and popped at most once.<br>
 * <b>Space complexity:</b> O(k) — the deque holds at most k indices at any time.
 *
 * <p><b>Key insight:</b> The deque is always monotonically decreasing in value; its front
 * is always the current window maximum, and stale (out-of-window) front entries are
 * lazily removed.
 */
public class SlidingWindowMaximum {

    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];
        // Monotonic deque storing indices; front = index of current window max
        Deque<Integer> dq = new ArrayDeque<>();

        for (int right = 0; right < n; right++) {
            // Remove indices from the back that correspond to values <= nums[right]
            // (they can never be the maximum for any future window)
            while (!dq.isEmpty() && nums[dq.peekLast()] <= nums[right]) {
                dq.pollLast();
            }
            dq.offerLast(right);

            // Remove the front index if it has slid out of the current window
            if (dq.peekFirst() < right - k + 1) {
                dq.pollFirst();
            }

            // Record result once the first full window is complete
            if (right >= k - 1) {
                result[right - k + 1] = nums[dq.peekFirst()];
            }
        }

        return result;
    }
}
