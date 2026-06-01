package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.easy.solutions.MaximumDepthOfBinaryTree.TreeNode;

class MaximumDepthOfBinaryTreeTest {

    private final MaximumDepthOfBinaryTree sol = new MaximumDepthOfBinaryTree();

    // Example 1: [3,9,20,null,null,15,7] → depth 3
    @Test
    void example1() {
        //       3
        //      / \
        //     9  20
        //        / \
        //       15   7
        TreeNode root = new TreeNode(3,
                new TreeNode(9),
                new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        assertEquals(3, sol.maxDepth(root));
    }

    // Example 2: [1,null,2] → depth 2
    @Test
    void example2() {
        TreeNode root = new TreeNode(1, null, new TreeNode(2));
        assertEquals(2, sol.maxDepth(root));
    }

    // Empty tree → 0
    @Test
    void emptyTree() {
        assertEquals(0, sol.maxDepth(null));
    }

    // Single node → 1
    @Test
    void singleNode() {
        assertEquals(1, sol.maxDepth(new TreeNode(5)));
    }

    // Linear (skewed right) tree of 5 nodes → depth 5
    @Test
    void skewedTree() {
        TreeNode root = new TreeNode(1,
                null,
                new TreeNode(2,
                    null,
                    new TreeNode(3,
                        null,
                        new TreeNode(4,
                            null,
                            new TreeNode(5)))));
        assertEquals(5, sol.maxDepth(root));
    }

    // Perfect binary tree of depth 4 (15 nodes)
    @Test
    void perfectTree() {
        TreeNode l3 = new TreeNode(4, new TreeNode(8), new TreeNode(9));
        TreeNode l4 = new TreeNode(5, new TreeNode(10), new TreeNode(11));
        TreeNode l5 = new TreeNode(6, new TreeNode(12), new TreeNode(13));
        TreeNode l6 = new TreeNode(7, new TreeNode(14), new TreeNode(15));
        TreeNode l1 = new TreeNode(2, l3, l4);
        TreeNode l2 = new TreeNode(3, l5, l6);
        TreeNode root = new TreeNode(1, l1, l2);
        assertEquals(4, sol.maxDepth(root));
    }
}
