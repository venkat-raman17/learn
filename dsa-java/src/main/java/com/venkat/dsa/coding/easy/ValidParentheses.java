package com.venkat.dsa.coding.easy;

import java.util.Stack;

/**
 * NeetCode / LeetCode — Valid Parentheses
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Stack
 * <p>URL: https://leetcode.com/problems/valid-parentheses/
 *
 * <p>Given a string containing only '(', ')', '{', '}', '[', ']', determine if
 * the input string is valid. A string is valid if every open bracket is closed
 * by the same type of bracket in the correct order.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= s.length <= 10^4</li>
 *   <li>s consists of parentheses only '()[]{}'</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   isValid("()[]{}") -> true
 *   isValid("(]")     -> false
 * </pre>
 *
 * <p>Target: O(n) time, O(n) space
 *
 * <p>Hint 1: Use a stack — push open brackets; on a closing bracket, check the top.
 * <p>Hint 2: If the stack is non-empty at the end, the string is invalid.
 */
public class ValidParentheses {

    public boolean isValid(String s) {
        throw new UnsupportedOperationException("implement me");
    }
}
