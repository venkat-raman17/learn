package com.venkat.dsa.coding.easy;

/**
 * NeetCode / LeetCode — Same Tree
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Trees
 * <p>URL: https://leetcode.com/problems/same-tree/
 *
 * <p>Given the roots of two binary trees p and q, write a function to check if
 * they are the same tree — structurally identical with the same node values.
 *
 * <p>Constraints:
 * <ul>
 *   <li>The number of nodes in both trees is in the range [0, 100].</li>
 *   <li>-10^4 <= Node.val <= 10^4</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  p = [1,2,3], q = [1,2,3]
 *   Output: true
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  p = [1,2], q = [1,null,2]
 *   Output: false
 * </pre>
 *
 * <p>Target: Time O(n), Space O(h).
 *
 * <p>Hint 1: Two trees are the same if their roots match and both subtrees are the same.
 * <p>Hint 2: Handle null combinations carefully as your base cases.
 */
public class SameTree {

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

    public boolean isSameTree(TreeNode p, TreeNode q) {
        throw new UnsupportedOperationException("implement me");
    }
}
