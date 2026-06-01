package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class BinaryTreeMaximumPathSumTest {

    private final BinaryTreeMaximumPathSum solution = new BinaryTreeMaximumPathSum();

    @Test
    public void testExample1() {
        // [1,2,3] -> 6
        BinaryTreeMaximumPathSum.TreeNode root = new BinaryTreeMaximumPathSum.TreeNode(1,
            new BinaryTreeMaximumPathSum.TreeNode(2),
            new BinaryTreeMaximumPathSum.TreeNode(3));

        assertEquals(6, solution.maxPathSum(root));
    }

    @Test
    public void testExample2() {
        // [-10,9,20,null,null,15,7] -> 42
        BinaryTreeMaximumPathSum.TreeNode root = new BinaryTreeMaximumPathSum.TreeNode(-10,
            new BinaryTreeMaximumPathSum.TreeNode(9),
            new BinaryTreeMaximumPathSum.TreeNode(20,
                new BinaryTreeMaximumPathSum.TreeNode(15),
                new BinaryTreeMaximumPathSum.TreeNode(7)));

        assertEquals(42, solution.maxPathSum(root));
    }

    @Test
    public void testAllNegative() {
        // [-3] -> -3 (must pick at least one node)
        BinaryTreeMaximumPathSum.TreeNode root = new BinaryTreeMaximumPathSum.TreeNode(-3);
        assertEquals(-3, solution.maxPathSum(root));
    }
}
