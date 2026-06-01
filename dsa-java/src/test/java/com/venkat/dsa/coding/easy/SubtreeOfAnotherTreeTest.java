package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class SubtreeOfAnotherTreeTest {

    private final SubtreeOfAnotherTree solution = new SubtreeOfAnotherTree();

    @Test
    public void testExample1IsSubtree() {
        SubtreeOfAnotherTree.TreeNode root = new SubtreeOfAnotherTree.TreeNode(3,
            new SubtreeOfAnotherTree.TreeNode(4,
                new SubtreeOfAnotherTree.TreeNode(1),
                new SubtreeOfAnotherTree.TreeNode(2)),
            new SubtreeOfAnotherTree.TreeNode(5));
        SubtreeOfAnotherTree.TreeNode subRoot = new SubtreeOfAnotherTree.TreeNode(4,
            new SubtreeOfAnotherTree.TreeNode(1),
            new SubtreeOfAnotherTree.TreeNode(2));

        assertTrue(solution.isSubtree(root, subRoot));
    }

    @Test
    public void testExample2NotSubtree() {
        SubtreeOfAnotherTree.TreeNode root = new SubtreeOfAnotherTree.TreeNode(3,
            new SubtreeOfAnotherTree.TreeNode(4,
                new SubtreeOfAnotherTree.TreeNode(1),
                new SubtreeOfAnotherTree.TreeNode(2,
                    new SubtreeOfAnotherTree.TreeNode(0),
                    null)),
            new SubtreeOfAnotherTree.TreeNode(5));
        SubtreeOfAnotherTree.TreeNode subRoot = new SubtreeOfAnotherTree.TreeNode(4,
            new SubtreeOfAnotherTree.TreeNode(1),
            new SubtreeOfAnotherTree.TreeNode(2));

        assertFalse(solution.isSubtree(root, subRoot));
    }

    @Test
    public void testSameTree() {
        SubtreeOfAnotherTree.TreeNode root = new SubtreeOfAnotherTree.TreeNode(1,
            new SubtreeOfAnotherTree.TreeNode(2),
            new SubtreeOfAnotherTree.TreeNode(3));
        SubtreeOfAnotherTree.TreeNode subRoot = new SubtreeOfAnotherTree.TreeNode(1,
            new SubtreeOfAnotherTree.TreeNode(2),
            new SubtreeOfAnotherTree.TreeNode(3));

        assertTrue(solution.isSubtree(root, subRoot));
    }
}
