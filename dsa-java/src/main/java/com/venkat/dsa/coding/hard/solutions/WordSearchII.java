package com.venkat.dsa.coding.hard.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Word Search II
 *
 * <p>Inserts all target words into a Trie, then runs a single DFS from every cell on the board.
 * Walking the trie in lock-step with the DFS prunes whole branches early: if the current path
 * prefix is not in the trie we stop immediately instead of trying all remaining words. When a
 * trie node's {@code word} field is non-null we have found a complete match.
 *
 * <p><b>Time complexity:</b> O(M * 4 * 3^(L-1)) where M = board cells and L = max word length
 * (each DFS step has at most 3 unexplored neighbours after the first move). Building the trie is
 * O(W) where W = total characters in all words.<br>
 * <b>Space complexity:</b> O(W) for the trie + O(L) recursion stack.
 *
 * <p><b>Key insight:</b> After finding a word, null out {@code node.word} so it is never returned
 * twice (handles duplicate starting paths). Decrement {@code refs} on found words so exhausted
 * trie branches are skipped by future DFS calls (pruning).
 */
public class WordSearchII {

    // -----------------------------------------------------------------------
    // Trie node
    // -----------------------------------------------------------------------
    private static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        String word; // non-null only at a terminal (complete-word) node
        int refs;    // number of words still unfound that pass through this node
    }

    private static final int[][] DIRS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    // -----------------------------------------------------------------------
    // Public API
    // -----------------------------------------------------------------------

    /**
     * Returns every word from {@code words} that can be constructed from sequentially-adjacent
     * cells on the board (no cell reused within one word).
     */
    public List<String> findWords(char[][] board, String[] words) {
        TrieNode root = buildTrie(words);
        List<String> result = new ArrayList<>();

        int rows = board.length, cols = board[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                dfs(board, r, c, root, result);
            }
        }
        return result;
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    /** Build a trie from all words; each node tracks how many words pass through it. */
    private TrieNode buildTrie(String[] words) {
        TrieNode root = new TrieNode();
        for (String w : words) {
            TrieNode cur = root;
            for (char c : w.toCharArray()) {
                int i = c - 'a';
                if (cur.children[i] == null) {
                    cur.children[i] = new TrieNode();
                }
                cur = cur.children[i];
                cur.refs++; // this word passes through cur
            }
            cur.word = w; // mark terminal
        }
        return root;
    }

    /**
     * DFS from cell (r, c) while following the trie from {@code node}.
     * Uses '#' as a visited sentinel; restores the cell on backtrack.
     */
    private void dfs(char[][] board, int r, int c, TrieNode node, List<String> result) {
        char ch = board[r][c];
        if (ch == '#') return; // cell already used in the current path

        int idx = ch - 'a';
        TrieNode child = node.children[idx];

        // child == null  → prefix not in trie
        // child.refs == 0 → all words through this node already found (pruned)
        if (child == null || child.refs == 0) return;

        if (child.word != null) {
            result.add(child.word);
            child.word = null; // prevent duplicate entries via different paths
            child.refs--;      // one fewer unfound word passes through child
        }

        board[r][c] = '#'; // mark visited for this DFS path

        for (int[] d : DIRS) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < board.length && nc >= 0 && nc < board[0].length) {
                dfs(board, nr, nc, child, result);
            }
        }

        board[r][c] = ch; // backtrack: restore original character
    }
}
