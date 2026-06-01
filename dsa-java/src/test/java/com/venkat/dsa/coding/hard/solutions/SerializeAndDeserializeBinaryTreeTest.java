package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.hard.solutions.SerializeAndDeserializeBinaryTree.TreeNode;
import java.util.ArrayList;
import java.util.List;

class SerializeAndDeserializeBinaryTreeTest {

    private final SerializeAndDeserializeBinaryTree codec = new SerializeAndDeserializeBinaryTree();

    /** Collect level-order values (non-null only) for comparison. */
    private List<Integer> levelOrderVals(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        java.util.Deque<TreeNode> q = new java.util.ArrayDeque<>();
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode n = q.poll();
            res.add(n.val);
            if (n.left  != null) q.offer(n.left);
            if (n.right != null) q.offer(n.right);
        }
        return res;
    }

    /** Full structural equality check (including shape). */
    private boolean structurallyEqual(TreeNode a, TreeNode b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.val == b.val
            && structurallyEqual(a.left, b.left)
            && structurallyEqual(a.right, b.right);
    }

    // Example 1: [1,2,3,null,null,4,5]
    @Test
    void example1() {
        TreeNode root = new TreeNode(1);
        root.left  = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.left  = new TreeNode(4);
        root.right.right = new TreeNode(5);

        String data     = codec.serialize(root);
        TreeNode result = codec.deserialize(data);

        assertTrue(structurallyEqual(root, result));
    }

    // Empty tree → serializes and deserializes to null
    @Test
    void emptyTree() {
        String data     = codec.serialize(null);
        TreeNode result = codec.deserialize(data);
        assertNull(result);
    }

    // Single node
    @Test
    void singleNode() {
        TreeNode root   = new TreeNode(42);
        String data     = codec.serialize(root);
        TreeNode result = codec.deserialize(data);
        assertNotNull(result);
        assertEquals(42, result.val);
        assertNull(result.left);
        assertNull(result.right);
    }

    // Left-skewed tree
    @Test
    void leftSkewed() {
        TreeNode root = new TreeNode(1,
                new TreeNode(2, new TreeNode(3), null), null);
        TreeNode result = codec.deserialize(codec.serialize(root));
        assertTrue(structurallyEqual(root, result));
    }

    // Right-skewed tree
    @Test
    void rightSkewed() {
        TreeNode root = new TreeNode(1, null,
                new TreeNode(2, null, new TreeNode(3)));
        TreeNode result = codec.deserialize(codec.serialize(root));
        assertTrue(structurallyEqual(root, result));
    }

    // Negative values
    @Test
    void negativeValues() {
        TreeNode root = new TreeNode(-1,
                new TreeNode(-2, new TreeNode(-3), null),
                new TreeNode(-4));
        TreeNode result = codec.deserialize(codec.serialize(root));
        assertTrue(structurallyEqual(root, result));
    }

    // Level-order values preserved after round-trip
    @Test
    void levelOrderPreservedRoundTrip() {
        //      1
        //     / \
        //    2   3
        //   /     \
        //  4       5
        TreeNode root = new TreeNode(1,
                new TreeNode(2, new TreeNode(4), null),
                new TreeNode(3, null, new TreeNode(5)));
        List<Integer> before = levelOrderVals(root);
        TreeNode result = codec.deserialize(codec.serialize(root));
        List<Integer> after = levelOrderVals(result);
        assertEquals(before, after);
        assertTrue(structurallyEqual(root, result));
    }
}
