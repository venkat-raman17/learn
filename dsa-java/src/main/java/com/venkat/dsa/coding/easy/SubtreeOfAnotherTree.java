package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Subtree of Another Tree
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Trees
 * <p>URL: https://leetcode.com/problems/subtree-of-another-tree/
 *
 * <p>Given the roots of two binary trees root and subRoot, return true if there
 * is a subtree of root with the same structure and node values as subRoot,
 * and false otherwise.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in root is in the range [1, 2000].</li>
 *   <li>The number of nodes in subRoot is in the range [1, 1000].</li>
 *   <li>-10^4 <= root.val, subRoot.val <= 10^4</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  root = [3,4,5,1,2], subRoot = [4,1,2]
 *   Output: true
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  root = [3,4,5,1,2,null,null,null,null,0], subRoot = [4,1,2]
 *   Output: false
 * </pre>
 *
 * <p>Target: Time O(m*n), Space O(h) where m, n are node counts.
 *
 * <p>Hint 1: Reuse a "same tree" check — at each node in root, test if that subtree equals subRoot.
 * <p>Hint 2: Recurse into left and right children if the current node does not match.
 */
public class SubtreeOfAnotherTree {

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

    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        throw new UnsupportedOperationException("implement me");
    }
}
