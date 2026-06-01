package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Count Good Nodes in Binary Tree
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Trees
 * <p>URL: https://leetcode.com/problems/count-good-nodes-in-binary-tree/
 *
 * <p>Given a binary tree root, a node X is "good" if in the path from root to X
 * there are no nodes with a value greater than X. Return the count of good nodes.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in the binary tree is in the range [1, 10^5].</li>
 *   <li>Each node's value is between [-10^4, 10^4].</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  root = [3,1,4,3,null,1,5]
 *   Output: 4  (nodes 3, 4, 3, 5 are good)
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  root = [3,3,null,4,2]
 *   Output: 3
 * </pre>
 *
 * <p>Target: Time O(n), Space O(h).
 *
 * <p>Hint 1: DFS while tracking the maximum value seen so far on the path from root.
 * <p>Hint 2: A node is good if node.val >= maxSoFar; update maxSoFar before recursing.
 */
public class CountGoodNodesInBinaryTree {

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

    public int goodNodes(TreeNode root) {
        throw new UnsupportedOperationException("implement me");
    }
}
