package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.easy.solutions.BalancedBinaryTree.TreeNode;

class BalancedBinaryTreeTest {

    private final BalancedBinaryTree sol = new BalancedBinaryTree();

    // Example 1: [3,9,20,null,null,15,7] → balanced
    @Test
    void example1Balanced() {
        TreeNode root = new TreeNode(3,
                new TreeNode(9),
                new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        assertTrue(sol.isBalanced(root));
    }

    // Example 2: [1,2,2,3,3,null,null,4,4] → NOT balanced
    // Height of left subtree = 3, height of right subtree = 1
    @Test
    void example2Unbalanced() {
        TreeNode root = new TreeNode(1,
                new TreeNode(2,
                    new TreeNode(3, new TreeNode(4), new TreeNode(4)),
                    new TreeNode(3)),
                new TreeNode(2));
        assertFalse(sol.isBalanced(root));
    }

    // Empty tree → balanced
    @Test
    void emptyTree() {
        assertTrue(sol.isBalanced(null));
    }

    // Single node → balanced
    @Test
    void singleNode() {
        assertTrue(sol.isBalanced(new TreeNode(1)));
    }

    // Skewed left chain of length 3 → unbalanced at root (heights 2 vs 0)
    @Test
    void leftSkewed() {
        TreeNode root = new TreeNode(1,
                new TreeNode(2, new TreeNode(3), null),
                null);
        assertFalse(sol.isBalanced(root));
    }

    // Perfect tree depth 3 → balanced
    @Test
    void perfectTree() {
        TreeNode root = new TreeNode(1,
                new TreeNode(2, new TreeNode(4), new TreeNode(5)),
                new TreeNode(3, new TreeNode(6), new TreeNode(7)));
        assertTrue(sol.isBalanced(root));
    }

    // Difference of exactly 1 → balanced
    @Test
    void diffOfOne() {
        // Height left=2, right=1 → |2-1|=1 → balanced
        TreeNode root = new TreeNode(1,
                new TreeNode(2, new TreeNode(3), null),
                new TreeNode(4));
        assertTrue(sol.isBalanced(root));
    }
}
