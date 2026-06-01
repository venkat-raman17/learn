package com.venkat.dsa.coding.medium.solutions;

/**
 * Valid Parenthesis String (LeetCode #678)
 *
 * <p>Track the range of possible "open parenthesis balances" as a window [lo, hi]. {@code lo} is
 * the minimum possible balance (treating '*' as ')'), {@code hi} is the maximum (treating '*' as
 * '('). For '(' both grow by 1; for ')' both shrink by 1; for '*' lo decreases and hi increases.
 * Clamp lo to 0 (balance can never go negative). If hi drops below 0 the string is invalid.
 *
 * <p><b>Key insight:</b> We don't need to enumerate all '*' choices — maintaining the feasible
 * range of balances captures all possibilities simultaneously.
 *
 * <p><b>Time complexity:</b> O(n) — single pass.<br>
 * <b>Space complexity:</b> O(1) — constant extra space.
 */
public class ValidParenthesisString {

    public boolean checkValidString(String s) {
        int lo = 0; // minimum possible open-paren balance
        int hi = 0; // maximum possible open-paren balance

        for (char c : s.toCharArray()) {
            if (c == '(') {
                lo++;
                hi++;
            } else if (c == ')') {
                lo--;
                hi--;
            } else { // '*'
                lo--; // treat as ')'
                hi++; // treat as '('
            }

            // Balance can never go negative — floor lo at 0
            if (lo < 0) lo = 0;

            // If even the maximum balance went negative, impossible
            if (hi < 0) return false;
        }

        // Valid iff minimum balance can reach 0
        return lo == 0;
    }
}
