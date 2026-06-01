package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Min Stack
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Stack
 * <p>URL: https://leetcode.com/problems/min-stack/
 *
 * <p>Design a stack that supports push, pop, top, and retrieving the minimum
 * element in constant time. All operations must run in O(1).
 *
 * <p>Constraints:
 * <ul>
 *   <li>-2^31 <= val <= 2^31 - 1</li>
 *   <li>pop, top, getMin are always called on a non-empty stack</li>
 *   <li>At most 3 * 10^4 calls to push, pop, top, and getMin</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   MinStack ms = new MinStack();
 *   ms.push(-2); ms.push(0); ms.push(-3);
 *   ms.getMin() -> -3
 *   ms.pop();
 *   ms.top()    -> 0
 *   ms.getMin() -> -2
 * </pre>
 *
 * <p>Target: O(1) per operation, O(n) space
 *
 * <p>Hint 1: Maintain a second "min stack" that tracks the current minimum alongside each element.
 * <p>Hint 2: When popping, pop from both stacks simultaneously.
 */
public class MinStack {

    public MinStack() {
        throw new UnsupportedOperationException("implement me");
    }

    public void push(int val) {
        throw new UnsupportedOperationException("implement me");
    }

    public void pop() {
        throw new UnsupportedOperationException("implement me");
    }

    public int top() {
        throw new UnsupportedOperationException("implement me");
    }

    public int getMin() {
        throw new UnsupportedOperationException("implement me");
    }
}
