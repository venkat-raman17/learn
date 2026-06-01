package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.hard.solutions.ReverseNodesInKGroup.ListNode;

class ReverseNodesInKGroupTest {

    private final ReverseNodesInKGroup sol = new ReverseNodesInKGroup();

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
    void example1_k2() {
        // [1,2,3,4,5], k=2 => [2,1,4,3,5]
        assertArrayEquals(new int[]{2, 1, 4, 3, 5},
            toArray(sol.reverseKGroup(build(1, 2, 3, 4, 5), 2)));
    }

    @Test
    void example2_k3() {
        // [1,2,3,4,5], k=3 => [3,2,1,4,5]
        assertArrayEquals(new int[]{3, 2, 1, 4, 5},
            toArray(sol.reverseKGroup(build(1, 2, 3, 4, 5), 3)));
    }

    @Test
    void k1_noChange() {
        // k=1: each group is a single node, no reversal
        assertArrayEquals(new int[]{1, 2, 3, 4},
            toArray(sol.reverseKGroup(build(1, 2, 3, 4), 1)));
    }

    @Test
    void kEqualsLength_fullReverse() {
        // k == length => entire list reversed
        assertArrayEquals(new int[]{4, 3, 2, 1},
            toArray(sol.reverseKGroup(build(1, 2, 3, 4), 4)));
    }

    @Test
    void kLargerThanLength_noChange() {
        // k > length => nothing is reversed
        assertArrayEquals(new int[]{1, 2, 3},
            toArray(sol.reverseKGroup(build(1, 2, 3), 5)));
    }

    @Test
    void singleNode() {
        assertArrayEquals(new int[]{1},
            toArray(sol.reverseKGroup(build(1), 1)));
    }

    @Test
    void exactMultipleGroups() {
        // [1,2,3,4], k=2 => [2,1,4,3]
        assertArrayEquals(new int[]{2, 1, 4, 3},
            toArray(sol.reverseKGroup(build(1, 2, 3, 4), 2)));
    }

    @Test
    void remainderLeftAsIs() {
        // [1,2,3,4,5], k=4 => [4,3,2,1,5]
        assertArrayEquals(new int[]{4, 3, 2, 1, 5},
            toArray(sol.reverseKGroup(build(1, 2, 3, 4, 5), 4)));
    }
}
