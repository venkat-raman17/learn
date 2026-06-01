package com.venkat.dsa.coding.medium.solutions;

import java.util.HashMap;
import java.util.Map;

/**
 * LRU Cache (LeetCode #146)
 *
 * Approach: Combine a HashMap (O(1) key lookup) with a doubly-linked list (O(1)
 * insertion and removal of any node). The doubly-linked list is kept in
 * most-recently-used to least-recently-used order using sentinel head and tail
 * nodes, so the LRU item is always {@code tail.prev}. On every get/put the
 * accessed node is moved to the front (right after {@code head}). When capacity
 * is exceeded the node at {@code tail.prev} is evicted.
 *
 * Key insight: The HashMap stores a direct pointer to each node; combined with
 * doubly-linked list relinking this achieves O(1) for all operations without
 * any iteration.
 *
 * Time complexity:  O(1) amortized for both {@code get} and {@code put}.
 * Space complexity: O(capacity) — at most {@code capacity} nodes in both structures.
 */
public class LRUCache {

    private static class DLinkedNode {
        int key, val;
        DLinkedNode prev, next;
        DLinkedNode(int key, int val) { this.key = key; this.val = val; }
    }

    private final int capacity;
    private final Map<Integer, DLinkedNode> map;
    private final DLinkedNode head; // most-recently-used sentinel
    private final DLinkedNode tail; // least-recently-used sentinel

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        head = new DLinkedNode(0, 0);
        tail = new DLinkedNode(0, 0);
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        DLinkedNode node = map.get(key);
        if (node == null) return -1;
        moveToFront(node); // mark as most recently used
        return node.val;
    }

    public void put(int key, int value) {
        DLinkedNode node = map.get(key);
        if (node != null) {
            node.val = value;
            moveToFront(node);
        } else {
            node = new DLinkedNode(key, value);
            map.put(key, node);
            addToFront(node);
            if (map.size() > capacity) {
                DLinkedNode lru = tail.prev; // LRU node is just before tail
                remove(lru);
                map.remove(lru.key);
            }
        }
    }

    /** Remove a node from its current position in the list. */
    private void remove(DLinkedNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    /** Insert a node immediately after the head sentinel. */
    private void addToFront(DLinkedNode node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void moveToFront(DLinkedNode node) {
        remove(node);
        addToFront(node);
    }
}
