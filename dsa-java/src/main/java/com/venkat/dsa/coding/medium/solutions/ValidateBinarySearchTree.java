package com.venkat.dsa.coding.medium.solutions;

/**
 * Validate Binary Search Tree (LeetCode 98)
 *
 * <p>A valid BST requires every node in the left subtree to be strictly less than
 * the node, and every node in the right subtree to be strictly greater — not just
 * the direct children. We enforce this by passing a (min, max) valid range down
 * the recursion: the left child tightens the upper bound, the right child tightens
 * the lower bound.
 *
 * <p><b>Time complexity:</b> O(n) — each node visited once.
 * <b>Space complexity:</b> O(h) — recursion stack depth.
 *
 * <p><b>Key insight:</b> Each node must satisfy min &lt; node.val &lt; max. Using
 * Long.MIN_VALUE / Long.MAX_VALUE as the initial bounds handles Integer.MIN_VALUE
 * and Integer.MAX_VALUE edge cases cleanly.
 */
public class ValidateBinarySearchTree {

    public static class TreeNode {
        public int val;
        public TreeNode left, right;
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    public boolean isValidBST(TreeNode root) {
        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean validate(TreeNode node, long min, long max) {
        if (node == null) return true;
        if (node.val <= min || node.val >= max) return false;   // violates bounds
        // Left child must be < node.val; right child must be > node.val
        return validate(node.left,  min, node.val)
            && validate(node.right, node.val, max);
    }
}
