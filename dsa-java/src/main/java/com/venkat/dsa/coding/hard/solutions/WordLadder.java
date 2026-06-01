package com.venkat.dsa.coding.hard.solutions;

import java.util.*;

/**
 * Word Ladder (LeetCode 127)
 *
 * <p>Uses BFS on an implicit word graph where edges connect words differing by exactly one
 * character. Starting from {@code beginWord}, each BFS level represents one transformation step.
 * A {@code HashSet} of the word list enables O(1) membership checks, and matched words are removed
 * from the set to avoid revisiting. Returns the length of the shortest transformation sequence
 * (including begin and end words), or 0 if unreachable.
 *
 * <p><b>Key insight:</b> BFS on an unweighted graph finds the shortest path in terms of hops;
 * generating all 26-letter substitutions per position is O(26 * L) per word and avoids building
 * the full O(N^2) adjacency list.
 *
 * <p><b>Time:</b> O(N * L^2 * 26) where N = dictionary size, L = word length.<br>
 * <b>Space:</b> O(N * L) for the word set and BFS queue.
 */
public class WordLadder {

    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;

        Queue<String> queue = new ArrayDeque<>();
        queue.offer(beginWord);
        wordSet.remove(beginWord); // treat beginWord as visited

        int steps = 1;

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String word = queue.poll();
                char[] chars = word.toCharArray();

                // Try every position and every letter a-z
                for (int pos = 0; pos < chars.length; pos++) {
                    char original = chars[pos];
                    for (char ch = 'a'; ch <= 'z'; ch++) {
                        if (ch == original) continue;
                        chars[pos] = ch;
                        String next = new String(chars);
                        if (next.equals(endWord)) return steps + 1;
                        if (wordSet.contains(next)) {
                            wordSet.remove(next); // remove to prevent re-enqueuing
                            queue.offer(next);
                        }
                    }
                    chars[pos] = original; // restore for next position
                }
            }
            steps++;
        }

        return 0; // endWord unreachable
    }
}
