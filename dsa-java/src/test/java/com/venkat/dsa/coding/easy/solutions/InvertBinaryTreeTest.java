package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.easy.solutions.InvertBinaryTree.TreeNode;

class InvertBinaryTreeTest {

    private final InvertBinaryTree sol = new InvertBinaryTree();

    /** Helper: build a tree by level-order array (null = absent). */
    private TreeNode build(Integer... vals) {
        if (vals.length == 0 || vals[0] == null) return null;
        TreeNode root = new TreeNode(vals[0]);
        java.util.Deque<TreeNode> q = new java.util.ArrayDeque<>();
        q.offer(root);
        int i = 1;
        while (!q.isEmpty() && i < vals.length) {
            TreeNode n = q.poll();
            if (i < vals.length && vals[i] != null) { n.left  = new TreeNode(vals[i]); q.offer(n.left);  } i++;
            if (i < vals.length && vals[i] != null) { n.right = new TreeNode(vals[i]); q.offer(n.right); } i++;
        }
        return root;
    }

    /** Collect level-order values including nulls as a list for comparison. */
    private java.util.List<Integer> levelOrder(TreeNode root) {
        java.util.List<Integer> res = new java.util.ArrayList<>();
        if (root == null) return res;
        java.util.Deque<TreeNode> q = new java.util.ArrayDeque<>();
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode n = q.poll();
            res.add(n.val);
            if (n.left  != null) q.offer(n.left);
            if (n.right != null) q.offer(n.right);
        }
        return res;
    }

    // Example 1: [4,2,7,1,3,6,9] → [4,7,2,9,6,3,1]
    @Test
    void example1() {
        TreeNode root = build(4, 2, 7, 1, 3, 6, 9);
        TreeNode inv  = sol.invertTree(root);
        // Root stays 4; left becomes 7, right becomes 2
        assertEquals(4, inv.val);
        assertEquals(7, inv.left.val);
        assertEquals(2, inv.right.val);
        assertEquals(9, inv.left.left.val);
        assertEquals(6, inv.left.right.val);
        assertEquals(3, inv.right.left.val);
        assertEquals(1, inv.right.right.val);
    }

    // Example 2: [2,1,3] → [2,3,1]
    @Test
    void example2() {
        TreeNode root = build(2, 1, 3);
        TreeNode inv  = sol.invertTree(root);
        assertEquals(2, inv.val);
        assertEquals(3, inv.left.val);
        assertEquals(1, inv.right.val);
    }

    // Edge: empty tree
    @Test
    void emptyTree() {
        assertNull(sol.invertTree(null));
    }

    // Edge: single node
    @Test
    void singleNode() {
        TreeNode root = new TreeNode(42);
        TreeNode inv  = sol.invertTree(root);
        assertEquals(42, inv.val);
        assertNull(inv.left);
        assertNull(inv.right);
    }

    // Inverting twice returns original structure
    @Test
    void doubleInvertRestores() {
        TreeNode root = build(4, 2, 7, 1, 3, 6, 9);
        java.util.List<Integer> before = levelOrder(root);
        sol.invertTree(sol.invertTree(root));
        java.util.List<Integer> after = levelOrder(root);
        assertEquals(before, after);
    }
}
