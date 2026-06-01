package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class RemoveNthNodeFromEndOfListTest {

    private RemoveNthNodeFromEndOfList.ListNode buildList(int... vals) {
        RemoveNthNodeFromEndOfList.ListNode dummy = new RemoveNthNodeFromEndOfList.ListNode(0);
        RemoveNthNodeFromEndOfList.ListNode cur = dummy;
        for (int v : vals) { cur.next = new RemoveNthNodeFromEndOfList.ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    private int[] toArray(RemoveNthNodeFromEndOfList.ListNode head) {
        java.util.List<Integer> list = new java.util.ArrayList<>();
        while (head != null) { list.add(head.val); head = head.next; }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    @Test
    void testRemoveSecondFromEnd() {
        RemoveNthNodeFromEndOfList sol = new RemoveNthNodeFromEndOfList();
        assertArrayEquals(new int[]{1, 2, 3, 5},
                toArray(sol.removeNthFromEnd(buildList(1, 2, 3, 4, 5), 2)));
    }

    @Test
    void testRemoveSingleNode() {
        RemoveNthNodeFromEndOfList sol = new RemoveNthNodeFromEndOfList();
        assertNull(sol.removeNthFromEnd(buildList(1), 1));
    }

    @Test
    void testRemoveLastOfTwo() {
        RemoveNthNodeFromEndOfList sol = new RemoveNthNodeFromEndOfList();
        assertArrayEquals(new int[]{1},
                toArray(sol.removeNthFromEnd(buildList(1, 2), 1)));
    }
}
