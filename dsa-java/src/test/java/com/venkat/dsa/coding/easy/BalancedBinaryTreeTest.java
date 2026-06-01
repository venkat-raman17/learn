package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class BalancedBinaryTreeTest {

    private final BalancedBinaryTree solution = new BalancedBinaryTree();

    @Test
    public void testExample1Balanced() {
        // [3,9,20,null,null,15,7] -> true
        BalancedBinaryTree.TreeNode root = new BalancedBinaryTree.TreeNode(3,
            new BalancedBinaryTree.TreeNode(9),
            new BalancedBinaryTree.TreeNode(20,
                new BalancedBinaryTree.TreeNode(15),
                new BalancedBinaryTree.TreeNode(7)));

        assertTrue(solution.isBalanced(root));
    }

    @Test
    public void testExample2Unbalanced() {
        // [1,2,2,3,3,null,null,4,4] -> false
        BalancedBinaryTree.TreeNode root = new BalancedBinaryTree.TreeNode(1,
            new BalancedBinaryTree.TreeNode(2,
                new BalancedBinaryTree.TreeNode(3,
                    new BalancedBinaryTree.TreeNode(4),
                    new BalancedBinaryTree.TreeNode(4)),
                new BalancedBinaryTree.TreeNode(3)),
            new BalancedBinaryTree.TreeNode(2));

        assertFalse(solution.isBalanced(root));
    }

    @Test
    public void testNullRoot() {
        assertTrue(solution.isBalanced(null));
    }
}
