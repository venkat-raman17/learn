package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class ReorderListTest {

    private ReorderList.ListNode buildList(int... vals) {
        ReorderList.ListNode dummy = new ReorderList.ListNode(0);
        ReorderList.ListNode cur = dummy;
        for (int v : vals) { cur.next = new ReorderList.ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    private int[] toArray(ReorderList.ListNode head) {
        java.util.List<Integer> list = new java.util.ArrayList<>();
        while (head != null) { list.add(head.val); head = head.next; }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    @Test
    void testFourElements() {
        ReorderList sol = new ReorderList();
        ReorderList.ListNode head = buildList(1, 2, 3, 4);
        sol.reorderList(head);
        assertArrayEquals(new int[]{1, 4, 2, 3}, toArray(head));
    }

    @Test
    void testFiveElements() {
        ReorderList sol = new ReorderList();
        ReorderList.ListNode head = buildList(1, 2, 3, 4, 5);
        sol.reorderList(head);
        assertArrayEquals(new int[]{1, 5, 2, 4, 3}, toArray(head));
    }

    @Test
    void testSingleElement() {
        ReorderList sol = new ReorderList();
        ReorderList.ListNode head = buildList(1);
        sol.reorderList(head);
        assertArrayEquals(new int[]{1}, toArray(head));
    }
}
