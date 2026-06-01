package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Decode Ways
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 1-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/decode-ways/
 *
 * <p>A message encoded as digits maps letters A-Z to 1-26. Given a string {@code s} of digits,
 * return the number of ways to decode it. A leading zero or an invalid two-digit number has
 * zero valid decodings.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= s.length &lt;= 100</li>
 *   <li>{@code s} contains only digits and may contain leading zeros.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Input: s = "12"   -> Output: 2  ("AB" or "L")
 * Input: s = "226"  -> Output: 3  ("BZ", "VF", "BBF")
 * Input: s = "06"   -> Output: 0
 * </pre>
 *
 * <p>Target: Time O(n), Space O(1)
 *
 * <p>Hint 1: dp[i] = ways to decode s[0..i-1]. A single digit s[i-1] != '0' contributes dp[i-1];
 *            a valid two-digit s[i-2..i-1] in [10,26] contributes dp[i-2].
 * <p>Hint 2: Only two previous values are needed; use two variables instead of an array.
 */
public class DecodeWays {

    public int numDecodings(String s) {
        throw new UnsupportedOperationException("implement me");
    }
}
