package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — Word Break
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: 1-D Dynamic Programming
 * <p>URL: https://leetcode.com/problems/word-break/
 *
 * <p>Given a string {@code s} and a list of strings {@code wordDict}, return {@code true} if
 * {@code s} can be segmented into a space-separated sequence of one or more dictionary words.
 * Words in the dictionary may be reused.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= s.length &lt;= 300</li>
 *   <li>1 &lt;= wordDict.length &lt;= 1000</li>
 *   <li>1 &lt;= wordDict[i].length &lt;= 20</li>
 *   <li>{@code s} and {@code wordDict[i]} consist of only lowercase English letters.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Input: s = "leetcode",  wordDict = ["leet","code"]    -> Output: true
 * Input: s = "applepenapple", wordDict = ["apple","pen"] -> Output: true
 * Input: s = "catsandog",  wordDict = ["cats","dog","sand","and","cat"] -> Output: false
 * </pre>
 *
 * <p>Target: Time O(n^2 * m) where m = max word length, Space O(n)
 *
 * <p>Hint 1: Let dp[i] = true if s[0..i-1] can be segmented. For each i, check every j &lt; i
 *            where dp[j] is true and s[j..i-1] is in the dictionary.
 * <p>Hint 2: Store wordDict in a HashSet for O(1) lookups; dp[0] = true is the base case.
 */
public class WordBreak {

    public boolean wordBreak(String s, List<String> wordDict) {
        throw new UnsupportedOperationException("implement me");
    }
}
