package com.venkat.dsa.coding.medium.solutions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Word Break (LeetCode 139)
 *
 * Approach: Bottom-up DP. Let dp[i] = true if s[0..i-1] can be segmented
 * using words from the dictionary. For every position i we check all j < i
 * where dp[j] is true and s[j..i-1] is a valid dictionary word.
 *
 * Key insight: Storing dictionary words in a HashSet gives O(1) look-up.
 * We only need dp[0..n] and a forward scan, so no memoised recursion is needed.
 *
 * Time:  O(n^2) — n positions × n substrings per position
 * Space: O(n + m) — dp array + dictionary set (m = total chars in wordDict)
 */
public class WordBreak {

    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true; // empty prefix is always segmentable

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                // if s[0..j-1] is segmentable and s[j..i-1] is a word, mark dp[i]
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break; // no need to check further j values
                }
            }
        }
        return dp[n];
    }
}
