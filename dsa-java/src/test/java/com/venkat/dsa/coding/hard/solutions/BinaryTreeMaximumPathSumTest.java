package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.hard.solutions.BinaryTreeMaximumPathSum.TreeNode;

class BinaryTreeMaximumPathSumTest {

    private final BinaryTreeMaximumPathSum sol = new BinaryTreeMaximumPathSum();

    // Example 1: [1,2,3] → 6  (2+1+3)
    @Test
    void example1() {
        TreeNode root = new TreeNode(1, new TreeNode(2), new TreeNode(3));
        assertEquals(6, sol.maxPathSum(root));
    }

    // Example 2: [-10,9,20,null,null,15,7] → 42  (15+20+7)
    @Test
    void example2() {
        TreeNode root = new TreeNode(-10,
                new TreeNode(9),
                new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        assertEquals(42, sol.maxPathSum(root));
    }

    // Single node → that node's value
    @Test
    void singleNode() {
        assertEquals(5, sol.maxPathSum(new TreeNode(5)));
    }

    // All negative → best is the least-negative single node
    @Test
    void allNegative() {
        TreeNode root = new TreeNode(-3, new TreeNode(-2), new TreeNode(-1));
        assertEquals(-1, sol.maxPathSum(root));
    }

    // Only negative root → -3 (no child path is better since children don't exist)
    @Test
    void singleNegativeNode() {
        assertEquals(-3, sol.maxPathSum(new TreeNode(-3)));
    }

    // Path that doesn't include root: best path is entirely in left subtree
    //     -10
    //     /
    //    5
    //   / \
    //  3   7
    // Best path: 3+5+7 = 15
    @Test
    void pathNotThroughRoot() {
        TreeNode root = new TreeNode(-10,
                new TreeNode(5, new TreeNode(3), new TreeNode(7)),
                null);
        assertEquals(15, sol.maxPathSum(root));
    }

    // Negative branches should not be included
    //   5
    //  / \
    // -4   3
    // Best path: 5+3 = 8 (not 5 + (-4))
    @Test
    void negativeBranchIgnored() {
        TreeNode root = new TreeNode(5, new TreeNode(-4), new TreeNode(3));
        assertEquals(8, sol.maxPathSum(root));
    }

    // Path through zigzag:
    //    2
    //   / \
    //  1   3
    //     /
    //    4
    // Best path: 4+3+2+1 = but it turns → 4+3+2 = 9 (can go 4->3->2->1 = 10 -- wait let's verify)
    // In-order: left subtree 1, root 2, right subtree with 3 and left child 4
    // Path 1-2-3-4 = 1+2+3+4 = 10 (this is a valid path: 4->3->2->1)
    @Test
    void zigzagPath() {
        TreeNode root = new TreeNode(2,
                new TreeNode(1),
                new TreeNode(3, new TreeNode(4), null));
        assertEquals(10, sol.maxPathSum(root));
    }
}
