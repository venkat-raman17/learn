package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class ValidateBinarySearchTreeTest {

    private final ValidateBinarySearchTree solution = new ValidateBinarySearchTree();

    @Test
    public void testExample1Valid() {
        ValidateBinarySearchTree.TreeNode root = new ValidateBinarySearchTree.TreeNode(2,
            new ValidateBinarySearchTree.TreeNode(1),
            new ValidateBinarySearchTree.TreeNode(3));

        assertTrue(solution.isValidBST(root));
    }

    @Test
    public void testExample2Invalid() {
        ValidateBinarySearchTree.TreeNode root = new ValidateBinarySearchTree.TreeNode(5,
            new ValidateBinarySearchTree.TreeNode(1),
            new ValidateBinarySearchTree.TreeNode(4,
                new ValidateBinarySearchTree.TreeNode(3),
                new ValidateBinarySearchTree.TreeNode(6)));

        assertFalse(solution.isValidBST(root));
    }

    @Test
    public void testSubtreeViolation() {
        // [10,5,15,null,null,6,20] — 6 is in the right subtree of 10 but 6 < 10
        ValidateBinarySearchTree.TreeNode root = new ValidateBinarySearchTree.TreeNode(10,
            new ValidateBinarySearchTree.TreeNode(5),
            new ValidateBinarySearchTree.TreeNode(15,
                new ValidateBinarySearchTree.TreeNode(6),
                new ValidateBinarySearchTree.TreeNode(20)));

        assertFalse(solution.isValidBST(root));
    }
}
