package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.easy.solutions.DiameterOfBinaryTree.TreeNode;

class DiameterOfBinaryTreeTest {

    private final DiameterOfBinaryTree sol = new DiameterOfBinaryTree();

    // Example 1: [1,2,3,4,5] → diameter 3 (path 4-2-1-3 or 5-2-1-3)
    @Test
    void example1() {
        //       1
        //      / \
        //     2   3
        //    / \
        //   4   5
        TreeNode root = new TreeNode(1,
                new TreeNode(2, new TreeNode(4), new TreeNode(5)),
                new TreeNode(3));
        assertEquals(3, sol.diameterOfBinaryTree(root));
    }

    // Example 2: [1,2] → diameter 1
    @Test
    void example2() {
        TreeNode root = new TreeNode(1, new TreeNode(2), null);
        assertEquals(1, sol.diameterOfBinaryTree(root));
    }

    // Single node → diameter 0
    @Test
    void singleNode() {
        assertEquals(0, sol.diameterOfBinaryTree(new TreeNode(1)));
    }

    // Linear chain of 5 → diameter 4
    @Test
    void linearChain() {
        TreeNode root = new TreeNode(1,
                new TreeNode(2,
                    new TreeNode(3,
                        new TreeNode(4,
                            new TreeNode(5), null), null), null), null);
        assertEquals(4, sol.diameterOfBinaryTree(root));
    }

    // Path does not go through root: balanced left subtree is deeper
    //       1
    //      /
    //     2
    //    / \
    //   3   4
    //  /     \
    // 5       6
    // Diameter: 5-3-2-4-6 = 4 edges
    @Test
    void diameterNotThroughRoot() {
        TreeNode root = new TreeNode(1,
                new TreeNode(2,
                    new TreeNode(3, new TreeNode(5), null),
                    new TreeNode(4, null, new TreeNode(6))),
                null);
        assertEquals(4, sol.diameterOfBinaryTree(root));
    }

    // Null tree → 0
    @Test
    void nullTree() {
        assertEquals(0, sol.diameterOfBinaryTree(null));
    }
}
