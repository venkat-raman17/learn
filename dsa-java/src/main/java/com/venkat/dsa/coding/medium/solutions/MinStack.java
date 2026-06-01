package com.venkat.dsa.coding.medium.solutions;

import java.util.Deque;
import java.util.ArrayDeque;

/**
 * Min Stack (LeetCode #155)
 *
 * Approach: Maintain two stacks — one for all values (main stack) and one
 * that tracks the current minimum at each level (min stack). Every push onto
 * the main stack also pushes the new running minimum onto the min stack, so
 * getMin() is always an O(1) peek of the min stack's top.
 *
 * Key insight: The min stack mirrors the main stack in size; when a value is
 * popped from the main stack the corresponding minimum is also popped,
 * restoring the previous minimum for free.
 *
 * Time complexity:  O(1) for all operations.
 * Space complexity: O(n) — two stacks each holding at most n elements.
 */
public class MinStack {

    private final Deque<Integer> stack;
    private final Deque<Integer> minStack; // top always holds current minimum

    public MinStack() {
        stack = new ArrayDeque<>();
        minStack = new ArrayDeque<>();
    }

    public void push(int val) {
        stack.push(val);
        // New minimum is the smaller of val and the current min (or val itself if empty)
        int newMin = minStack.isEmpty() ? val : Math.min(val, minStack.peek());
        minStack.push(newMin);
    }

    public void pop() {
        stack.pop();
        minStack.pop(); // discard the minimum that corresponded to the removed element
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return minStack.peek();
    }
}
