package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class MaximumDepthOfBinaryTreeTest {

    private final MaximumDepthOfBinaryTree solution = new MaximumDepthOfBinaryTree();

    @Test
    public void testExample1() {
        // [3,9,20,null,null,15,7] -> 3
        MaximumDepthOfBinaryTree.TreeNode root = new MaximumDepthOfBinaryTree.TreeNode(3,
            new MaximumDepthOfBinaryTree.TreeNode(9),
            new MaximumDepthOfBinaryTree.TreeNode(20,
                new MaximumDepthOfBinaryTree.TreeNode(15),
                new MaximumDepthOfBinaryTree.TreeNode(7)));

        assertEquals(3, solution.maxDepth(root));
    }

    @Test
    public void testExample2() {
        // [1,null,2] -> 2
        MaximumDepthOfBinaryTree.TreeNode root = new MaximumDepthOfBinaryTree.TreeNode(1,
            null,
            new MaximumDepthOfBinaryTree.TreeNode(2));

        assertEquals(2, solution.maxDepth(root));
    }

    @Test
    public void testNullRoot() {
        assertEquals(0, solution.maxDepth(null));
    }
}
