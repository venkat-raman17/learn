package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — Letter Combinations of a Phone Number
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Backtracking
 * <p>URL: https://leetcode.com/problems/letter-combinations-of-a-phone-number/
 *
 * <p>Given a string containing digits from 2–9 inclusive, return all possible letter combinations
 * that the number could represent on a phone keypad. Return the answer in any order.
 * If the input is empty, return an empty list.
 *
 * <p>Constraints:
 * <ul>
 *   <li>0 &lt;= digits.length &lt;= 4</li>
 *   <li>digits[i] is a digit in the range ['2', '9'].</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: digits = "23"
 *   Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]
 *
 *   Input: digits = ""
 *   Output: []
 *
 *   Input: digits = "2"
 *   Output: ["a","b","c"]
 * </pre>
 *
 * <p>Target: Time O(4^n * n), Space O(n)
 *
 * <p>Hint 1: Map each digit to its corresponding letters; then use backtracking to append
 * one letter per digit position, advancing an index through the digits string.
 * <p>Hint 2: Return early (empty list) if the input string is empty or null.
 */
public class LetterCombinationsOfAPhoneNumber {

    public List<String> letterCombinations(String digits) {
        throw new UnsupportedOperationException("implement me");
    }
}
