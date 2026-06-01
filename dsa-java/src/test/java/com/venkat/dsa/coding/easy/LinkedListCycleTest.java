package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class LinkedListCycleTest {

    /** Builds a list and connects tail to the node at cyclePos (-1 means no cycle). */
    private LinkedListCycle.ListNode buildList(int[] vals, int cyclePos) {
        if (vals.length == 0) return null;
        LinkedListCycle.ListNode[] nodes = new LinkedListCycle.ListNode[vals.length];
        for (int i = 0; i < vals.length; i++) nodes[i] = new LinkedListCycle.ListNode(vals[i]);
        for (int i = 0; i < vals.length - 1; i++) nodes[i].next = nodes[i + 1];
        if (cyclePos >= 0) nodes[vals.length - 1].next = nodes[cyclePos];
        return nodes[0];
    }

    @Test
    void testCycleAtPos1() {
        LinkedListCycle sol = new LinkedListCycle();
        assertTrue(sol.hasCycle(buildList(new int[]{3, 2, 0, -4}, 1)));
    }

    @Test
    void testCycleAtPos0() {
        LinkedListCycle sol = new LinkedListCycle();
        assertTrue(sol.hasCycle(buildList(new int[]{1, 2}, 0)));
    }

    @Test
    void testNoCycle() {
        LinkedListCycle sol = new LinkedListCycle();
        assertFalse(sol.hasCycle(buildList(new int[]{1}, -1)));
    }
}
