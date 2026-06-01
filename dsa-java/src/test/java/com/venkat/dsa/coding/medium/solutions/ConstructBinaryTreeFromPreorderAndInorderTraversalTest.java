package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.medium.solutions.ConstructBinaryTreeFromPreorderAndInorderTraversal.TreeNode;
import java.util.ArrayList;
import java.util.List;

class ConstructBinaryTreeFromPreorderAndInorderTraversalTest {

    private final ConstructBinaryTreeFromPreorderAndInorderTraversal sol =
            new ConstructBinaryTreeFromPreorderAndInorderTraversal();

    /** Collect in-order values to verify reconstruction. */
    private List<Integer> inOrder(TreeNode node) {
        List<Integer> res = new ArrayList<>();
        inOrderHelper(node, res);
        return res;
    }

    private void inOrderHelper(TreeNode node, List<Integer> res) {
        if (node == null) return;
        inOrderHelper(node.left, res);
        res.add(node.val);
        inOrderHelper(node.right, res);
    }

    /** Collect preorder values to verify reconstruction. */
    private List<Integer> preOrder(TreeNode node) {
        List<Integer> res = new ArrayList<>();
        preOrderHelper(node, res);
        return res;
    }

    private void preOrderHelper(TreeNode node, List<Integer> res) {
        if (node == null) return;
        res.add(node.val);
        preOrderHelper(node.left, res);
        preOrderHelper(node.right, res);
    }

    // Example 1: preorder=[3,9,20,15,7], inorder=[9,3,15,20,7]
    @Test
    void example1() {
        int[] pre = {3, 9, 20, 15, 7};
        int[] in  = {9, 3, 15, 20, 7};
        TreeNode root = sol.buildTree(pre, in);
        // Verify root
        assertEquals(3, root.val);
        assertEquals(9, root.left.val);
        assertEquals(20, root.right.val);
        assertNull(root.left.left);
        assertNull(root.left.right);
        assertEquals(15, root.right.left.val);
        assertEquals(7, root.right.right.val);
    }

    // Example 2: preorder=[-1], inorder=[-1]
    @Test
    void singleNode() {
        int[] pre = {-1};
        int[] in  = {-1};
        TreeNode root = sol.buildTree(pre, in);
        assertEquals(-1, root.val);
        assertNull(root.left);
        assertNull(root.right);
    }

    // Verify round-trip: reconstructed traversals match originals
    @Test
    void roundTrip() {
        int[] pre = {1, 2, 4, 5, 3, 6, 7};
        int[] in  = {4, 2, 5, 1, 6, 3, 7};
        TreeNode root = sol.buildTree(pre, in);

        List<Integer> preResult = preOrder(root);
        List<Integer> inResult  = inOrder(root);

        List<Integer> expectedPre = new ArrayList<>();
        for (int v : pre) expectedPre.add(v);
        List<Integer> expectedIn = new ArrayList<>();
        for (int v : in)  expectedIn.add(v);

        assertEquals(expectedPre, preResult);
        assertEquals(expectedIn,  inResult);
    }

    // Left-skewed tree: preorder=[1,2,3], inorder=[3,2,1]
    @Test
    void leftSkewed() {
        int[] pre = {1, 2, 3};
        int[] in  = {3, 2, 1};
        TreeNode root = sol.buildTree(pre, in);
        assertEquals(1, root.val);
        assertEquals(2, root.left.val);
        assertEquals(3, root.left.left.val);
        assertNull(root.right);
    }

    // Right-skewed tree: preorder=[1,2,3], inorder=[1,2,3]
    @Test
    void rightSkewed() {
        int[] pre = {1, 2, 3};
        int[] in  = {1, 2, 3};
        TreeNode root = sol.buildTree(pre, in);
        assertEquals(1, root.val);
        assertNull(root.left);
        assertEquals(2, root.right.val);
        assertEquals(3, root.right.right.val);
    }
}
