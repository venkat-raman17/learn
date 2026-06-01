package com.venkat.dsa.coding.hard.solutions;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Serialize and Deserialize Binary Tree (LeetCode 297)
 *
 * <p>Uses BFS (level-order) serialization. The tree is encoded as a
 * comma-separated string of node values with "N" for null children, e.g.
 * "1,2,3,N,N,4,5". Deserialization replays the BFS: for each non-null token
 * dequeued we assign its next two tokens as left and right children.
 *
 * <p><b>Time complexity:</b> O(n) for both serialize and deserialize.
 * <b>Space complexity:</b> O(n) for the queue and the string representation.
 *
 * <p><b>Key insight:</b> BFS order is self-describing: position in the sequence
 * uniquely determines parent-child relationships when null placeholders are
 * included, so reconstruction needs no index arithmetic.
 */
public class SerializeAndDeserializeBinaryTree {

    public static class TreeNode {
        public int val;
        public TreeNode left, right;
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) { this.val = val; this.left = left; this.right = right; }
    }

    private static final String NULL_MARKER = "N";
    private static final String DELIMITER   = ",";

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        if (root == null) return NULL_MARKER;

        StringBuilder sb = new StringBuilder();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node == null) {
                sb.append(NULL_MARKER);
            } else {
                sb.append(node.val);
                queue.offer(node.left);
                queue.offer(node.right);
            }
            sb.append(DELIMITER);
        }
        // Remove trailing delimiter
        return sb.substring(0, sb.length() - 1);
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        String[] tokens = data.split(DELIMITER);
        if (tokens[0].equals(NULL_MARKER)) return null;

        TreeNode root = new TreeNode(Integer.parseInt(tokens[0]));
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int i = 1;
        while (!queue.isEmpty() && i < tokens.length) {
            TreeNode node = queue.poll();

            // Assign left child
            if (!tokens[i].equals(NULL_MARKER)) {
                node.left = new TreeNode(Integer.parseInt(tokens[i]));
                queue.offer(node.left);
            }
            i++;

            // Assign right child
            if (i < tokens.length && !tokens[i].equals(NULL_MARKER)) {
                node.right = new TreeNode(Integer.parseInt(tokens[i]));
                queue.offer(node.right);
            }
            i++;
        }
        return root;
    }
}
