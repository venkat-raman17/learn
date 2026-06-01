package com.venkat.dsa.coding.medium.solutions;

import java.util.HashMap;
import java.util.Map;

/**
 * Copy List with Random Pointer (LeetCode #138)
 *
 * Approach: Two-pass HashMap approach. In the first pass build a mapping from
 * every original node to its newly created clone (without wiring any pointers).
 * In the second pass use the map to set each clone's {@code next} and
 * {@code random} pointers by looking up the clones of the originals.
 *
 * Key insight: Separating allocation from pointer-wiring avoids the chicken-
 * and-egg problem where a random pointer may reference a node that has not
 * been created yet.
 *
 * Time complexity:  O(n) — two linear passes.
 * Space complexity: O(n) — HashMap stores one entry per node.
 */
public class CopyListWithRandomPointer {

    public static class Node {
        public int val;
        public Node next;
        public Node random;
        public Node(int val) { this.val = val; }
    }

    public Node copyRandomList(Node head) {
        if (head == null) return null;

        // Pass 1: create all clone nodes, keyed by their original
        Map<Node, Node> map = new HashMap<>();
        Node cur = head;
        while (cur != null) {
            map.put(cur, new Node(cur.val));
            cur = cur.next;
        }

        // Pass 2: wire next and random for every clone
        cur = head;
        while (cur != null) {
            Node clone = map.get(cur);
            clone.next   = map.get(cur.next);   // null-safe: map.get(null) == null
            clone.random = map.get(cur.random);
            cur = cur.next;
        }

        return map.get(head);
    }
}
