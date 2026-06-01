package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Validate Binary Search Tree
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Trees
 * <p>URL: https://leetcode.com/problems/validate-binary-search-tree/
 *
 * <p>Given the root of a binary tree, determine if it is a valid binary search tree (BST).
 * A valid BST requires every node's left subtree to contain only values strictly less than
 * the node's value, and the right subtree only values strictly greater.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in the tree is in the range [1, 10^4].</li>
 *   <li>-2^31 <= Node.val <= 2^31 - 1</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  root = [2,1,3]
 *   Output: true
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  root = [5,1,4,null,null,3,6]
 *   Output: false  (4 is in the right subtree of 5 but 4 < 5)
 * </pre>
 *
 * <p>Target: Time O(n), Space O(h).
 *
 * <p>Hint 1: Track a valid (min, max) range for each node; the root starts with (-INF, +INF).
 * <p>Hint 2: Going left tightens the upper bound; going right tightens the lower bound.
 */
public class ValidateBinarySearchTree {

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

    public boolean isValidBST(TreeNode root) {
        throw new UnsupportedOperationException("implement me");
    }
}
