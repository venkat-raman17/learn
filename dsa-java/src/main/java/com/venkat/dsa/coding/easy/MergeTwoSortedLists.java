package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Merge Two Sorted Lists
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Linked List
 * <p>URL: https://leetcode.com/problems/merge-two-sorted-lists/
 *
 * <p>Merge two sorted singly linked lists and return the head of the merged sorted list.
 * The merged list should be made by splicing together the nodes of the two input lists.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in both lists is in the range [0, 50].</li>
 *   <li>-100 <= Node.val <= 100</li>
 *   <li>Both list1 and list2 are sorted in non-decreasing order.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: list1 = [1,2,4], list2 = [1,3,4]  ->  Output: [1,1,2,3,4,4]
 *   Input: list1 = [], list2 = []             ->  Output: []
 * </pre>
 *
 * <p>Target: O(n + m) time, O(1) space
 *
 * <p>Hint 1: Use a dummy head node so you never need a special case for the start.
 * <p>Hint 2: At each step compare the current heads of both lists, attach the smaller, advance that pointer.
 */
public class MergeTwoSortedLists {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int val) { this.val = val; }
        public ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        throw new UnsupportedOperationException("implement me");
    }
}
