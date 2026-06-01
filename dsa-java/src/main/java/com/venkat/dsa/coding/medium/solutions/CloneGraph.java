package com.venkat.dsa.coding.medium.solutions;

import java.util.*;

/**
 * Clone Graph (LeetCode 133)
 *
 * <p>Uses BFS from the given node. A {@code HashMap<Node, Node>} maps every original node to its
 * deep clone; before enqueuing a neighbor, we check the map to avoid revisiting. After cloning a
 * node, we wire all neighbor pointers to their clones.
 *
 * <p><b>Key insight:</b> The hash map serves as both a visited set and the node registry, so each
 * node is cloned exactly once and all back-edges in the original graph are correctly reproduced.
 *
 * <p><b>Time:</b> O(V + E) — each node and edge visited once.<br>
 * <b>Space:</b> O(V) for the hash map and BFS queue.
 */
public class CloneGraph {

    /** Minimal Node definition matching LeetCode's provided class. */
    public static class Node {
        public int val;
        public List<Node> neighbors;

        public Node() {
            val = 0;
            neighbors = new ArrayList<>();
        }

        public Node(int _val) {
            val = _val;
            neighbors = new ArrayList<>();
        }

        public Node(int _val, List<Node> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
    }

    public Node cloneGraph(Node node) {
        if (node == null) return null;

        Map<Node, Node> cloned = new HashMap<>();
        Queue<Node> queue = new ArrayDeque<>();

        // Seed: clone the starting node and enqueue it
        cloned.put(node, new Node(node.val));
        queue.offer(node);

        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            for (Node neighbor : curr.neighbors) {
                if (!cloned.containsKey(neighbor)) {
                    // Clone neighbor on first encounter and schedule its processing
                    cloned.put(neighbor, new Node(neighbor.val));
                    queue.offer(neighbor);
                }
                // Wire the clone's neighbor list
                cloned.get(curr).neighbors.add(cloned.get(neighbor));
            }
        }
        return cloned.get(node);
    }
}
