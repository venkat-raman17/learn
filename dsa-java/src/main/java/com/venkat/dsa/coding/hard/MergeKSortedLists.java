package com.venkat.dsa.coding.hard;

import java.util.List;

/**
 * NeetCode / LeetCode — Merge K Sorted Lists
 *
 * <p>Difficulty: HARD
 * <p>Pattern: Linked List
 * <p>URL: https://leetcode.com/problems/merge-k-sorted-lists/
 *
 * <p>Given an array of k linked lists, each sorted in ascending order, merge all the lists
 * into one sorted linked list and return its head.
 *
 * <p>Constraints:
 * <ul>
 *   <li>k == lists.length</li>
 *   <li>0 <= k <= 10^4</li>
 *   <li>0 <= lists[i].length <= 500</li>
 *   <li>-10^4 <= lists[i][j] <= 10^4</li>
 *   <li>lists[i] is sorted in ascending order.</li>
 *   <li>The sum of lists[i].length will not exceed 10^4.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: lists = [[1,4,5],[1,3,4],[2,6]]  ->  Output: [1,1,2,3,4,4,5,6]
 *   Input: lists = []                        ->  Output: []
 * </pre>
 *
 * <p>Target: O(N log k) time where N = total nodes, O(log k) space (recursion/heap)
 *
 * <p>Hint 1: Use a min-heap (PriorityQueue) seeded with the head of each list; always poll the
 *            minimum and push its successor.
 * <p>Hint 2: Alternatively, use divide-and-conquer: repeatedly merge pairs of lists until one
 *            remains, similar to merge sort.
 */
public class MergeKSortedLists {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int val) { this.val = val; }
    }

    public ListNode mergeKLists(ListNode[] lists) {
        throw new UnsupportedOperationException("implement me");
    }
}
