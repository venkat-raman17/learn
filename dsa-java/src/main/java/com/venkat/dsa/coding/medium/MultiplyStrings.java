package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Multiply Strings
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Math & Geometry
 * <p>URL: https://leetcode.com/problems/multiply-strings/
 *
 * <p>Given two non-negative integers num1 and num2 represented as strings, return the product
 * of num1 and num2 as a string. You must not convert the inputs to integers directly.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= num1.length, num2.length <= 200</li>
 *   <li>num1 and num2 consist of digits only</li>
 *   <li>Both num1 and num2 do not have leading zeros, except the number "0" itself</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   multiply("2", "3")   -> "6"
 *   multiply("123", "456") -> "56088"
 * </pre>
 *
 * <p>Target: Time O(m*n), Space O(m+n) where m and n are the lengths of the inputs.
 *
 * <p>Hint 1: Use an int array of size m+n to accumulate partial products digit by digit.
 * <p>Hint 2: The product of num1[i] and num2[j] contributes to positions i+j and i+j+1 in the result array.
 */
public class MultiplyStrings {

    public String multiply(String num1, String num2) {
        throw new UnsupportedOperationException("implement me");
    }
}
