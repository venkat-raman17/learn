package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Add Two Numbers
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Linked List
 * <p>URL: https://leetcode.com/problems/add-two-numbers/
 *
 * <p>Two non-empty linked lists represent two non-negative integers stored in reverse order
 * (each node contains a single digit). Add the two numbers and return the sum as a linked list
 * also in reverse order.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in each list is in the range [1, 100].</li>
 *   <li>0 <= Node.val <= 9</li>
 *   <li>The numbers do not have leading zeros (except the number 0 itself).</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: l1 = [2,4,3], l2 = [5,6,4]   ->  Output: [7,0,8]   (342 + 465 = 807)
 *   Input: l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]  ->  Output: [8,9,9,9,0,0,0,1]
 * </pre>
 *
 * <p>Target: O(max(m, n)) time, O(max(m, n)) space
 *
 * <p>Hint 1: Simulate grade-school addition digit by digit, carrying over when the sum exceeds 9.
 * <p>Hint 2: Continue iterating while either list has remaining nodes OR there is a non-zero carry.
 */
public class AddTwoNumbers {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int val) { this.val = val; }
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        throw new UnsupportedOperationException("implement me");
    }
}
