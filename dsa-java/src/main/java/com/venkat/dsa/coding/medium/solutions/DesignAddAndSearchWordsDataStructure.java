package com.venkat.dsa.coding.medium.solutions;

/**
 * Design Add and Search Words Data Structure
 *
 * <p>Uses a Trie for storage. The challenge is the wildcard character {@code '.'}, which can match
 * any single letter. {@code search} does a DFS through the trie: at a {@code '.'} it tries all 26
 * children recursively; at a normal letter it follows the single matching branch.
 *
 * <p><b>Time complexity:</b> addWord O(m); search O(m) average, O(26^m) worst case (all dots).<br>
 * <b>Space complexity:</b> O(n * m) for the trie nodes.
 *
 * <p><b>Key insight:</b> Treat {@code '.'} as "branch and OR all children together" during DFS —
 * return true as soon as any branch succeeds.
 */
public class DesignAddAndSearchWordsDataStructure {

    // -----------------------------------------------------------------------
    // Trie node
    // -----------------------------------------------------------------------
    private static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        boolean isEnd;
    }

    private final TrieNode root;

    public DesignAddAndSearchWordsDataStructure() {
        root = new TrieNode();
    }

    /**
     * Adds a word into the data structure.
     * O(m) where m = word.length()
     */
    public void addWord(String word) {
        TrieNode cur = root;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (cur.children[idx] == null) {
                cur.children[idx] = new TrieNode();
            }
            cur = cur.children[idx];
        }
        cur.isEnd = true;
    }

    /**
     * Returns true if there is any word in the data structure that matches {@code word}.
     * {@code '.'} matches any single letter.
     * O(m) average; O(26^m) worst case with all wildcards.
     */
    public boolean search(String word) {
        return dfs(word, 0, root);
    }

    /**
     * DFS helper: matches word[index..] against the subtree rooted at {@code node}.
     */
    private boolean dfs(String word, int index, TrieNode node) {
        if (node == null) {
            return false; // fell off the trie
        }
        if (index == word.length()) {
            return node.isEnd; // consumed all chars — must be end of a stored word
        }

        char c = word.charAt(index);

        if (c == '.') {
            // wildcard: try every non-null child
            for (TrieNode child : node.children) {
                if (dfs(word, index + 1, child)) {
                    return true; // short-circuit on first match
                }
            }
            return false;
        } else {
            // normal letter: follow the single matching branch
            int idx = c - 'a';
            return dfs(word, index + 1, node.children[idx]);
        }
    }
}
