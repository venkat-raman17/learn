package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Evaluate Reverse Polish Notation
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Stack
 * <p>URL: https://leetcode.com/problems/evaluate-reverse-polish-notation/
 *
 * <p>Evaluate an arithmetic expression in Reverse Polish Notation (postfix). Valid
 * operators are +, -, *, /. Integer division truncates toward zero.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= tokens.length <= 10^4</li>
 *   <li>tokens[i] is either an operator or an integer in [-200, 200]</li>
 *   <li>The answer is guaranteed to fit in a 32-bit integer</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   evalRPN(["2","1","+","3","*"]) -> 9   // (2+1)*3
 *   evalRPN(["4","13","5","/","+"]) -> 6  // 4+(13/5)
 * </pre>
 *
 * <p>Target: O(n) time, O(n) space
 *
 * <p>Hint 1: Push operands onto a stack; when an operator is seen, pop two operands, apply, and push the result.
 * <p>Hint 2: Be careful about operand order: pop b first, then a; compute a OP b.
 */
public class EvaluateReversePolishNotation {

    public int evalRPN(String[] tokens) {
        throw new UnsupportedOperationException("implement me");
    }
}
