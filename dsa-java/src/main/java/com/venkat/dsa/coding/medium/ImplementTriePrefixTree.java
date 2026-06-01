package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Implement Trie (Prefix Tree)
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Tries
 * <p>URL: https://leetcode.com/problems/implement-trie-prefix-tree/
 *
 * <p>Restatement:
 * Design a Trie data structure that supports inserting a word, searching for an exact word,
 * and checking whether any word in the trie starts with a given prefix.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= word.length, prefix.length <= 2000</li>
 *   <li>word and prefix consist only of lowercase English letters.</li>
 *   <li>At most 3 * 10^4 calls to insert, search, and startsWith.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Example 1:
 *   Input:  insert("apple"), search("apple") -> true, search("app") -> false,
 *           startsWith("app") -> true, insert("app"), search("app") -> true
 *
 * Example 2:
 *   Input:  insert("car"), search("car") -> true, startsWith("ca") -> true,
 *           search("ca") -> false
 * </pre>
 *
 * <p>Target: Time O(m) per operation where m = word length; Space O(m * n) total nodes.
 *
 * <p>Hints:
 * <ol>
 *   <li>Each TrieNode has an array of 26 children (one per letter) and a boolean isEnd flag.</li>
 *   <li>For insert/search/startsWith, traverse characters one by one, creating nodes as needed
 *       for insert, returning false early if a node is missing for search/startsWith.</li>
 * </ol>
 */
public class ImplementTriePrefixTree {

    private final TrieNode root;

    public ImplementTriePrefixTree() {
        root = new TrieNode();
    }

    public void insert(String word) {
        throw new UnsupportedOperationException("implement me");
    }

    public boolean search(String word) {
        throw new UnsupportedOperationException("implement me");
    }

    public boolean startsWith(String prefix) {
        throw new UnsupportedOperationException("implement me");
    }

    // Minimal nested TrieNode used by this class
    public static class TrieNode {
        public TrieNode[] children;
        public boolean isEnd;

        public TrieNode() {
            children = new TrieNode[26];
            isEnd = false;
        }
    }
}
