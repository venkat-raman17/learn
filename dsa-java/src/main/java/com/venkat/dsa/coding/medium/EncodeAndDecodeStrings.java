package com.venkat.dsa.coding.medium;

import java.util.ArrayList;
import java.util.List;

/**
 * NeetCode / LeetCode — Encode and Decode Strings
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Arrays &amp; Hashing
 * <p>URL: https://leetcode.com/problems/encode-and-decode-strings/
 *
 * <p>Design an algorithm to encode a list of strings to a single string, and decode that single
 * string back to the original list. The encoded string must be transmittable over a network and
 * must survive round-trip (encode then decode returns the original list).
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>0 &lt;= strs.length &lt;= 200</li>
 *   <li>0 &lt;= strs[i].length &lt;= 200</li>
 *   <li>strs[i] contains any possible character including special characters</li>
 * </ul>
 *
 * <p><b>Examples:</b>
 * <pre>
 *   Input: ["lint","code","love","you"]  →  encode  →  decode  →  ["lint","code","love","you"]
 *   Input: [""]                          →  encode  →  decode  →  [""]
 * </pre>
 *
 * <p><b>Target:</b> Time O(n) encode and O(n) decode, Space O(n)
 *
 * <p><b>Hint 1:</b> Prefix each word with its length and a delimiter (e.g. "4#lint") so the decoder
 *   can read the exact number of characters without ambiguity.
 * <p><b>Hint 2:</b> In the decode loop, read digits up to '#', parse length, then substring exactly
 *   that many characters — no risk of confusion with special characters inside the string.
 */
public class EncodeAndDecodeStrings {

    public String encode(List<String> strs) {
        throw new UnsupportedOperationException("implement me");
    }

    public List<String> decode(String s) {
        throw new UnsupportedOperationException("implement me");
    }
}
