package com.venkat.dsa.coding.medium.solutions;

/**
 * Add Two Numbers (LeetCode #2)
 *
 * Approach: Simulate grade-school addition digit by digit. Traverse both lists
 * simultaneously (least-significant digit first, which matches the list order)
 * maintaining a carry. At each step sum the two digits plus the carry, produce
 * a new result node with the unit digit, and propagate the carry. Continue
 * until both lists are exhausted and the carry is zero.
 *
 * Key insight: Using a dummy sentinel head avoids a null-check for the very
 * first node and lets the loop stay uniform. Handling the final carry outside
 * the loop ensures the extra node is appended when the sum overflows.
 *
 * Time complexity:  O(max(m, n)) — processes as many steps as the longer list.
 * Space complexity: O(max(m, n)) — output list length is at most max(m,n)+1.
 */
public class AddTwoNumbers {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int val) { this.val = val; }
        public ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        int carry = 0;

        while (l1 != null || l2 != null || carry != 0) {
            int sum = carry;
            if (l1 != null) { sum += l1.val; l1 = l1.next; }
            if (l2 != null) { sum += l2.val; l2 = l2.next; }
            carry = sum / 10;            // carry for next iteration
            cur.next = new ListNode(sum % 10); // unit digit becomes new node
            cur = cur.next;
        }

        return dummy.next;
    }
}
