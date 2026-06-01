package com.venkat.dsa.coding.easy.solutions;

/**
 * Subtree of Another Tree (LeetCode 572)
 *
 * <p>We reuse the Same Tree equality check. For each node in {@code root} we
 * ask: "is the tree rooted here identical to {@code subRoot}?" We recurse
 * through {@code root} and check at every node, stopping as soon as a match
 * is found. The isSameTree helper is the same O(n) check from LeetCode 100.
 *
 * <p><b>Time complexity:</b> O(m * n) where m = nodes in root, n = nodes in
 * subRoot — in the worst case we call isSameTree at every node of root.
 * <b>Space complexity:</b> O(h_root) for the outer recursion stack.
 *
 * <p><b>Key insight:</b> A subtree is a connected sub-structure starting from
 * some node and including ALL its descendants — so equality (not just partial
 * match) is the right comparison primitive.
 */
public class SubtreeOfAnotherTree {

    public static class TreeNode {
        public int val;
        public TreeNode left, right;
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        if (root == null) return false;                    // exhausted root, not found
        if (isSameTree(root, subRoot)) return true;        // match found at this node
        // Try every other node in root
        return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
    }

    /** Returns true iff the two trees are structurally identical with equal values. */
    private boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        if (p == null || q == null) return false;
        if (p.val != q.val) return false;
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }
}
