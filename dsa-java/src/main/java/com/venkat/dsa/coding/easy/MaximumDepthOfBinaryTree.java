package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Maximum Depth of Binary Tree
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Trees
 * <p>URL: https://leetcode.com/problems/maximum-depth-of-binary-tree/
 *
 * <p>Given the root of a binary tree, return its maximum depth — the number of
 * nodes along the longest path from the root down to the farthest leaf node.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in the tree is in the range [0, 10^4].</li>
 *   <li>-100 <= Node.val <= 100</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  root = [3,9,20,null,null,15,7]
 *   Output: 3
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  root = [1,null,2]
 *   Output: 2
 * </pre>
 *
 * <p>Target: Time O(n), Space O(h).
 *
 * <p>Hint 1: The depth of a node equals 1 + the maximum depth of its two subtrees.
 * <p>Hint 2: Base case: a null node has depth 0.
 */
public class MaximumDepthOfBinaryTree {

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

    public int maxDepth(TreeNode root) {
        throw new UnsupportedOperationException("implement me");
    }
}
