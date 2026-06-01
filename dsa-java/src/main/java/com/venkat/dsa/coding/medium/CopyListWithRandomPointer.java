package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Copy List With Random Pointer
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Linked List
 * <p>URL: https://leetcode.com/problems/copy-list-with-random-pointer/
 *
 * <p>A linked list where each node has an additional random pointer that can point to any node
 * in the list or null. Construct a deep copy of the list and return the head of the copied list.
 *
 * <p>Constraints:
 * <ul>
 *   <li>0 <= n <= 1000</li>
 *   <li>-10^4 <= Node.val <= 10^4</li>
 *   <li>Node.random is null or points to some node in the linked list.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   Input: head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
 *   Output: [[7,null],[13,0],[11,4],[10,2],[1,0]]   (deep copy, different objects)
 *
 *   Input: head = [[1,1],[2,1]]
 *   Output: [[1,1],[2,1]]
 * </pre>
 *
 * <p>Target: O(n) time, O(n) space (hash map) or O(1) extra space (interleaving)
 *
 * <p>Hint 1: Use a HashMap from original node to its copy; iterate twice — first to build copies,
 *            second to wire next and random pointers.
 * <p>Hint 2: Alternatively, interleave copies next to originals, set random pointers, then detach.
 */
public class CopyListWithRandomPointer {

    public static class Node {
        public int val;
        public Node next;
        public Node random;
        public Node(int val) { this.val = val; }
    }

    public Node copyRandomList(Node head) {
        throw new UnsupportedOperationException("implement me");
    }
}
