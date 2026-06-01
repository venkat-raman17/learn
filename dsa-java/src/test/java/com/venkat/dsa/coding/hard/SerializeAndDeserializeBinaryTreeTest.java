package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class SerializeAndDeserializeBinaryTreeTest {

    private final SerializeAndDeserializeBinaryTree codec = new SerializeAndDeserializeBinaryTree();

    private boolean isSameTree(SerializeAndDeserializeBinaryTree.TreeNode a,
                                SerializeAndDeserializeBinaryTree.TreeNode b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.val == b.val && isSameTree(a.left, b.left) && isSameTree(a.right, b.right);
    }

    @Test
    public void testExample1RoundTrip() {
        // [1,2,3,null,null,4,5]
        SerializeAndDeserializeBinaryTree.TreeNode root =
            new SerializeAndDeserializeBinaryTree.TreeNode(1,
                new SerializeAndDeserializeBinaryTree.TreeNode(2),
                new SerializeAndDeserializeBinaryTree.TreeNode(3,
                    new SerializeAndDeserializeBinaryTree.TreeNode(4),
                    new SerializeAndDeserializeBinaryTree.TreeNode(5)));

        String encoded = codec.serialize(root);
        SerializeAndDeserializeBinaryTree.TreeNode decoded = codec.deserialize(encoded);

        assertTrue(isSameTree(root, decoded));
    }

    @Test
    public void testNullRoot() {
        String encoded = codec.serialize(null);
        SerializeAndDeserializeBinaryTree.TreeNode decoded = codec.deserialize(encoded);
        assertNull(decoded);
    }

    @Test
    public void testSingleNode() {
        SerializeAndDeserializeBinaryTree.TreeNode root =
            new SerializeAndDeserializeBinaryTree.TreeNode(42);

        String encoded = codec.serialize(root);
        SerializeAndDeserializeBinaryTree.TreeNode decoded = codec.deserialize(encoded);

        assertNotNull(decoded);
        assertEquals(42, decoded.val);
        assertNull(decoded.left);
        assertNull(decoded.right);
    }
}
