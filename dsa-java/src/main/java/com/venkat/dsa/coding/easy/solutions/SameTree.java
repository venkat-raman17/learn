package com.venkat.dsa.coding.easy.solutions;

/**
 * Same Tree (LeetCode 100)
 *
 * <p>Two trees are identical if their root values match and both subtrees are
 * also identical. We check this recursively: if both nodes are null they match;
 * if exactly one is null they don't; otherwise compare values and recurse.
 *
 * <p><b>Time complexity:</b> O(n) — visit every node at most once (stops early
 * on first mismatch).
 * <b>Space complexity:</b> O(h) — recursion stack proportional to tree height.
 *
 * <p><b>Key insight:</b> Structural equality and value equality must both hold
 * simultaneously; checking null status first avoids NPE and handles shape mismatches.
 */
public class SameTree {

    public static class TreeNode {
        public int val;
        public TreeNode left, right;
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    public boolean isSameTree(TreeNode p, TreeNode q) {
        // Both null → identical at this position
        if (p == null && q == null) return true;
        // One null, one not → structural mismatch
        if (p == null || q == null) return false;
        // Values differ → mismatch
        if (p.val != q.val) return false;
        // Both subtrees must also match
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }
}
