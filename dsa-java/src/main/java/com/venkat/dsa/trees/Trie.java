package com.venkat.dsa.trees;

import java.util.Objects;

/**
 * Trie (Prefix Tree) — a multiway tree for storing strings character by character.
 *
 * <p><b>Backing representation:</b> Each node holds a fixed {@code children[26]} array
 * indexed by {@code ch - 'a'}, plus a boolean {@code isEndOfWord} flag. The root node
 * represents the empty string and is never marked as a word endpoint itself.
 *
 * <p><b>Invariants:</b>
 * <ul>
 *   <li>Only lowercase a–z characters are accepted; any other input throws
 *       {@link IllegalArgumentException}.</li>
 *   <li>A node exists at depth {@code d} only if at least one stored word has a
 *       character at that position along that path.</li>
 *   <li>{@code isEndOfWord == true} on node N iff the path from root to N spells
 *       at least one word that was inserted.</li>
 * </ul>
 *
 * <p><b>Operations — Time &amp; Space Complexity:</b>
 * <pre>
 * ┌──────────────────┬──────────────┬─────────────────────────────────────────┐
 * │ Operation        │ Time         │ Space (auxiliary)                       │
 * ├──────────────────┼──────────────┼─────────────────────────────────────────┤
 * │ insert(word)     │ O(L)         │ O(L) worst case (new nodes created)     │
 * │ search(word)     │ O(L)         │ O(1)                                    │
 * │ startsWith(pfx)  │ O(P)         │ O(1)                                    │
 * └──────────────────┴──────────────┴─────────────────────────────────────────┘
 * L = length of the word, P = length of the prefix.
 * Each node allocates a 26-element array; total space is O(SIGMA * N) where
 * SIGMA = 26 and N = total characters stored across all words (worst case no
 * shared prefixes).
 * </pre>
 *
 * <p><b>When to use:</b> Tries excel at prefix-based queries (autocomplete,
 * spell-check, IP routing) where a hash map would require iterating all keys.
 * Prefer a trie when you need {@code startsWith} in O(P) time and the alphabet
 * is bounded and small.
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Compared with HashMap: trie avoids re-hashing and supports ordered/prefix
 *       iteration; hash map has better average-case for exact lookup with large
 *       alphabets.</li>
 *   <li>Space trade-off: the fixed {@code children[26]} array wastes memory when
 *       the trie is sparse; a {@code HashMap&lt;Character, TrieNode&gt;} per node
 *       trades CPU for memory.</li>
 *   <li>Common follow-ups: delete a word (mark {@code isEndOfWord = false}, prune
 *       empty branches), count words with prefix, wildcard search via DFS/BFS.</li>
 *   <li>LeetCode 208 "Implement Trie (Prefix Tree)" is the canonical problem.</li>
 * </ul>
 */
public class Trie {

    // -------------------------------------------------------------------------
    // Inner node class
    // -------------------------------------------------------------------------

    private static final class TrieNode {
        /** One slot per lowercase letter; {@code null} means the edge is absent. */
        final TrieNode[] children = new TrieNode[26];
        /** True iff this node marks the end of at least one inserted word. */
        boolean isEndOfWord;
    }

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private final TrieNode root;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /** Creates an empty Trie. */
    public Trie() {
        root = new TrieNode();
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Inserts {@code word} into the trie.
     *
     * <p>Time: O(L), where L = {@code word.length()}.
     * Space: O(L) auxiliary in the worst case (all characters are new nodes).
     *
     * @param word a non-null, non-empty string of lowercase a–z characters
     * @throws IllegalArgumentException if {@code word} is empty or contains a
     *                                  character outside {@code [a-z]}
     * @throws NullPointerException     if {@code word} is {@code null}
     */
    public void insert(String word) {
        Objects.requireNonNull(word, "word must not be null");
        validate(word);
        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            int idx = ch - 'a';
            if (current.children[idx] == null) {
                current.children[idx] = new TrieNode();
            }
            current = current.children[idx];
        }
        current.isEndOfWord = true;
    }

    /**
     * Returns {@code true} iff {@code word} was previously inserted (exact match).
     *
     * <p>Time: O(L), where L = {@code word.length()}.
     *
     * @param word a non-null string of lowercase a–z characters
     * @throws IllegalArgumentException if {@code word} contains a character outside
     *                                  {@code [a-z]}
     * @throws NullPointerException     if {@code word} is {@code null}
     */
    public boolean search(String word) {
        Objects.requireNonNull(word, "word must not be null");
        if (word.isEmpty()) {
            return false;
        }
        validate(word);
        TrieNode node = walkTo(word);
        return node != null && node.isEndOfWord;
    }

    /**
     * Returns {@code true} iff at least one inserted word begins with {@code prefix}.
     *
     * <p>Time: O(P), where P = {@code prefix.length()}.
     *
     * @param prefix a non-null string of lowercase a–z characters; the empty
     *               string is considered a prefix of every word
     * @throws IllegalArgumentException if {@code prefix} contains a character
     *                                  outside {@code [a-z]}
     * @throws NullPointerException     if {@code prefix} is {@code null}
     */
    public boolean startsWith(String prefix) {
        Objects.requireNonNull(prefix, "prefix must not be null");
        if (prefix.isEmpty()) {
            return true; // empty prefix matches every word
        }
        validate(prefix);
        return walkTo(prefix) != null;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Walks the trie following the characters of {@code s} and returns the node
     * reached after consuming all characters, or {@code null} if any edge is missing.
     */
    private TrieNode walkTo(String s) {
        TrieNode current = root;
        for (char ch : s.toCharArray()) {
            int idx = ch - 'a';
            if (current.children[idx] == null) {
                return null;
            }
            current = current.children[idx];
        }
        return current;
    }

    /**
     * Validates that every character in {@code s} is a lowercase ASCII letter.
     *
     * @throws IllegalArgumentException if any character is outside {@code [a-z]}
     */
    private static void validate(String s) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch < 'a' || ch > 'z') {
                throw new IllegalArgumentException(
                        "Character '" + ch + "' at index " + i
                        + " is not a lowercase a-z letter.");
            }
        }
    }
}
