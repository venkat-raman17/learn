package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class BinaryTreeRightSideViewTest {

    private final BinaryTreeRightSideView solution = new BinaryTreeRightSideView();

    @Test
    public void testExample1() {
        // [1,2,3,null,5,null,4] -> [1,3,4]
        BinaryTreeRightSideView.TreeNode root = new BinaryTreeRightSideView.TreeNode(1,
            new BinaryTreeRightSideView.TreeNode(2,
                null,
                new BinaryTreeRightSideView.TreeNode(5)),
            new BinaryTreeRightSideView.TreeNode(3,
                null,
                new BinaryTreeRightSideView.TreeNode(4)));

        assertEquals(Arrays.asList(1, 3, 4), solution.rightSideView(root));
    }

    @Test
    public void testExample2() {
        BinaryTreeRightSideView.TreeNode root = new BinaryTreeRightSideView.TreeNode(1,
            null,
            new BinaryTreeRightSideView.TreeNode(3));

        assertEquals(Arrays.asList(1, 3), solution.rightSideView(root));
    }

    @Test
    public void testNullRoot() {
        assertEquals(Collections.emptyList(), solution.rightSideView(null));
    }
}
