package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Lowest Common Ancestor of a Binary Search Tree
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Trees
 * <p>URL: https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/
 *
 * <p>Given a BST and two nodes p and q, return their lowest common ancestor (LCA) —
 * the deepest node that has both p and q as descendants (a node is a descendant of itself).
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes is in the range [2, 10^5].</li>
 *   <li>-10^9 <= Node.val <= 10^9</li>
 *   <li>All Node.val are unique.</li>
 *   <li>p != q; p and q exist in the BST.</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 8
 *   Output: 6
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 4
 *   Output: 2
 * </pre>
 *
 * <p>Target: Time O(h), Space O(1) iterative / O(h) recursive.
 *
 * <p>Hint 1: Use BST ordering: if both p and q are less than root, the LCA is in the left subtree.
 * <p>Hint 2: If they are on opposite sides (or one equals root), the current root is the LCA.
 */
public class LowestCommonAncestorOfABinarySearchTree {

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

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        throw new UnsupportedOperationException("implement me");
    }
}
