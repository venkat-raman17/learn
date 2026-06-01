package com.venkat.dsa.coding.hard;

import java.util.*;

/**
 * NeetCode / LeetCode — Alien Dictionary
 *
 * <p>Difficulty: HARD
 * <p>Pattern: Advanced Graphs
 * <p>URL: https://leetcode.com/problems/alien-dictionary/
 *
 * <p>Given a sorted list of words from an alien language, deduce the order of characters
 * in that language's alphabet. Return any valid ordering. If no valid ordering exists
 * (due to a cycle), return "". If the order cannot be determined (insufficient info),
 * any valid result is accepted.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= words.length &lt;= 100</li>
 *   <li>1 &lt;= words[i].length &lt;= 100</li>
 *   <li>words[i] consists of lowercase English letters only.</li>
 *   <li>All characters in all words are in the range ['a','z'].</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  words = ["wrt","wrf","er","ett","rftt"]
 *   Output: "wertf"
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  words = ["z","x"]
 *   Output: "zx"
 * </pre>
 *
 * <p>Target Time: O(C) where C = total length of all words &nbsp; Space: O(1) — at most 26 chars
 *
 * <p>Hint 1: Compare adjacent words character-by-character to extract ordering constraints
 *            (edges in a directed graph). Watch out for the invalid prefix case
 *            (longer word appears before its prefix).
 * <p>Hint 2: Perform topological sort (Kahn's BFS or DFS with cycle detection) on the
 *            character graph; a cycle means return "".
 */
public class AlienDictionary {

    /**
     * Returns a string representing the lexicographic order of characters in the alien
     * alphabet, or "" if the ordering is invalid (cycle detected or prefix contradiction).
     *
     * @param words sorted list of words in the alien language
     * @return valid character ordering, or "" if none exists
     */
    public String alienOrder(String[] words) {
        throw new UnsupportedOperationException("implement me");
    }
}
