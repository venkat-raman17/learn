package com.venkat.dsa.coding.medium.solutions;

/**
 * Count Good Nodes in Binary Tree (LeetCode 1448)
 *
 * <p>A node is "good" if no node on the path from the root to it (inclusive)
 * has a value strictly greater than the node's own value — i.e., the node's
 * value is >= the maximum seen so far. We perform a DFS, propagating the
 * running maximum from root to leaves and incrementing the count whenever the
 * current node's value meets the condition.
 *
 * <p><b>Time complexity:</b> O(n) — every node visited once.
 * <b>Space complexity:</b> O(h) — recursion stack depth.
 *
 * <p><b>Key insight:</b> Keep a running max along the current root-to-node path;
 * a node is good iff its value >= that max.
 */
public class CountGoodNodesInBinaryTree {

    public static class TreeNode {
        public int val;
        public TreeNode left, right;
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    public int goodNodes(TreeNode root) {
        return dfs(root, Integer.MIN_VALUE);
    }

    private int dfs(TreeNode node, int maxSoFar) {
        if (node == null) return 0;

        // Current node is good if its value >= max value on path from root
        int good = (node.val >= maxSoFar) ? 1 : 0;

        int newMax = Math.max(maxSoFar, node.val);
        good += dfs(node.left,  newMax);
        good += dfs(node.right, newMax);
        return good;
    }
}
