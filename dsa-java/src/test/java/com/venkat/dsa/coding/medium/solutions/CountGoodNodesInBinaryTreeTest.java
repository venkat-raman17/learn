package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.medium.solutions.CountGoodNodesInBinaryTree.TreeNode;

class CountGoodNodesInBinaryTreeTest {

    private final CountGoodNodesInBinaryTree sol = new CountGoodNodesInBinaryTree();

    // Example 1: [3,1,4,3,null,1,5] → 4
    // Good nodes: 3 (root), 3 (left.left, path max=3), 4 (right), 5 (right.right, path max=4→5>=4)
    @Test
    void example1() {
        //       3
        //      / \
        //     1   4
        //    /   / \
        //   3   1   5
        TreeNode root = new TreeNode(3,
                new TreeNode(1, new TreeNode(3), null),
                new TreeNode(4, new TreeNode(1), new TreeNode(5)));
        assertEquals(4, sol.goodNodes(root));
    }

    // Example 2: [3,3,null,4,2] → 3
    // Path to 4: 3->3->4 max=3, 4>=3 ✓. Path to 2: 3->3->2 max=3, 2<3 ✗.
    @Test
    void example2() {
        //   3
        //  /
        // 3
        // / \
        //4   2
        TreeNode root = new TreeNode(3,
                new TreeNode(3, new TreeNode(4), new TreeNode(2)),
                null);
        assertEquals(3, sol.goodNodes(root));
    }

    // Example 3: [1] → 1
    @Test
    void singleNode() {
        assertEquals(1, sol.goodNodes(new TreeNode(1)));
    }

    // Decreasing path: 5->4->3->2->1 → only root is good
    @Test
    void decreasingPath() {
        TreeNode root = new TreeNode(5,
                new TreeNode(4,
                    new TreeNode(3,
                        new TreeNode(2, new TreeNode(1), null), null), null), null);
        assertEquals(1, sol.goodNodes(root));
    }

    // Increasing path: 1->2->3->4->5 → all nodes are good
    @Test
    void increasingPath() {
        TreeNode root = new TreeNode(1,
                new TreeNode(2,
                    new TreeNode(3,
                        new TreeNode(4, new TreeNode(5), null), null), null), null);
        assertEquals(5, sol.goodNodes(root));
    }

    // All equal values → all nodes are good
    @Test
    void allEqualValues() {
        TreeNode root = new TreeNode(2,
                new TreeNode(2, new TreeNode(2), new TreeNode(2)),
                new TreeNode(2));
        assertEquals(5, sol.goodNodes(root));
    }
}
