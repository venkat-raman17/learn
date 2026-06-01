package com.venkat.dsa.coding.medium.solutions;

/**
 * Kth Smallest Element in a BST (LeetCode 230)
 *
 * <p>In-order traversal of a BST visits nodes in ascending sorted order.
 * We perform an iterative in-order traversal (left → node → right) using an
 * explicit stack, decrementing a counter each time we process a node and
 * returning as soon as the counter reaches zero — no need to finish the traversal.
 *
 * <p><b>Time complexity:</b> O(h + k) — descend to the leftmost leaf O(h), then
 * visit k nodes.
 * <b>Space complexity:</b> O(h) — stack holds at most one root-to-node path.
 *
 * <p><b>Key insight:</b> In-order traversal of a BST is always sorted ascending,
 * so the k-th node visited is the k-th smallest element.
 */
public class KthSmallestElementInABST {

    public static class TreeNode {
        public int val;
        public TreeNode left, right;
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    public int kthSmallest(TreeNode root, int k) {
        java.util.Deque<TreeNode> stack = new java.util.ArrayDeque<>();
        TreeNode cur = root;

        while (cur != null || !stack.isEmpty()) {
            // Go as far left as possible
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();          // process node (in-order visit)
            if (--k == 0) return cur.val;
            cur = cur.right;            // move to right subtree
        }
        return -1; // k out of range — unreachable for valid input
    }
}
