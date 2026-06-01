package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Valid Parenthesis String
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Greedy
 * <p>URL: https://leetcode.com/problems/valid-parenthesis-string/
 *
 * <p>Given a string {@code s} containing only {@code '('}, {@code ')'}, and {@code '*'} where
 * {@code '*'} can be treated as a single {@code '('}, a single {@code ')'}, or an empty string,
 * return {@code true} if {@code s} is valid.
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>1 &lt;= s.length &lt;= 100</li>
 *   <li>s[i] is '(', ')' or '*'</li>
 * </ul>
 *
 * <p><b>Examples:</b>
 * <pre>
 * Input: s = "()"   Output: true
 * Input: s = "(*)"  Output: true
 * Input: s = "(*))" Output: true
 * </pre>
 *
 * <p><b>Target:</b> O(n) time, O(1) space
 *
 * <p><b>Hint 1:</b> Track a range [minOpen, maxOpen] of possible open-paren counts; '*' widens
 * the range by adjusting both ends.
 * <p><b>Hint 2:</b> If maxOpen ever drops below 0 the string is invalid; at the end check that
 * minOpen == 0.
 */
public class ValidParenthesisString {

    /**
     * Returns true if the string is valid given that '*' can represent '(', ')', or empty.
     *
     * @param s string of '(', ')', '*'
     * @return whether a valid interpretation exists
     */
    public boolean checkValidString(String s) {
        throw new UnsupportedOperationException("implement me");
    }
}
