package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — Binary Tree Level Order Traversal
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Trees
 * <p>URL: https://leetcode.com/problems/binary-tree-level-order-traversal/
 *
 * <p>Given the root of a binary tree, return the level order traversal of its nodes'
 * values as a list of lists, where each inner list contains one level's values
 * from left to right.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in the tree is in the range [0, 2000].</li>
 *   <li>-1000 <= Node.val <= 1000</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  root = [3,9,20,null,null,15,7]
 *   Output: [[3],[9,20],[15,7]]
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  root = [1]
 *   Output: [[1]]
 * </pre>
 *
 * <p>Target: Time O(n), Space O(n).
 *
 * <p>Hint 1: Use a queue (BFS). After enqueuing the root, process nodes level-by-level
 * by capturing the queue size at the start of each level iteration.
 * <p>Hint 2: For each level, dequeue exactly `size` nodes, collect their values, and
 * enqueue their non-null children for the next level.
 */
public class BinaryTreeLevelOrderTraversal {

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

    public List<List<Integer>> levelOrder(TreeNode root) {
        throw new UnsupportedOperationException("implement me");
    }
}
