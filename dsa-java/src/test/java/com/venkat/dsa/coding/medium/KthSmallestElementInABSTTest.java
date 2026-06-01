package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class KthSmallestElementInABSTTest {

    private final KthSmallestElementInABST solution = new KthSmallestElementInABST();

    @Test
    public void testExample1() {
        // [3,1,4,null,2], k=1 -> 1
        KthSmallestElementInABST.TreeNode root = new KthSmallestElementInABST.TreeNode(3,
            new KthSmallestElementInABST.TreeNode(1,
                null,
                new KthSmallestElementInABST.TreeNode(2)),
            new KthSmallestElementInABST.TreeNode(4));

        assertEquals(1, solution.kthSmallest(root, 1));
    }

    @Test
    public void testExample2() {
        // [5,3,6,2,4,null,null,1], k=3 -> 3
        KthSmallestElementInABST.TreeNode root = new KthSmallestElementInABST.TreeNode(5,
            new KthSmallestElementInABST.TreeNode(3,
                new KthSmallestElementInABST.TreeNode(2,
                    new KthSmallestElementInABST.TreeNode(1),
                    null),
                new KthSmallestElementInABST.TreeNode(4)),
            new KthSmallestElementInABST.TreeNode(6));

        assertEquals(3, solution.kthSmallest(root, 3));
    }

    @Test
    public void testKthLargest() {
        KthSmallestElementInABST.TreeNode root = new KthSmallestElementInABST.TreeNode(3,
            new KthSmallestElementInABST.TreeNode(1,
                null,
                new KthSmallestElementInABST.TreeNode(2)),
            new KthSmallestElementInABST.TreeNode(4));

        assertEquals(4, solution.kthSmallest(root, 4));
    }
}
