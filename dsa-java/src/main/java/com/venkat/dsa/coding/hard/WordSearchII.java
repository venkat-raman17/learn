package com.venkat.dsa.coding.hard;

import java.util.List;

/**
 * NeetCode / LeetCode — Word Search II
 *
 * <p>Difficulty: HARD
 * <p>Pattern: Tries
 * <p>URL: https://leetcode.com/problems/word-search-ii/
 *
 * <p>Restatement:
 * Given an m x n character grid and a list of words, return all words that can be formed by
 * sequentially adjacent cells (horizontally or vertically), where each cell may not be reused
 * within a single word path.
 *
 * <p>Constraints:
 * <ul>
 *   <li>m == board.length, n == board[i].length</li>
 *   <li>1 <= m, n <= 12</li>
 *   <li>board[i][j] is a lowercase English letter.</li>
 *   <li>1 <= words.length <= 3 * 10^4</li>
 *   <li>1 <= words[i].length <= 10</li>
 *   <li>words[i] consists of lowercase English letters.</li>
 *   <li>All strings in words are unique.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Example 1:
 *   Input:  board = [["o","a","a","n"],["e","t","a","e"],["i","h","k","r"],["i","f","l","v"]],
 *           words = ["oath","pea","eat","rain"]
 *   Output: ["eat","oath"]
 *
 * Example 2:
 *   Input:  board = [["a","b"],["c","d"]], words = ["abcb"]
 *   Output: []
 * </pre>
 *
 * <p>Target: Time O(m * n * 4 * 3^(L-1)) per word search where L = max word length; Space O(W * L) for Trie.
 *
 * <p>Hints:
 * <ol>
 *   <li>Insert all words into a Trie first, then DFS from each cell on the board, pruning branches
 *       that have no matching Trie prefix — this avoids redundant searches across shared prefixes.</li>
 *   <li>When a word is found during DFS, mark it in the Trie (e.g., clear the word string) to avoid
 *       adding duplicates; also prune dead Trie branches (leaf nodes with no word) to speed up search.</li>
 * </ol>
 */
public class WordSearchII {

    public List<String> findWords(char[][] board, String[] words) {
        throw new UnsupportedOperationException("implement me");
    }

    // Minimal nested TrieNode used by this problem
    public static class TrieNode {
        public TrieNode[] children;
        public String word; // non-null when this node marks the end of a word

        public TrieNode() {
            children = new TrieNode[26];
            word = null;
        }
    }
}
