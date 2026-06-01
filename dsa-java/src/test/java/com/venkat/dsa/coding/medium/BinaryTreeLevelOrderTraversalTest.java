package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class BinaryTreeLevelOrderTraversalTest {

    private final BinaryTreeLevelOrderTraversal solution = new BinaryTreeLevelOrderTraversal();

    @Test
    public void testExample1() {
        BinaryTreeLevelOrderTraversal.TreeNode root = new BinaryTreeLevelOrderTraversal.TreeNode(3,
            new BinaryTreeLevelOrderTraversal.TreeNode(9),
            new BinaryTreeLevelOrderTraversal.TreeNode(20,
                new BinaryTreeLevelOrderTraversal.TreeNode(15),
                new BinaryTreeLevelOrderTraversal.TreeNode(7)));

        List<List<Integer>> expected = Arrays.asList(
            Collections.singletonList(3),
            Arrays.asList(9, 20),
            Arrays.asList(15, 7)
        );
        assertEquals(expected, solution.levelOrder(root));
    }

    @Test
    public void testSingleNode() {
        BinaryTreeLevelOrderTraversal.TreeNode root = new BinaryTreeLevelOrderTraversal.TreeNode(1);
        List<List<Integer>> expected = Collections.singletonList(Collections.singletonList(1));
        assertEquals(expected, solution.levelOrder(root));
    }

    @Test
    public void testNullRoot() {
        assertEquals(Collections.emptyList(), solution.levelOrder(null));
    }
}
