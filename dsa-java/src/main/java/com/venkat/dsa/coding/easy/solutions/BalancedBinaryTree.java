package com.venkat.dsa.coding.easy.solutions;

/**
 * Balanced Binary Tree (LeetCode 110)
 *
 * <p>A tree is height-balanced if, for every node, the heights of its left and
 * right subtrees differ by at most 1. We compute heights bottom-up with DFS,
 * using -1 as a sentinel to propagate an "already unbalanced" signal upward.
 * This avoids recomputing heights multiple times (naive approach is O(n log n)).
 *
 * <p><b>Time complexity:</b> O(n) — each node visited once.
 * <b>Space complexity:</b> O(h) — recursion stack depth.
 *
 * <p><b>Key insight:</b> Return -1 to short-circuit: once any subtree is unbalanced
 * we immediately propagate the failure without further work.
 */
public class BalancedBinaryTree {

    public static class TreeNode {
        public int val;
        public TreeNode left, right;
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    public boolean isBalanced(TreeNode root) {
        return checkHeight(root) != -1;
    }

    /**
     * Returns the height of the subtree, or -1 if it is unbalanced.
     */
    private int checkHeight(TreeNode node) {
        if (node == null) return 0;

        int left = checkHeight(node.left);
        if (left == -1) return -1;               // propagate unbalanced signal

        int right = checkHeight(node.right);
        if (right == -1) return -1;              // propagate unbalanced signal

        // Check balance condition at this node
        if (Math.abs(left - right) > 1) return -1;

        return 1 + Math.max(left, right);
    }
}
