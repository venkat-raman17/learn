package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Group Anagrams (LeetCode 49) — Medium
 *
 * <p>Approach: Sort each word's characters to produce a canonical key; words that are
 * anagrams of each other share the same sorted key. Use a HashMap from sorted-key to
 * list of original words, then return the map's values.
 *
 * <p><b>Time complexity:</b> O(n · k log k) where n = number of strings and k = max string
 * length (sorting each string dominates).<br>
 * <b>Space complexity:</b> O(n · k) — to store all strings in the map.
 *
 * <p><b>Key insight:</b> Sorting is the simplest canonical form for anagram grouping;
 * an alternative is a 26-element frequency-count key which runs in O(n · k) time.
 */
public class GroupAnagrams {

    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> groups = new HashMap<>();
        for (String word : strs) {
            char[] chars = word.toCharArray();
            Arrays.sort(chars);                      // canonical anagram key
            String key = new String(chars);
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(word);
        }
        return new ArrayList<>(groups.values());
    }
}
