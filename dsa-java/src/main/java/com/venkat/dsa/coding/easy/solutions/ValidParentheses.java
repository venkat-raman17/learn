package com.venkat.dsa.coding.easy.solutions;

import java.util.Deque;
import java.util.ArrayDeque;

/**
 * Valid Parentheses (LeetCode #20)
 *
 * Approach: Use a stack to track opening brackets. For each character in the
 * string, push opening brackets onto the stack. When a closing bracket is
 * encountered, check if the top of the stack holds the matching opener —
 * if not (or the stack is empty), the string is invalid.
 *
 * Key insight: Every closing bracket must match the most recently seen
 * unmatched opening bracket; a stack naturally models this LIFO pairing.
 *
 * Time complexity:  O(n) — single pass through the string.
 * Space complexity: O(n) — stack can hold up to n/2 opening brackets.
 */
public class ValidParentheses {

    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();

        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else {
                // Closing bracket — stack must have a matching opener on top
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if (c == ')' && top != '(') return false;
                if (c == ']' && top != '[') return false;
                if (c == '}' && top != '{') return false;
            }
        }

        // Valid only if all openers were matched
        return stack.isEmpty();
    }
}
