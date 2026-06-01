package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — Partition Labels
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Greedy
 * <p>URL: https://leetcode.com/problems/partition-labels/
 *
 * <p>Given a string {@code s}, partition it into as many parts as possible so that each letter
 * appears in at most one part. Return a list of integers representing the size of each part.
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>1 &lt;= s.length &lt;= 500</li>
 *   <li>s consists of lowercase English letters only</li>
 * </ul>
 *
 * <p><b>Examples:</b>
 * <pre>
 * Input: s = "ababcbacadefegdehijhklij"  Output: [9,7,8]
 * Input: s = "eccbbbbdec"                Output: [10]
 * </pre>
 *
 * <p><b>Target:</b> O(n) time, O(1) space (26-letter alphabet)
 *
 * <p><b>Hint 1:</b> First pass: record the last occurrence index of each character.
 * <p><b>Hint 2:</b> Second pass: expand the current partition's end to the last occurrence of the
 * current character; when you reach the end, record the partition size and start a new one.
 */
public class PartitionLabels {

    /**
     * Returns the sizes of the maximum-count partitions where each letter appears in at most one part.
     *
     * @param s input string of lowercase letters
     * @return list of partition sizes in order
     */
    public List<Integer> partitionLabels(String s) {
        throw new UnsupportedOperationException("implement me");
    }
}
