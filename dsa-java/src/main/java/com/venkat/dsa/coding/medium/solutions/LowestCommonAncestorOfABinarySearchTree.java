package com.venkat.dsa.coding.medium.solutions;

/**
 * Lowest Common Ancestor of a Binary Search Tree (LeetCode 235)
 *
 * <p>Exploit the BST property: if both p and q are smaller than the current
 * node, the LCA must be in the left subtree; if both are larger, it is in the
 * right subtree; otherwise the current node is the split point — it is the LCA.
 * This gives an iterative solution that walks down one path only.
 *
 * <p><b>Time complexity:</b> O(h) — traverses at most one root-to-leaf path
 * (O(log n) balanced, O(n) skewed).
 * <b>Space complexity:</b> O(1) — iterative, no recursion stack.
 *
 * <p><b>Key insight:</b> In a BST the ordered structure lets us decide in O(1)
 * which direction to descend, making a linear scan unnecessary.
 */
public class LowestCommonAncestorOfABinarySearchTree {

    public static class TreeNode {
        public int val;
        public TreeNode left, right;
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        TreeNode cur = root;
        while (cur != null) {
            if (p.val < cur.val && q.val < cur.val) {
                cur = cur.left;          // both values in left subtree
            } else if (p.val > cur.val && q.val > cur.val) {
                cur = cur.right;         // both values in right subtree
            } else {
                return cur;              // split point — this is the LCA
            }
        }
        return null; // should never reach here for valid input
    }
}
