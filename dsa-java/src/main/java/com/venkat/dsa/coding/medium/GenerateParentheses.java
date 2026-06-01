package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — Generate Parentheses
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Stack
 * <p>URL: https://leetcode.com/problems/generate-parentheses/
 *
 * <p>Given n pairs of parentheses, generate all combinations of well-formed
 * (valid) parentheses strings.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= n <= 8</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   generateParenthesis(1) -> ["()"]
 *   generateParenthesis(3) -> ["((()))","(()())","(())()","()(())","()()()"]
 * </pre>
 *
 * <p>Target: O(4^n / sqrt(n)) time (Catalan number), O(n) stack space
 *
 * <p>Hint 1: Use backtracking — at each step you may add '(' if open count < n, or ')' if close count < open count.
 * <p>Hint 2: A stack (or recursion with a StringBuilder) tracks the current string being built.
 */
public class GenerateParentheses {

    public List<String> generateParenthesis(int n) {
        throw new UnsupportedOperationException("implement me");
    }
}
