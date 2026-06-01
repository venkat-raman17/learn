package com.venkat.dsa.coding.hard;

/**
 * NeetCode / LeetCode — Serialize and Deserialize Binary Tree
 *
 * <p>Difficulty: HARD
 * <p>Pattern: Trees
 * <p>URL: https://leetcode.com/problems/serialize-and-deserialize-binary-tree/
 *
 * <p>Design an algorithm to serialize a binary tree to a string and deserialize
 * that string back to the original tree structure. There is no restriction on how
 * your serialization/deserialization algorithm works as long as a TreeNode can be
 * reconstructed from the serialized string.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in the tree is in the range [0, 10^4].</li>
 *   <li>-1000 <= Node.val <= 1000</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  root = [1,2,3,null,null,4,5]
 *   Output: [1,2,3,null,null,4,5]  (the deserialized tree must equal the original)
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  root = []
 *   Output: []
 * </pre>
 *
 * <p>Target: Time O(n) for both encode and decode, Space O(n).
 *
 * <p>Hint 1: Use preorder DFS: encode each node's value and use a sentinel (e.g. "null")
 * for missing children; separate tokens with a delimiter like ",".
 * <p>Hint 2: During decode, consume tokens from a queue/iterator in the same preorder
 * sequence; a sentinel token means return null.
 */
public class SerializeAndDeserializeBinaryTree {

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }

        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        throw new UnsupportedOperationException("implement me");
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        throw new UnsupportedOperationException("implement me");
    }
}
