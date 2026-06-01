package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.easy.solutions.SubtreeOfAnotherTree.TreeNode;

class SubtreeOfAnotherTreeTest {

    private final SubtreeOfAnotherTree sol = new SubtreeOfAnotherTree();

    // Example 1: root=[3,4,5,1,2], subRoot=[4,1,2] → true
    @Test
    void example1() {
        TreeNode root = new TreeNode(3,
                new TreeNode(4, new TreeNode(1), new TreeNode(2)),
                new TreeNode(5));
        TreeNode sub = new TreeNode(4, new TreeNode(1), new TreeNode(2));
        assertTrue(sol.isSubtree(root, sub));
    }

    // Example 2: root=[3,4,5,1,2,null,null,null,null,0], subRoot=[4,1,2] → false
    // Node 4 in root has an extra child 0 under node 2
    @Test
    void example2ExtraChild() {
        TreeNode root = new TreeNode(3,
                new TreeNode(4,
                    new TreeNode(1),
                    new TreeNode(2, new TreeNode(0), null)),
                new TreeNode(5));
        TreeNode sub = new TreeNode(4, new TreeNode(1), new TreeNode(2));
        assertFalse(sol.isSubtree(root, sub));
    }

    // subRoot equals the whole root
    @Test
    void subRootIsRoot() {
        TreeNode root = new TreeNode(1, new TreeNode(2), new TreeNode(3));
        TreeNode sub  = new TreeNode(1, new TreeNode(2), new TreeNode(3));
        assertTrue(sol.isSubtree(root, sub));
    }

    // subRoot is a single leaf
    @Test
    void subRootIsLeaf() {
        TreeNode root = new TreeNode(1, new TreeNode(2), new TreeNode(3));
        assertTrue(sol.isSubtree(root, new TreeNode(2)));
        assertTrue(sol.isSubtree(root, new TreeNode(3)));
        assertFalse(sol.isSubtree(root, new TreeNode(9)));
    }

    // root is null → always false
    @Test
    void nullRoot() {
        assertFalse(sol.isSubtree(null, new TreeNode(1)));
    }
}
