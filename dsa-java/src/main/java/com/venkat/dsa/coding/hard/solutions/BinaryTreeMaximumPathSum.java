package com.venkat.dsa.coding.hard.solutions;

/**
 * Binary Tree Maximum Path Sum (LeetCode 124)
 *
 * <p>A path in a binary tree is any sequence of nodes connected by edges; it
 * does not need to pass through the root. For each node we compute the maximum
 * "gain" obtainable by extending a path from that node downward into one of its
 * subtrees (we take the better subtree, or 0 if both are negative). The candidate
 * global answer at each node is leftGain + node.val + rightGain. We track the
 * global maximum across all nodes via an instance variable.
 *
 * <p><b>Time complexity:</b> O(n) — single DFS pass.
 * <b>Space complexity:</b> O(h) — recursion stack.
 *
 * <p><b>Key insight:</b> The contribution a subtree offers to its parent path is
 * max(0, best single-branch gain) — we never extend a path that would decrease
 * the total, hence the max with 0.
 */
public class BinaryTreeMaximumPathSum {

    public static class TreeNode {
        public int val;
        public TreeNode left, right;
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    private int globalMax;

    public int maxPathSum(TreeNode root) {
        globalMax = Integer.MIN_VALUE;
        gainFrom(root);
        return globalMax;
    }

    /**
     * Returns the maximum gain that can be obtained by extending a path that
     * starts at {@code node} and goes into exactly one subtree (or terminates here).
     */
    private int gainFrom(TreeNode node) {
        if (node == null) return 0;

        // Only include a subtree gain if it's positive; otherwise ignore it (take 0)
        int leftGain  = Math.max(0, gainFrom(node.left));
        int rightGain = Math.max(0, gainFrom(node.right));

        // A path that "turns" at this node uses both subtrees
        int pathThroughNode = node.val + leftGain + rightGain;
        globalMax = Math.max(globalMax, pathThroughNode);

        // Parent can only extend via one branch
        return node.val + Math.max(leftGain, rightGain);
    }
}
