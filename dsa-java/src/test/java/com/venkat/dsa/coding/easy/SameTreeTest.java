package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class SameTreeTest {

    private final SameTree solution = new SameTree();

    @Test
    public void testExample1Same() {
        SameTree.TreeNode p = new SameTree.TreeNode(1,
            new SameTree.TreeNode(2),
            new SameTree.TreeNode(3));
        SameTree.TreeNode q = new SameTree.TreeNode(1,
            new SameTree.TreeNode(2),
            new SameTree.TreeNode(3));

        assertTrue(solution.isSameTree(p, q));
    }

    @Test
    public void testExample2DifferentStructure() {
        SameTree.TreeNode p = new SameTree.TreeNode(1,
            new SameTree.TreeNode(2),
            null);
        SameTree.TreeNode q = new SameTree.TreeNode(1,
            null,
            new SameTree.TreeNode(2));

        assertFalse(solution.isSameTree(p, q));
    }

    @Test
    public void testDifferentValues() {
        SameTree.TreeNode p = new SameTree.TreeNode(1,
            new SameTree.TreeNode(2),
            new SameTree.TreeNode(1));
        SameTree.TreeNode q = new SameTree.TreeNode(1,
            new SameTree.TreeNode(1),
            new SameTree.TreeNode(2));

        assertFalse(solution.isSameTree(p, q));
    }
}
