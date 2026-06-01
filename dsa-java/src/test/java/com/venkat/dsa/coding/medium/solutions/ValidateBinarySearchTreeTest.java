package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.medium.solutions.ValidateBinarySearchTree.TreeNode;

class ValidateBinarySearchTreeTest {

    private final ValidateBinarySearchTree sol = new ValidateBinarySearchTree();

    // Example 1: [2,1,3] → true
    @Test
    void example1() {
        TreeNode root = new TreeNode(2, new TreeNode(1), new TreeNode(3));
        assertTrue(sol.isValidBST(root));
    }

    // Example 2: [5,1,4,null,null,3,6] → false (4 is in right subtree of 5 but 4<5)
    @Test
    void example2() {
        TreeNode root = new TreeNode(5,
                new TreeNode(1),
                new TreeNode(4, new TreeNode(3), new TreeNode(6)));
        assertFalse(sol.isValidBST(root));
    }

    // Single node → always valid
    @Test
    void singleNode() {
        assertTrue(sol.isValidBST(new TreeNode(1)));
    }

    // Null tree → valid
    @Test
    void nullTree() {
        assertTrue(sol.isValidBST(null));
    }

    // Subtree violation (not just direct child):
    //   5
    //  / \
    // 4   6
    //    / \
    //   3   7   ← 3 < 5 but in right subtree of 5 → invalid
    @Test
    void subtreeViolation() {
        TreeNode root = new TreeNode(5,
                new TreeNode(4),
                new TreeNode(6, new TreeNode(3), new TreeNode(7)));
        assertFalse(sol.isValidBST(root));
    }

    // Duplicate values → invalid (strict BST requires strict inequality)
    @Test
    void duplicateValues() {
        TreeNode root = new TreeNode(2, new TreeNode(2), null);
        assertFalse(sol.isValidBST(root));
    }

    // Edge: Integer.MIN_VALUE and Integer.MAX_VALUE nodes (bounds check)
    @Test
    void integerBoundaryValues() {
        TreeNode root = new TreeNode(Integer.MIN_VALUE, null, new TreeNode(Integer.MAX_VALUE));
        assertTrue(sol.isValidBST(root));
    }

    // Integer.MAX_VALUE as root with same value right child → invalid
    @Test
    void maxValueRightDuplicate() {
        TreeNode root = new TreeNode(Integer.MAX_VALUE, null, new TreeNode(Integer.MAX_VALUE));
        assertFalse(sol.isValidBST(root));
    }
}
