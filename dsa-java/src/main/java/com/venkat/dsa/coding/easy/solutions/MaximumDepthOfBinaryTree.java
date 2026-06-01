package com.venkat.dsa.coding.easy.solutions;

/**
 * Maximum Depth of Binary Tree (LeetCode 104)
 *
 * <p>Uses recursive DFS: the depth of a tree rooted at {@code root} equals
 * 1 + max(depth(left), depth(right)). The base case — a null node — has depth 0.
 * Each call returns the height of its subtree, and the root call aggregates the
 * global answer automatically.
 *
 * <p><b>Time complexity:</b> O(n) — every node is visited once.
 * <b>Space complexity:</b> O(h) — recursion stack proportional to tree height.
 *
 * <p><b>Key insight:</b> Depth is just the length of the longest root-to-leaf path,
 * which is computed bottom-up in a single DFS pass.
 */
public class MaximumDepthOfBinaryTree {

    public static class TreeNode {
        public int val;
        public TreeNode left, right;
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    public int maxDepth(TreeNode root) {
        if (root == null) return 0;                          // base case: empty tree has depth 0
        int leftDepth  = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);
        return 1 + Math.max(leftDepth, rightDepth);          // current node adds 1 level
    }
}
