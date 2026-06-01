package com.venkat.dsa.coding.medium.solutions;

import java.util.Deque;
import java.util.ArrayDeque;

/**
 * Evaluate Reverse Polish Notation (LeetCode #150)
 *
 * Approach: Iterate through each token using a stack of integers. When a token
 * is a number, push it. When a token is an operator, pop two operands (the
 * second-popped value is the LEFT operand), apply the operation, and push the
 * result back. The single remaining value is the answer.
 *
 * Key insight: RPN eliminates parentheses; a stack directly models the
 * evaluation order — operands accumulate until an operator collapses them.
 *
 * Time complexity:  O(n) — one pass over the tokens array.
 * Space complexity: O(n) — stack holds at most n/2 + 1 intermediate values.
 */
public class EvaluateReversePolishNotation {

    public int evalRPN(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();

        for (String token : tokens) {
            switch (token) {
                case "+": {
                    int b = stack.pop();
                    int a = stack.pop(); // a is the left operand
                    stack.push(a + b);
                    break;
                }
                case "-": {
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a - b);
                    break;
                }
                case "*": {
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a * b);
                    break;
                }
                case "/": {
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a / b); // integer division truncates toward zero per spec
                    break;
                }
                default:
                    stack.push(Integer.parseInt(token));
            }
        }

        return stack.pop();
    }
}
