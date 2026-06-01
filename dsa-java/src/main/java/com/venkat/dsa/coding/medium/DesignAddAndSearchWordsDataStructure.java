package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Design Add and Search Words Data Structure
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Tries
 * <p>URL: https://leetcode.com/problems/design-add-and-search-words-data-structure/
 *
 * <p>Restatement:
 * Design a data structure that supports adding words and searching for words where '.' can
 * match any single letter (wildcard search). Implement addWord and search methods.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= word.length <= 25</li>
 *   <li>word in addWord consists of lowercase English letters.</li>
 *   <li>word in search consists of '.' or lowercase English letters.</li>
 *   <li>At most 10^4 calls to addWord and search.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Example 1:
 *   Input:  addWord("bad"), addWord("dad"), addWord("mad")
 *           search("pad") -> false, search("bad") -> true,
 *           search(".ad") -> true, search("b..") -> true
 *
 * Example 2:
 *   Input:  addWord("a"), search("a") -> true, search(".") -> true
 * </pre>
 *
 * <p>Target: Time O(m) for addWord, O(26^m) worst case for search with many dots; Space O(m * n).
 *
 * <p>Hints:
 * <ol>
 *   <li>Build a standard Trie for addWord; for search, recursively handle '.' by trying all
 *       26 child nodes at that level.</li>
 *   <li>Use a helper DFS method that takes the current TrieNode and current character index,
 *       branching on every child when the character is '.'.</li>
 * </ol>
 */
public class DesignAddAndSearchWordsDataStructure {

    private final WordNode root;

    public DesignAddAndSearchWordsDataStructure() {
        root = new WordNode();
    }

    public void addWord(String word) {
        throw new UnsupportedOperationException("implement me");
    }

    public boolean search(String word) {
        throw new UnsupportedOperationException("implement me");
    }

    // Minimal nested node for this trie
    public static class WordNode {
        public WordNode[] children;
        public boolean isEnd;

        public WordNode() {
            children = new WordNode[26];
            isEnd = false;
        }
    }
}
