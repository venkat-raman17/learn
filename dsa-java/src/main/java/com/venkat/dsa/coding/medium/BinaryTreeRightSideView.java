package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — Binary Tree Right Side View
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Trees
 * <p>URL: https://leetcode.com/problems/binary-tree-right-side-view/
 *
 * <p>Given the root of a binary tree, imagine standing on the right side of it.
 * Return the values of the nodes you can see ordered from top to bottom.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in the tree is in the range [0, 100].</li>
 *   <li>-100 <= Node.val <= 100</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  root = [1,2,3,null,5,null,4]
 *   Output: [1,3,4]
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  root = [1,null,3]
 *   Output: [1,3]
 * </pre>
 *
 * <p>Target: Time O(n), Space O(n).
 *
 * <p>Hint 1: BFS level-by-level; the last node dequeued at each level is visible from the right.
 * <p>Hint 2: Alternatively, DFS with right-child-first order and tracking depth — the first
 * visit at each new depth is the rightmost node.
 */
public class BinaryTreeRightSideView {

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

    public List<Integer> rightSideView(TreeNode root) {
        throw new UnsupportedOperationException("implement me");
    }
}
