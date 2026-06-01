package com.venkat.dsa.coding.hard.solutions;

import java.util.*;

/**
 * Alien Dictionary (LeetCode 269)
 *
 * <p>Derives a character ordering from a lexicographically sorted list of alien words by comparing
 * adjacent words to extract ordering constraints, then performs a topological sort (Kahn's BFS
 * algorithm) over the resulting DAG of characters. If a cycle exists (invalid input) or a prefix
 * word appears after a longer word it extends, returns {@code ""}.
 *
 * <p><b>Key insight:</b> Each adjacent pair of words reveals at most one ordering edge — the first
 * position where they differ gives {@code words[i][j] → words[i+1][j]}. All characters that
 * appear in any word must be present as nodes even if they have no ordering edges.
 *
 * <p><b>Time:</b> O(C) where C = total length of all words (building edges) + O(V + E) for BFS.<br>
 * <b>Space:</b> O(1) — at most 26 characters, so effectively O(1) regardless of input size.
 */
public class AlienDictionary {

    public String alienOrder(String[] words) {
        // Initialize adjacency list and in-degree map for every character that appears
        Map<Character, Set<Character>> adj = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();

        for (String word : words) {
            for (char c : word.toCharArray()) {
                adj.putIfAbsent(c, new HashSet<>());
                inDegree.putIfAbsent(c, 0);
            }
        }

        // Compare adjacent words to derive ordering edges
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i], w2 = words[i + 1];
            int minLen = Math.min(w1.length(), w2.length());
            boolean foundDiff = false;
            for (int j = 0; j < minLen; j++) {
                char c1 = w1.charAt(j), c2 = w2.charAt(j);
                if (c1 != c2) {
                    // c1 must come before c2
                    if (!adj.get(c1).contains(c2)) {
                        adj.get(c1).add(c2);
                        inDegree.put(c2, inDegree.get(c2) + 1);
                    }
                    foundDiff = true;
                    break;
                }
            }
            // Invalid: prefix word appears after its extension (e.g., "abc" before "ab")
            if (!foundDiff && w1.length() > w2.length()) {
                return "";
            }
        }

        // Kahn's BFS topological sort
        Queue<Character> queue = new LinkedList<>();
        for (Map.Entry<Character, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            char c = queue.poll();
            sb.append(c);
            for (char neighbor : adj.get(c)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // If not all characters are in the result, there is a cycle
        if (sb.length() != inDegree.size()) return "";
        return sb.toString();
    }
}
