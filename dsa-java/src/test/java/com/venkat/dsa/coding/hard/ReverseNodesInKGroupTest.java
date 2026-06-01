package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class ReverseNodesInKGroupTest {

    private ReverseNodesInKGroup.ListNode buildList(int... vals) {
        ReverseNodesInKGroup.ListNode dummy = new ReverseNodesInKGroup.ListNode(0);
        ReverseNodesInKGroup.ListNode cur = dummy;
        for (int v : vals) { cur.next = new ReverseNodesInKGroup.ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    private int[] toArray(ReverseNodesInKGroup.ListNode head) {
        java.util.List<Integer> list = new java.util.ArrayList<>();
        while (head != null) { list.add(head.val); head = head.next; }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    @Test
    void testKEqualsTwo() {
        ReverseNodesInKGroup sol = new ReverseNodesInKGroup();
        assertArrayEquals(new int[]{2, 1, 4, 3, 5},
                toArray(sol.reverseKGroup(buildList(1, 2, 3, 4, 5), 2)));
    }

    @Test
    void testKEqualsThree() {
        ReverseNodesInKGroup sol = new ReverseNodesInKGroup();
        assertArrayEquals(new int[]{3, 2, 1, 4, 5},
                toArray(sol.reverseKGroup(buildList(1, 2, 3, 4, 5), 3)));
    }

    @Test
    void testKEqualsOne() {
        ReverseNodesInKGroup sol = new ReverseNodesInKGroup();
        assertArrayEquals(new int[]{1, 2, 3},
                toArray(sol.reverseKGroup(buildList(1, 2, 3), 1)));
    }
}
