package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class DiameterOfBinaryTreeTest {

    private final DiameterOfBinaryTree solution = new DiameterOfBinaryTree();

    @Test
    public void testExample1() {
        // [1,2,3,4,5] -> 3
        DiameterOfBinaryTree.TreeNode root = new DiameterOfBinaryTree.TreeNode(1,
            new DiameterOfBinaryTree.TreeNode(2,
                new DiameterOfBinaryTree.TreeNode(4),
                new DiameterOfBinaryTree.TreeNode(5)),
            new DiameterOfBinaryTree.TreeNode(3));

        assertEquals(3, solution.diameterOfBinaryTree(root));
    }

    @Test
    public void testExample2() {
        // [1,2] -> 1
        DiameterOfBinaryTree.TreeNode root = new DiameterOfBinaryTree.TreeNode(1,
            new DiameterOfBinaryTree.TreeNode(2),
            null);

        assertEquals(1, solution.diameterOfBinaryTree(root));
    }

    @Test
    public void testSingleNode() {
        DiameterOfBinaryTree.TreeNode root = new DiameterOfBinaryTree.TreeNode(1);
        assertEquals(0, solution.diameterOfBinaryTree(root));
    }
}
