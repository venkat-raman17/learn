package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.medium.solutions.LowestCommonAncestorOfABinarySearchTree.TreeNode;

class LowestCommonAncestorOfABinarySearchTreeTest {

    private final LowestCommonAncestorOfABinarySearchTree sol =
            new LowestCommonAncestorOfABinarySearchTree();

    /**
     * BST:
     *        6
     *       / \
     *      2   8
     *     / \ / \
     *    0  4 7  9
     *      / \
     *     3   5
     */
    private TreeNode buildBST() {
        TreeNode n0 = new TreeNode(0);
        TreeNode n3 = new TreeNode(3);
        TreeNode n5 = new TreeNode(5);
        TreeNode n7 = new TreeNode(7);
        TreeNode n9 = new TreeNode(9);
        TreeNode n4 = new TreeNode(4, n3, n5);
        TreeNode n2 = new TreeNode(2, n0, n4);
        TreeNode n8 = new TreeNode(8, n7, n9);
        return new TreeNode(6, n2, n8);
    }

    // Example 1: LCA(2, 8) = 6
    @Test
    void example1() {
        TreeNode root = buildBST();
        TreeNode p = new TreeNode(2);
        TreeNode q = new TreeNode(8);
        assertEquals(6, sol.lowestCommonAncestor(root, p, q).val);
    }

    // Example 2: LCA(2, 4) = 2
    @Test
    void example2() {
        TreeNode root = buildBST();
        TreeNode p = new TreeNode(2);
        TreeNode q = new TreeNode(4);
        assertEquals(2, sol.lowestCommonAncestor(root, p, q).val);
    }

    // LCA(3, 5) = 4
    @Test
    void lcaWithinSubtree() {
        TreeNode root = buildBST();
        TreeNode p = new TreeNode(3);
        TreeNode q = new TreeNode(5);
        assertEquals(4, sol.lowestCommonAncestor(root, p, q).val);
    }

    // LCA(7, 9) = 8
    @Test
    void lcaRightSubtree() {
        TreeNode root = buildBST();
        TreeNode p = new TreeNode(7);
        TreeNode q = new TreeNode(9);
        assertEquals(8, sol.lowestCommonAncestor(root, p, q).val);
    }

    // One of p/q is the root itself
    @Test
    void oneNodeIsRoot() {
        TreeNode root = buildBST();
        TreeNode p = new TreeNode(6);
        TreeNode q = new TreeNode(3);
        assertEquals(6, sol.lowestCommonAncestor(root, p, q).val);
    }

    // Two-node BST: root=2, right=3, LCA(2,3) = 2
    @Test
    void twoNodeBST() {
        TreeNode root = new TreeNode(2, null, new TreeNode(3));
        assertEquals(2, sol.lowestCommonAncestor(root, new TreeNode(2), new TreeNode(3)).val);
    }
}
