package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Letter Combinations of a Phone Number (LeetCode 17)
 *
 * Approach: Backtracking — map each digit to its letters, then for each position in the
 * digits string pick one letter and recurse to the next position. When the current
 * combination length equals digits.length(), record it.
 *
 * Key insight: the digit-to-letter mapping is fixed; the backtracking tree has depth
 * equal to digits.length() with branching factor 3 or 4, giving at most 4^n leaves.
 *
 * Time:  O(4^n * n) — n = digits length; 4^n combinations each of length n.
 * Space: O(n) recursion depth.
 */
public class LetterCombinationsPhoneNumber {

    private static final Map<Character, String> PHONE = Map.of(
        '2', "abc", '3', "def",  '4', "ghi", '5', "jkl",
        '6', "mno", '7', "pqrs", '8', "tuv", '9', "wxyz"
    );

    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        if (digits == null || digits.isEmpty()) return result;
        backtrack(digits, 0, new StringBuilder(), result);
        return result;
    }

    private void backtrack(String digits, int index, StringBuilder current, List<String> result) {
        if (index == digits.length()) {
            result.add(current.toString());
            return;
        }

        String letters = PHONE.get(digits.charAt(index));
        for (char ch : letters.toCharArray()) {
            current.append(ch);
            backtrack(digits, index + 1, current, result);
            current.deleteCharAt(current.length() - 1);
        }
    }
}
