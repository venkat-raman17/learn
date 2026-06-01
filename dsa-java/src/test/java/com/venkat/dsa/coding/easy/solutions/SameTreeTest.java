package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.easy.solutions.SameTree.TreeNode;

class SameTreeTest {

    private final SameTree sol = new SameTree();

    // Example 1: [1,2,3] vs [1,2,3] → true
    @Test
    void example1() {
        TreeNode p = new TreeNode(1, new TreeNode(2), new TreeNode(3));
        TreeNode q = new TreeNode(1, new TreeNode(2), new TreeNode(3));
        assertTrue(sol.isSameTree(p, q));
    }

    // Example 2: [1,2] vs [1,null,2] → false (structural difference)
    @Test
    void example2StructureDiffers() {
        TreeNode p = new TreeNode(1, new TreeNode(2), null);
        TreeNode q = new TreeNode(1, null, new TreeNode(2));
        assertFalse(sol.isSameTree(p, q));
    }

    // Example 3: [1,2,1] vs [1,1,2] → false (value difference)
    @Test
    void example3ValueDiffers() {
        TreeNode p = new TreeNode(1, new TreeNode(2), new TreeNode(1));
        TreeNode q = new TreeNode(1, new TreeNode(1), new TreeNode(2));
        assertFalse(sol.isSameTree(p, q));
    }

    // Both null → true
    @Test
    void bothNull() {
        assertTrue(sol.isSameTree(null, null));
    }

    // One null → false
    @Test
    void oneNull() {
        assertFalse(sol.isSameTree(new TreeNode(1), null));
        assertFalse(sol.isSameTree(null, new TreeNode(1)));
    }

    // Identical single nodes → true
    @Test
    void singleNodes() {
        assertTrue(sol.isSameTree(new TreeNode(5), new TreeNode(5)));
    }

    // Single nodes with different values → false
    @Test
    void singleNodesDiffer() {
        assertFalse(sol.isSameTree(new TreeNode(5), new TreeNode(6)));
    }
}
