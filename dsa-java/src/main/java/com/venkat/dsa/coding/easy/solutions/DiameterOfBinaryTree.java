package com.venkat.dsa.coding.easy.solutions;

/**
 * Diameter of Binary Tree (LeetCode 543)
 *
 * <p>The diameter through any node equals the sum of the heights of its left and
 * right subtrees. We run a single DFS that computes heights bottom-up, updating a
 * global maximum whenever we process a node. The diameter is the maximum over all
 * nodes of (leftHeight + rightHeight).
 *
 * <p><b>Time complexity:</b> O(n) — each node visited once.
 * <b>Space complexity:</b> O(h) — recursion stack depth.
 *
 * <p><b>Key insight:</b> The longest path may not pass through the root — it passes
 * through the node that maximises (leftHeight + rightHeight), so we track the
 * maximum during the height-computation DFS.
 */
public class DiameterOfBinaryTree {

    public static class TreeNode {
        public int val;
        public TreeNode left, right;
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    private int maxDiameter = 0;

    public int diameterOfBinaryTree(TreeNode root) {
        maxDiameter = 0;
        height(root);
        return maxDiameter;
    }

    /** Returns the height of the subtree; updates maxDiameter as a side-effect. */
    private int height(TreeNode node) {
        if (node == null) return 0;
        int left  = height(node.left);
        int right = height(node.right);
        // The path length through this node is left + right edges
        maxDiameter = Math.max(maxDiameter, left + right);
        return 1 + Math.max(left, right);
    }
}
