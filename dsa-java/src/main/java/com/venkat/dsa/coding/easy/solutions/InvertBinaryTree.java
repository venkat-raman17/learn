package com.venkat.dsa.coding.easy.solutions;

/**
 * Invert Binary Tree (LeetCode 226)
 *
 * <p>Recursively swap the left and right children of every node in the tree.
 * A single DFS post-order pass inverts both subtrees before swapping at the
 * current node, guaranteeing that the entire subtree is already inverted when
 * we perform the swap. This is equivalent to a mirror-image reflection of the
 * tree about its vertical axis.
 *
 * <p><b>Time complexity:</b> O(n) — every node is visited exactly once.
 * <b>Space complexity:</b> O(h) — recursion stack depth equals tree height h
 * (O(log n) balanced, O(n) worst-case skewed).
 *
 * <p><b>Key insight:</b> The problem reduces to: swap children, then recurse — or
 * equivalently recurse first and then swap (either order works).
 */
public class InvertBinaryTree {

    public static class TreeNode {
        public int val;
        public TreeNode left, right;
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;

        // Recurse into both subtrees first (post-order)
        TreeNode left  = invertTree(root.left);
        TreeNode right = invertTree(root.right);

        // Swap the children
        root.left  = right;
        root.right = left;

        return root;
    }
}
