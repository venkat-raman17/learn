package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class ReverseLinkedListTest {

    private ReverseLinkedList.ListNode buildList(int... vals) {
        ReverseLinkedList.ListNode dummy = new ReverseLinkedList.ListNode(0);
        ReverseLinkedList.ListNode cur = dummy;
        for (int v : vals) { cur.next = new ReverseLinkedList.ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    private int[] toArray(ReverseLinkedList.ListNode head) {
        java.util.List<Integer> list = new java.util.ArrayList<>();
        while (head != null) { list.add(head.val); head = head.next; }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    @Test
    void testFiveElements() {
        ReverseLinkedList sol = new ReverseLinkedList();
        ReverseLinkedList.ListNode head = buildList(1, 2, 3, 4, 5);
        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, toArray(sol.reverseList(head)));
    }

    @Test
    void testTwoElements() {
        ReverseLinkedList sol = new ReverseLinkedList();
        ReverseLinkedList.ListNode head = buildList(1, 2);
        assertArrayEquals(new int[]{2, 1}, toArray(sol.reverseList(head)));
    }

    @Test
    void testEmptyList() {
        ReverseLinkedList sol = new ReverseLinkedList();
        assertNull(sol.reverseList(null));
    }
}
