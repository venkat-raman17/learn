package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class InvertBinaryTreeTest {

    private final InvertBinaryTree solution = new InvertBinaryTree();

    @Test
    public void testExample1() {
        // [4,2,7,1,3,6,9] -> [4,7,2,9,6,3,1]
        InvertBinaryTree.TreeNode root = new InvertBinaryTree.TreeNode(4,
            new InvertBinaryTree.TreeNode(2,
                new InvertBinaryTree.TreeNode(1),
                new InvertBinaryTree.TreeNode(3)),
            new InvertBinaryTree.TreeNode(7,
                new InvertBinaryTree.TreeNode(6),
                new InvertBinaryTree.TreeNode(9)));

        InvertBinaryTree.TreeNode result = solution.invertTree(root);

        assertEquals(4, result.val);
        assertEquals(7, result.left.val);
        assertEquals(2, result.right.val);
        assertEquals(9, result.left.left.val);
        assertEquals(6, result.left.right.val);
        assertEquals(3, result.right.left.val);
        assertEquals(1, result.right.right.val);
    }

    @Test
    public void testExample2() {
        // [2,1,3] -> [2,3,1]
        InvertBinaryTree.TreeNode root = new InvertBinaryTree.TreeNode(2,
            new InvertBinaryTree.TreeNode(1),
            new InvertBinaryTree.TreeNode(3));

        InvertBinaryTree.TreeNode result = solution.invertTree(root);

        assertEquals(2, result.val);
        assertEquals(3, result.left.val);
        assertEquals(1, result.right.val);
    }

    @Test
    public void testNullRoot() {
        assertNull(solution.invertTree(null));
    }
}
