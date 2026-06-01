package com.venkat.dsa.coding.medium.solutions;

import java.util.Deque;
import java.util.ArrayDeque;

/**
 * Daily Temperatures (LeetCode #739)
 *
 * Approach: Monotonic decreasing stack that stores indices. For each day i,
 * while the stack is non-empty and the current temperature is greater than the
 * temperature at the index on top of the stack, that top index has found its
 * "next warmer day" — record the difference (i - top) in the answer array
 * and pop. Push the current index afterwards.
 *
 * Key insight: Maintaining a stack of indices in non-increasing temperature
 * order means the first time a day is "warmer" than a previous day, we
 * immediately know the wait in O(1) per element (amortized).
 *
 * Time complexity:  O(n) — each index is pushed and popped at most once.
 * Space complexity: O(n) — stack can hold all indices in the worst case.
 */
public class DailyTemperatures {

    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n]; // default 0 for days with no warmer future day
        Deque<Integer> stack = new ArrayDeque<>(); // stores indices

        for (int i = 0; i < n; i++) {
            // Pop all indices whose temperature is beaten by today's temperature
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int prevIdx = stack.pop();
                answer[prevIdx] = i - prevIdx; // days waited
            }
            stack.push(i);
        }

        return answer;
    }
}
