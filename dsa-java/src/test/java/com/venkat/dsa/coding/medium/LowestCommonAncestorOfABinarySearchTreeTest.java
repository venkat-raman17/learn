package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class LowestCommonAncestorOfABinarySearchTreeTest {

    private final LowestCommonAncestorOfABinarySearchTree solution =
        new LowestCommonAncestorOfABinarySearchTree();

    private LowestCommonAncestorOfABinarySearchTree.TreeNode buildBST() {
        // [6,2,8,0,4,7,9,null,null,3,5]
        LowestCommonAncestorOfABinarySearchTree.TreeNode n0 =
            new LowestCommonAncestorOfABinarySearchTree.TreeNode(0);
        LowestCommonAncestorOfABinarySearchTree.TreeNode n3 =
            new LowestCommonAncestorOfABinarySearchTree.TreeNode(3);
        LowestCommonAncestorOfABinarySearchTree.TreeNode n5 =
            new LowestCommonAncestorOfABinarySearchTree.TreeNode(5);
        LowestCommonAncestorOfABinarySearchTree.TreeNode n4 =
            new LowestCommonAncestorOfABinarySearchTree.TreeNode(4, n3, n5);
        LowestCommonAncestorOfABinarySearchTree.TreeNode n2 =
            new LowestCommonAncestorOfABinarySearchTree.TreeNode(2, n0, n4);
        LowestCommonAncestorOfABinarySearchTree.TreeNode n7 =
            new LowestCommonAncestorOfABinarySearchTree.TreeNode(7);
        LowestCommonAncestorOfABinarySearchTree.TreeNode n9 =
            new LowestCommonAncestorOfABinarySearchTree.TreeNode(9);
        LowestCommonAncestorOfABinarySearchTree.TreeNode n8 =
            new LowestCommonAncestorOfABinarySearchTree.TreeNode(8, n7, n9);
        return new LowestCommonAncestorOfABinarySearchTree.TreeNode(6, n2, n8);
    }

    @Test
    public void testExample1LCARoot() {
        LowestCommonAncestorOfABinarySearchTree.TreeNode root = buildBST();
        LowestCommonAncestorOfABinarySearchTree.TreeNode p =
            new LowestCommonAncestorOfABinarySearchTree.TreeNode(2);
        LowestCommonAncestorOfABinarySearchTree.TreeNode q =
            new LowestCommonAncestorOfABinarySearchTree.TreeNode(8);

        LowestCommonAncestorOfABinarySearchTree.TreeNode result =
            solution.lowestCommonAncestor(root, p, q);
        assertEquals(6, result.val);
    }

    @Test
    public void testExample2AncestorIsP() {
        LowestCommonAncestorOfABinarySearchTree.TreeNode root = buildBST();
        LowestCommonAncestorOfABinarySearchTree.TreeNode p =
            new LowestCommonAncestorOfABinarySearchTree.TreeNode(2);
        LowestCommonAncestorOfABinarySearchTree.TreeNode q =
            new LowestCommonAncestorOfABinarySearchTree.TreeNode(4);

        LowestCommonAncestorOfABinarySearchTree.TreeNode result =
            solution.lowestCommonAncestor(root, p, q);
        assertEquals(2, result.val);
    }

    @Test
    public void testLeafNodes() {
        LowestCommonAncestorOfABinarySearchTree.TreeNode root = buildBST();
        LowestCommonAncestorOfABinarySearchTree.TreeNode p =
            new LowestCommonAncestorOfABinarySearchTree.TreeNode(3);
        LowestCommonAncestorOfABinarySearchTree.TreeNode q =
            new LowestCommonAncestorOfABinarySearchTree.TreeNode(5);

        LowestCommonAncestorOfABinarySearchTree.TreeNode result =
            solution.lowestCommonAncestor(root, p, q);
        assertEquals(4, result.val);
    }
}
