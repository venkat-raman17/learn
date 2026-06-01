package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Construct Binary Tree from Preorder and Inorder Traversal
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Trees
 * <p>URL: https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
 *
 * <p>Given two integer arrays preorder and inorder where preorder is the preorder traversal
 * and inorder is the inorder traversal of the same tree, construct and return the binary tree.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= preorder.length <= 3000</li>
 *   <li>inorder.length == preorder.length</li>
 *   <li>-3000 <= preorder[i], inorder[i] <= 3000</li>
 *   <li>All values in preorder and inorder are unique.</li>
 *   <li>preorder and inorder represent a valid binary tree.</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
 *   Output: [3,9,20,null,null,15,7]
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  preorder = [-1], inorder = [-1]
 *   Output: [-1]
 * </pre>
 *
 * <p>Target: Time O(n), Space O(n).
 *
 * <p>Hint 1: preorder[0] is always the root; find its index in inorder to split left/right.
 * <p>Hint 2: Use a HashMap of value->inorder index to look up splits in O(1).
 */
public class ConstructBinaryTreeFromPreorderAndInorderTraversal {

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

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        throw new UnsupportedOperationException("implement me");
    }
}
