package com.venkat.dsa.coding.hard;

/**
 * NeetCode / LeetCode — Binary Tree Maximum Path Sum
 *
 * <p>Difficulty: HARD
 * <p>Pattern: Trees
 * <p>URL: https://leetcode.com/problems/binary-tree-maximum-path-sum/
 *
 * <p>A path in a binary tree is a sequence of nodes where each pair of adjacent nodes
 * has an edge between them. A node can appear in the path at most once. The path does
 * not need to pass through the root. Given the root of a binary tree, return the maximum
 * path sum of any non-empty path.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in the tree is in the range [1, 3 * 10^4].</li>
 *   <li>-1000 <= Node.val <= 1000</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  root = [1,2,3]
 *   Output: 6  (path: 2->1->3)
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  root = [-10,9,20,null,null,15,7]
 *   Output: 42  (path: 15->20->7)
 * </pre>
 *
 * <p>Target: Time O(n), Space O(h).
 *
 * <p>Hint 1: At each node, the max path through it = node.val + max(0, leftGain) + max(0, rightGain).
 * <p>Hint 2: For the return value (used by the parent), you can only extend in ONE direction —
 * return node.val + max(0, leftGain, rightGain).
 */
public class BinaryTreeMaximumPathSum {

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

    public int maxPathSum(TreeNode root) {
        throw new UnsupportedOperationException("implement me");
    }
}
