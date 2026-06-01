package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.medium.solutions.RemoveNthNodeFromEndOfList.ListNode;

class RemoveNthNodeFromEndOfListTest {

    private final RemoveNthNodeFromEndOfList sol = new RemoveNthNodeFromEndOfList();

    private ListNode build(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int v : vals) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    private int[] toArray(ListNode head) {
        java.util.List<Integer> list = new java.util.ArrayList<>();
        while (head != null) { list.add(head.val); head = head.next; }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    @Test
    void example1_removeSecondFromEnd() {
        // [1,2,3,4,5], n=2 => [1,2,3,5]
        ListNode result = sol.removeNthFromEnd(build(1, 2, 3, 4, 5), 2);
        assertArrayEquals(new int[]{1, 2, 3, 5}, toArray(result));
    }

    @Test
    void example2_singleNodeRemoveFirst() {
        // [1], n=1 => []
        ListNode result = sol.removeNthFromEnd(build(1), 1);
        assertNull(result);
    }

    @Test
    void example3_twoNodesRemoveFirst() {
        // [1,2], n=2 => [2]
        ListNode result = sol.removeNthFromEnd(build(1, 2), 2);
        assertArrayEquals(new int[]{2}, toArray(result));
    }

    @Test
    void removeLastNode() {
        // [1,2,3], n=1 => [1,2]
        ListNode result = sol.removeNthFromEnd(build(1, 2, 3), 1);
        assertArrayEquals(new int[]{1, 2}, toArray(result));
    }

    @Test
    void removeHeadOfLongerList() {
        // [1,2,3,4,5], n=5 => [2,3,4,5]
        ListNode result = sol.removeNthFromEnd(build(1, 2, 3, 4, 5), 5);
        assertArrayEquals(new int[]{2, 3, 4, 5}, toArray(result));
    }

    @Test
    void removeMiddleNode() {
        // [1,2,3,4,5], n=3 => [1,2,4,5]
        ListNode result = sol.removeNthFromEnd(build(1, 2, 3, 4, 5), 3);
        assertArrayEquals(new int[]{1, 2, 4, 5}, toArray(result));
    }
}
