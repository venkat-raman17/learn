package com.venkat.dsa.coding.hard.solutions;

import java.util.Deque;
import java.util.ArrayDeque;

/**
 * Largest Rectangle in Histogram (LeetCode #84)
 *
 * Approach: Monotonic increasing stack of indices. For each bar we maintain
 * the invariant that bars in the stack are strictly increasing in height.
 * When we encounter a bar shorter than the stack top, we pop and compute the
 * rectangle with that popped bar as the shortest bar (limiting height). The
 * width extends from the new stack top + 1 to the current index - 1. A
 * sentinel value of 0 height at the end flushes all remaining bars.
 *
 * Key insight: The furthest left a rectangle of height h can extend is the
 * position immediately after the nearest bar shorter than h to its left —
 * exactly what the monotonic stack tracks.
 *
 * Time complexity:  O(n) — each bar is pushed and popped at most once.
 * Space complexity: O(n) — stack can hold up to n indices.
 */
public class LargestRectangleInHistogram {

    public int largestRectangleArea(int[] heights) {
        int n = heights.length;
        int maxArea = 0;
        // Stack holds indices; heights are non-decreasing from bottom to top
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i <= n; i++) {
            // Use 0 as a sentinel height at index n to flush the stack
            int currentHeight = (i == n) ? 0 : heights[i];

            while (!stack.isEmpty() && currentHeight < heights[stack.peek()]) {
                int height = heights[stack.pop()]; // height of the popped bar
                // Width: extends from (new top + 1) to (i - 1)
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * width);
            }

            stack.push(i);
        }

        return maxArea;
    }
}
