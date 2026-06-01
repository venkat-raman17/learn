package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class ConstructBinaryTreeFromPreorderAndInorderTraversalTest {

    private final ConstructBinaryTreeFromPreorderAndInorderTraversal solution =
        new ConstructBinaryTreeFromPreorderAndInorderTraversal();

    @Test
    public void testExample1() {
        int[] preorder = {3, 9, 20, 15, 7};
        int[] inorder = {9, 3, 15, 20, 7};

        ConstructBinaryTreeFromPreorderAndInorderTraversal.TreeNode root =
            solution.buildTree(preorder, inorder);

        assertEquals(3, root.val);
        assertEquals(9, root.left.val);
        assertEquals(20, root.right.val);
        assertNull(root.left.left);
        assertNull(root.left.right);
        assertEquals(15, root.right.left.val);
        assertEquals(7, root.right.right.val);
    }

    @Test
    public void testSingleNode() {
        int[] preorder = {-1};
        int[] inorder = {-1};

        ConstructBinaryTreeFromPreorderAndInorderTraversal.TreeNode root =
            solution.buildTree(preorder, inorder);

        assertEquals(-1, root.val);
        assertNull(root.left);
        assertNull(root.right);
    }

    @Test
    public void testTwoNodes() {
        int[] preorder = {1, 2};
        int[] inorder = {2, 1};

        ConstructBinaryTreeFromPreorderAndInorderTraversal.TreeNode root =
            solution.buildTree(preorder, inorder);

        assertEquals(1, root.val);
        assertEquals(2, root.left.val);
        assertNull(root.right);
    }
}
