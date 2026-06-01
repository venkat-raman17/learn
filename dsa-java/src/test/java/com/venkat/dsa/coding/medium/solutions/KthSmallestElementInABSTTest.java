package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.medium.solutions.KthSmallestElementInABST.TreeNode;

class KthSmallestElementInABSTTest {

    private final KthSmallestElementInABST sol = new KthSmallestElementInABST();

    // Example 1: [3,1,4,null,2], k=1 → 1
    @Test
    void example1() {
        TreeNode root = new TreeNode(3,
                new TreeNode(1, null, new TreeNode(2)),
                new TreeNode(4));
        assertEquals(1, sol.kthSmallest(root, 1));
    }

    // Example 2: [5,3,6,2,4,null,null,1], k=3 → 3
    @Test
    void example2() {
        TreeNode root = new TreeNode(5,
                new TreeNode(3,
                    new TreeNode(2, new TreeNode(1), null),
                    new TreeNode(4)),
                new TreeNode(6));
        // In-order: 1,2,3,4,5,6 → k=3 is 3
        assertEquals(3, sol.kthSmallest(root, 3));
    }

    // k equals total number of nodes → largest element
    @Test
    void kIsLargest() {
        TreeNode root = new TreeNode(3,
                new TreeNode(1, null, new TreeNode(2)),
                new TreeNode(4));
        // In-order: 1,2,3,4 → k=4 is 4
        assertEquals(4, sol.kthSmallest(root, 4));
    }

    // Single node → k=1 returns the only element
    @Test
    void singleNode() {
        assertEquals(42, sol.kthSmallest(new TreeNode(42), 1));
    }

    // In-order: 1,2,3,4,5,6,7 (perfect BST depth 3)
    @Test
    void perfectBST() {
        TreeNode root = new TreeNode(4,
                new TreeNode(2, new TreeNode(1), new TreeNode(3)),
                new TreeNode(6, new TreeNode(5), new TreeNode(7)));
        assertEquals(1, sol.kthSmallest(root, 1));
        assertEquals(4, sol.kthSmallest(root, 4));
        assertEquals(7, sol.kthSmallest(root, 7));
    }
}
