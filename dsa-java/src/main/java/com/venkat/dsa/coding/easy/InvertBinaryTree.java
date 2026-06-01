package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Invert Binary Tree
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Trees
 * <p>URL: https://leetcode.com/problems/invert-binary-tree/
 *
 * <p>Given the root of a binary tree, invert the tree (mirror it left-to-right)
 * and return its root.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in the tree is in the range [0, 100].</li>
 *   <li>-100 <= Node.val <= 100</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  root = [4,2,7,1,3,6,9]
 *   Output: [4,7,2,9,6,3,1]
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  root = [2,1,3]
 *   Output: [2,3,1]
 * </pre>
 *
 * <p>Target: Time O(n), Space O(h) where h = tree height.
 *
 * <p>Hint 1: Think recursively — what does it mean to invert a single node's children?
 * <p>Hint 2: After swapping left and right children, recursively invert each subtree.
 */
public class InvertBinaryTree {

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

    public TreeNode invertTree(TreeNode root) {
        throw new UnsupportedOperationException("implement me");
    }
}
