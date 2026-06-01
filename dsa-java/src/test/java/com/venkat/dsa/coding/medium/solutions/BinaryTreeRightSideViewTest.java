package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.venkat.dsa.coding.medium.solutions.BinaryTreeRightSideView.TreeNode;
import java.util.List;

class BinaryTreeRightSideViewTest {

    private final BinaryTreeRightSideView sol = new BinaryTreeRightSideView();

    // Example 1: [1,2,3,null,5,null,4] → [1,3,4]
    @Test
    void example1() {
        //     1
        //    / \
        //   2   3
        //    \   \
        //     5   4
        TreeNode root = new TreeNode(1,
                new TreeNode(2, null, new TreeNode(5)),
                new TreeNode(3, null, new TreeNode(4)));
        assertEquals(List.of(1, 3, 4), sol.rightSideView(root));
    }

    // Example 2: [1,null,3] → [1,3]
    @Test
    void example2() {
        TreeNode root = new TreeNode(1, null, new TreeNode(3));
        assertEquals(List.of(1, 3), sol.rightSideView(root));
    }

    // Empty tree → []
    @Test
    void emptyTree() {
        assertTrue(sol.rightSideView(null).isEmpty());
    }

    // Single node → [1]
    @Test
    void singleNode() {
        assertEquals(List.of(5), sol.rightSideView(new TreeNode(5)));
    }

    // Left-only tree: right view is every node
    @Test
    void leftOnlyTree() {
        TreeNode root = new TreeNode(1, new TreeNode(2, new TreeNode(3), null), null);
        // At each level, only the left node exists — it IS the rightmost visible
        assertEquals(List.of(1, 2, 3), sol.rightSideView(root));
    }

    // Level 3 has only a left child — it still shows in the right view
    @Test
    void deepLeftChildVisible() {
        //     1
        //    / \
        //   2   3
        //  /
        // 4
        TreeNode root = new TreeNode(1,
                new TreeNode(2, new TreeNode(4), null),
                new TreeNode(3));
        // Level 0 → 1, Level 1 → 3, Level 2 → 4 (only node at that depth)
        assertEquals(List.of(1, 3, 4), sol.rightSideView(root));
    }
}
