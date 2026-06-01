package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Generate Parentheses (LeetCode #22)
 *
 * Approach: Backtracking with a character buffer. At each recursive step we
 * can add an open bracket if we still have opens remaining, or a close bracket
 * if the number of closes used is less than opens used so far. When the buffer
 * reaches length 2*n the combination is valid and is added to the result list.
 *
 * Key insight: Tracking open/close counts (rather than building a stack) lets
 * us prune invalid branches early — we never need to validate after the fact.
 *
 * Time complexity:  O(4^n / sqrt(n)) — the n-th Catalan number of valid combos.
 * Space complexity: O(n) recursion depth; O(4^n / sqrt(n)) for the output list.
 */
public class GenerateParentheses {

    public List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        backtrack(result, new StringBuilder(), 0, 0, n);
        return result;
    }

    /**
     * @param open  number of '(' placed so far
     * @param close number of ')' placed so far
     * @param max   total pairs needed (n)
     */
    private void backtrack(List<String> result, StringBuilder current,
                           int open, int close, int max) {
        if (current.length() == max * 2) {
            result.add(current.toString());
            return;
        }
        // Place an open bracket if we still have some left
        if (open < max) {
            current.append('(');
            backtrack(result, current, open + 1, close, max);
            current.deleteCharAt(current.length() - 1); // undo
        }
        // Place a close bracket only if it won't exceed opens placed
        if (close < open) {
            current.append(')');
            backtrack(result, current, open, close + 1, max);
            current.deleteCharAt(current.length() - 1); // undo
        }
    }
}
