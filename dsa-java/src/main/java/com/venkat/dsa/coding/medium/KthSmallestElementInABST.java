package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Kth Smallest Element in a BST
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Trees
 * <p>URL: https://leetcode.com/problems/kth-smallest-element-in-a-bst/
 *
 * <p>Given the root of a BST and an integer k, return the k-th smallest value
 * (1-indexed) of all the values of the nodes in the tree.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in the tree is n, where 1 <= k <= n <= 10^4.</li>
 *   <li>0 <= Node.val <= 10^4</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  root = [3,1,4,null,2], k = 1
 *   Output: 1
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  root = [5,3,6,2,4,null,null,1], k = 3
 *   Output: 3
 * </pre>
 *
 * <p>Target: Time O(h + k), Space O(h).
 *
 * <p>Hint 1: In-order traversal of a BST yields values in sorted ascending order.
 * <p>Hint 2: Use an iterative in-order traversal with a stack; stop as soon as you
 * have visited k nodes.
 */
public class KthSmallestElementInABST {

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

    public int kthSmallest(TreeNode root, int k) {
        throw new UnsupportedOperationException("implement me");
    }
}
