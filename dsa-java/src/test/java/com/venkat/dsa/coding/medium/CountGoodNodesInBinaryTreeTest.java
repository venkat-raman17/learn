package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class CountGoodNodesInBinaryTreeTest {

    private final CountGoodNodesInBinaryTree solution = new CountGoodNodesInBinaryTree();

    @Test
    public void testExample1() {
        // [3,1,4,3,null,1,5] -> 4
        CountGoodNodesInBinaryTree.TreeNode root = new CountGoodNodesInBinaryTree.TreeNode(3,
            new CountGoodNodesInBinaryTree.TreeNode(1,
                new CountGoodNodesInBinaryTree.TreeNode(3),
                null),
            new CountGoodNodesInBinaryTree.TreeNode(4,
                new CountGoodNodesInBinaryTree.TreeNode(1),
                new CountGoodNodesInBinaryTree.TreeNode(5)));

        assertEquals(4, solution.goodNodes(root));
    }

    @Test
    public void testExample2() {
        // [3,3,null,4,2] -> 3
        CountGoodNodesInBinaryTree.TreeNode root = new CountGoodNodesInBinaryTree.TreeNode(3,
            new CountGoodNodesInBinaryTree.TreeNode(3,
                new CountGoodNodesInBinaryTree.TreeNode(4),
                new CountGoodNodesInBinaryTree.TreeNode(2)),
            null);

        assertEquals(3, solution.goodNodes(root));
    }

    @Test
    public void testSingleNode() {
        CountGoodNodesInBinaryTree.TreeNode root = new CountGoodNodesInBinaryTree.TreeNode(1);
        assertEquals(1, solution.goodNodes(root));
    }
}
