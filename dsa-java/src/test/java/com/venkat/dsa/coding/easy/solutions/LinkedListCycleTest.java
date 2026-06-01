package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.easy.solutions.LinkedListCycle.ListNode;

class LinkedListCycleTest {

    private final LinkedListCycle sol = new LinkedListCycle();

    /** Build a list from vals, then connect tail to the node at cyclePos (0-indexed). */
    private ListNode buildWithCycle(int[] vals, int cyclePos) {
        if (vals.length == 0) return null;
        ListNode[] nodes = new ListNode[vals.length];
        for (int i = 0; i < vals.length; i++) nodes[i] = new ListNode(vals[i]);
        for (int i = 0; i < vals.length - 1; i++) nodes[i].next = nodes[i + 1];
        if (cyclePos >= 0) nodes[vals.length - 1].next = nodes[cyclePos];
        return nodes[0];
    }

    @Test
    void example1_cycleAtPos1() {
        // 3->2->0->-4  tail connects back to index 1 (val=2)
        ListNode head = buildWithCycle(new int[]{3, 2, 0, -4}, 1);
        assertTrue(sol.hasCycle(head));
    }

    @Test
    void example2_cycleAtPos0() {
        // 1->2  tail connects back to index 0 (val=1)
        ListNode head = buildWithCycle(new int[]{1, 2}, 0);
        assertTrue(sol.hasCycle(head));
    }

    @Test
    void example3_noCycle_singleNode() {
        ListNode head = buildWithCycle(new int[]{1}, -1);
        assertFalse(sol.hasCycle(head));
    }

    @Test
    void noCycle_multipleNodes() {
        ListNode head = buildWithCycle(new int[]{1, 2, 3, 4, 5}, -1);
        assertFalse(sol.hasCycle(head));
    }

    @Test
    void nullHead() {
        assertFalse(sol.hasCycle(null));
    }

    @Test
    void selfLoop() {
        // Single node pointing to itself
        ListNode node = new ListNode(1);
        node.next = node;
        assertTrue(sol.hasCycle(node));
    }
}
