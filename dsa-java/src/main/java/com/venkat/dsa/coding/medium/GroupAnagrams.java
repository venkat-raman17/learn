package com.venkat.dsa.coding.medium;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * NeetCode / LeetCode — Group Anagrams
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Arrays &amp; Hashing
 * <p>URL: https://leetcode.com/problems/group-anagrams/
 *
 * <p>Given an array of strings {@code strs}, group the anagrams together and return them in any
 * order. Two strings are anagrams if one can be rearranged to form the other.
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>1 &lt;= strs.length &lt;= 10^4</li>
 *   <li>0 &lt;= strs[i].length &lt;= 100</li>
 *   <li>strs[i] consists of lowercase English letters</li>
 * </ul>
 *
 * <p><b>Examples:</b>
 * <pre>
 *   Input: ["eat","tea","tan","ate","nat","bat"]
 *   Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
 *
 *   Input: [""]  →  Output: [[""]]
 * </pre>
 *
 * <p><b>Target:</b> Time O(n * k log k) where k = max string length, Space O(n * k)
 *
 * <p><b>Hint 1:</b> Sorting each string produces the same key for all anagrams — use it as a HashMap key.
 * <p><b>Hint 2:</b> Alternatively, build a frequency-count key (e.g., "a2b1...") in O(k) to avoid the sort.
 */
public class GroupAnagrams {

    public List<List<String>> groupAnagrams(String[] strs) {
        throw new UnsupportedOperationException("implement me");
    }
}
