package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Diameter of Binary Tree
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Trees
 * <p>URL: https://leetcode.com/problems/diameter-of-binary-tree/
 *
 * <p>Given the root of a binary tree, return the length of the diameter of the tree.
 * The diameter is the length of the longest path between any two nodes — this path
 * may or may not pass through the root. Length is measured in number of edges.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in the tree is in the range [1, 10^4].</li>
 *   <li>-100 <= Node.val <= 100</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  root = [1,2,3,4,5]
 *   Output: 3  (path: 4->2->1->3 or 5->2->1->3)
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  root = [1,2]
 *   Output: 1
 * </pre>
 *
 * <p>Target: Time O(n), Space O(h).
 *
 * <p>Hint 1: At each node, the diameter through that node = height(left) + height(right).
 * <p>Hint 2: Track a global maximum while computing heights via a single DFS pass.
 */
public class DiameterOfBinaryTree {

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

    public int diameterOfBinaryTree(TreeNode root) {
        throw new UnsupportedOperationException("implement me");
    }
}
