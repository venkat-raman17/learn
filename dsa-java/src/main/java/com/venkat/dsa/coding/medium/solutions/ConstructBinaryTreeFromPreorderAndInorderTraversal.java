package com.venkat.dsa.coding.medium.solutions;

import java.util.HashMap;
import java.util.Map;

/**
 * Construct Binary Tree from Preorder and Inorder Traversal (LeetCode 105)
 *
 * <p>In a preorder traversal the first element is always the root. We locate
 * that root in the inorder array — everything to its left belongs to the left
 * subtree, everything to its right belongs to the right subtree. We build a
 * HashMap of {value → inorder index} for O(1) look-ups, then recurse with
 * array-index boundaries instead of copying sub-arrays.
 *
 * <p><b>Time complexity:</b> O(n) — each node is constructed exactly once.
 * <b>Space complexity:</b> O(n) — HashMap + O(h) recursion stack.
 *
 * <p><b>Key insight:</b> The preorder index of the current root advances
 * monotonically through the preorder array; passing it as a class-level counter
 * (or index-math) avoids recomputing offsets.
 */
public class ConstructBinaryTreeFromPreorderAndInorderTraversal {

    public static class TreeNode {
        public int val;
        public TreeNode left, right;
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    private int preIdx = 0;
    private Map<Integer, Integer> inorderIndex;

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        preIdx = 0;
        inorderIndex = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inorderIndex.put(inorder[i], i);    // value -> index in inorder
        }
        return build(preorder, 0, inorder.length - 1);
    }

    private TreeNode build(int[] preorder, int left, int right) {
        if (left > right) return null;

        int rootVal = preorder[preIdx++];       // next preorder element is the root
        TreeNode root = new TreeNode(rootVal);

        int mid = inorderIndex.get(rootVal);    // split point in inorder array
        root.left  = build(preorder, left,   mid - 1);
        root.right = build(preorder, mid + 1, right);
        return root;
    }
}
