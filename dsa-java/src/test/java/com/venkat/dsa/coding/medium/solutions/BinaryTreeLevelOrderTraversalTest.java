package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.medium.solutions.BinaryTreeLevelOrderTraversal.TreeNode;
import java.util.List;

class BinaryTreeLevelOrderTraversalTest {

    private final BinaryTreeLevelOrderTraversal sol = new BinaryTreeLevelOrderTraversal();

    // Example 1: [3,9,20,null,null,15,7] → [[3],[9,20],[15,7]]
    @Test
    void example1() {
        TreeNode root = new TreeNode(3,
                new TreeNode(9),
                new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        List<List<Integer>> res = sol.levelOrder(root);
        assertEquals(3, res.size());
        assertEquals(List.of(3), res.get(0));
        assertEquals(List.of(9, 20), res.get(1));
        assertEquals(List.of(15, 7), res.get(2));
    }

    // Example 2: [1] → [[1]]
    @Test
    void singleNode() {
        List<List<Integer>> res = sol.levelOrder(new TreeNode(1));
        assertEquals(1, res.size());
        assertEquals(List.of(1), res.get(0));
    }

    // Example 3: [] → []
    @Test
    void emptyTree() {
        assertTrue(sol.levelOrder(null).isEmpty());
    }

    // Left-skewed tree: 1-2-3-4
    @Test
    void leftSkewed() {
        TreeNode root = new TreeNode(1,
                new TreeNode(2, new TreeNode(3, new TreeNode(4), null), null), null);
        List<List<Integer>> res = sol.levelOrder(root);
        assertEquals(4, res.size());
        assertEquals(List.of(1), res.get(0));
        assertEquals(List.of(2), res.get(1));
        assertEquals(List.of(3), res.get(2));
        assertEquals(List.of(4), res.get(3));
    }

    // Complete tree depth 3: verifies level grouping
    @Test
    void completeTree() {
        TreeNode root = new TreeNode(1,
                new TreeNode(2, new TreeNode(4), new TreeNode(5)),
                new TreeNode(3, new TreeNode(6), new TreeNode(7)));
        List<List<Integer>> res = sol.levelOrder(root);
        assertEquals(3, res.size());
        assertEquals(List.of(1), res.get(0));
        assertEquals(List.of(2, 3), res.get(1));
        assertEquals(List.of(4, 5, 6, 7), res.get(2));
    }
}
