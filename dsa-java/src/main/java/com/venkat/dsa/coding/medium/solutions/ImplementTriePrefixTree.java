package com.venkat.dsa.coding.medium.solutions;

/**
 * Implement Trie (Prefix Tree)
 *
 * <p>A Trie is a tree data structure where each node stores one character and branches to children
 * for the next characters. Insert, search, and startsWith all walk the trie character by character.
 * A boolean flag on each node marks whether a complete word ends there.
 *
 * <p><b>Time complexity:</b> O(m) per operation where m = length of the word/prefix.<br>
 * <b>Space complexity:</b> O(n * m) total where n = number of inserted words and m = average length.
 *
 * <p><b>Key insight:</b> Each of the 26 lowercase letters maps to a fixed children[] slot; no
 * hashing needed. Walking to a null child means the prefix/word does not exist.
 */
public class ImplementTriePrefixTree {

    // -----------------------------------------------------------------------
    // Trie node: 26-way branching for 'a'..'z'
    // -----------------------------------------------------------------------
    private static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        boolean isEnd; // true when a complete word ends here
    }

    private final TrieNode root;

    public ImplementTriePrefixTree() {
        root = new TrieNode();
    }

    /**
     * Inserts a word into the trie.
     * O(m) where m = word.length()
     */
    public void insert(String word) {
        TrieNode cur = root;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (cur.children[idx] == null) {
                cur.children[idx] = new TrieNode(); // create node on demand
            }
            cur = cur.children[idx];
        }
        cur.isEnd = true; // mark end of word
    }

    /**
     * Returns true if the word exists in the trie (exact match).
     * O(m) where m = word.length()
     */
    public boolean search(String word) {
        TrieNode node = walkTo(word);
        return node != null && node.isEnd; // must reach node AND be a complete word
    }

    /**
     * Returns true if any word in the trie starts with the given prefix.
     * O(m) where m = prefix.length()
     */
    public boolean startsWith(String prefix) {
        return walkTo(prefix) != null; // any reachable node counts
    }

    /**
     * Walks the trie following characters of {@code s}.
     * Returns the final node, or null if any character has no branch.
     */
    private TrieNode walkTo(String s) {
        TrieNode cur = root;
        for (char c : s.toCharArray()) {
            int idx = c - 'a';
            if (cur.children[idx] == null) {
                return null; // prefix/word not found
            }
            cur = cur.children[idx];
        }
        return cur;
    }
}
