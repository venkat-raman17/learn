package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Balanced Binary Tree
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Trees
 * <p>URL: https://leetcode.com/problems/balanced-binary-tree/
 *
 * <p>Given a binary tree, determine if it is height-balanced — a tree where the
 * heights of the two subtrees of every node never differ by more than one.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in the tree is in the range [0, 5000].</li>
 *   <li>-10^4 <= Node.val <= 10^4</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  root = [3,9,20,null,null,15,7]
 *   Output: true
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  root = [1,2,2,3,3,null,null,4,4]
 *   Output: false
 * </pre>
 *
 * <p>Target: Time O(n), Space O(h).
 *
 * <p>Hint 1: Use a helper that returns the height of a subtree, or -1 if it is unbalanced.
 * <p>Hint 2: Propagate the -1 sentinel upward so you short-circuit on the first imbalance.
 */
public class BalancedBinaryTree {

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

    public boolean isBalanced(TreeNode root) {
        throw new UnsupportedOperationException("implement me");
    }
}
