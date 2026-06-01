package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.medium.solutions.ReorderList.ListNode;

class ReorderListTest {

    private final ReorderList sol = new ReorderList();

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
    void example1_fourElements() {
        // [1,2,3,4] => [1,4,2,3]
        ListNode head = build(1, 2, 3, 4);
        sol.reorderList(head);
        assertArrayEquals(new int[]{1, 4, 2, 3}, toArray(head));
    }

    @Test
    void example2_fiveElements() {
        // [1,2,3,4,5] => [1,5,2,4,3]
        ListNode head = build(1, 2, 3, 4, 5);
        sol.reorderList(head);
        assertArrayEquals(new int[]{1, 5, 2, 4, 3}, toArray(head));
    }

    @Test
    void singleElement() {
        ListNode head = build(1);
        sol.reorderList(head);
        assertArrayEquals(new int[]{1}, toArray(head));
    }

    @Test
    void twoElements() {
        // [1,2] => [1,2] (no change needed)
        ListNode head = build(1, 2);
        sol.reorderList(head);
        assertArrayEquals(new int[]{1, 2}, toArray(head));
    }

    @Test
    void threeElements() {
        // [1,2,3] => [1,3,2]
        ListNode head = build(1, 2, 3);
        sol.reorderList(head);
        assertArrayEquals(new int[]{1, 3, 2}, toArray(head));
    }

    @Test
    void sixElements() {
        // [1,2,3,4,5,6] => [1,6,2,5,3,4]
        ListNode head = build(1, 2, 3, 4, 5, 6);
        sol.reorderList(head);
        assertArrayEquals(new int[]{1, 6, 2, 5, 3, 4}, toArray(head));
    }
}
