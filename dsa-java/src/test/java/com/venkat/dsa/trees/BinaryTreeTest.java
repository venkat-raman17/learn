package com.venkat.dsa.trees;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BinaryTree}.
 *
 * <p>Tree used in most tests (values are integers):
 * <pre>
 *         1
 *        / \
 *       2   3
 *      / \
 *     4   5
 * </pre>
 * Pre-order  : 1 2 4 5 3
 * In-order   : 4 2 5 1 3
 * Post-order : 4 5 2 3 1
 * Level-order: 1 2 3 4 5
 * Height     : 2  (root=0, children=1, grandchildren=2)
 * Size       : 5
 */
class BinaryTreeTest {

    // -----------------------------------------------------------------------
    // Helper — build the standard 5-node test tree
    // -----------------------------------------------------------------------

    /**
     *         1
     *        / \
     *       2   3
     *      / \
     *     4   5
     */
    private BinaryTree<Integer> buildStandardTree() {
        BinaryTree.Node<Integer> n4 = new BinaryTree.Node<>(4);
        BinaryTree.Node<Integer> n5 = new BinaryTree.Node<>(5);
        BinaryTree.Node<Integer> n2 = new BinaryTree.Node<>(2, n4, n5);
        BinaryTree.Node<Integer> n3 = new BinaryTree.Node<>(3);
        BinaryTree.Node<Integer> n1 = new BinaryTree.Node<>(1, n2, n3);
        return new BinaryTree<>(n1);
    }

    // -----------------------------------------------------------------------
    // Empty-tree edge cases
    // -----------------------------------------------------------------------

    @Test
    void emptyTree_preorderReturnsEmptyList() {
        BinaryTree<Integer> tree = new BinaryTree<>(null);
        assertTrue(tree.preorder().isEmpty());
    }

    @Test
    void emptyTree_inorderReturnsEmptyList() {
        BinaryTree<Integer> tree = new BinaryTree<>(null);
        assertTrue(tree.inorder().isEmpty());
    }

    @Test
    void emptyTree_postorderReturnsEmptyList() {
        BinaryTree<Integer> tree = new BinaryTree<>(null);
        assertTrue(tree.postorder().isEmpty());
    }

    @Test
    void emptyTree_levelOrderReturnsEmptyList() {
        BinaryTree<Integer> tree = new BinaryTree<>(null);
        assertTrue(tree.levelOrder().isEmpty());
    }

    @Test
    void emptyTree_heightIsMinusOne() {
        BinaryTree<Integer> tree = new BinaryTree<>(null);
        assertEquals(-1, tree.height());
    }

    @Test
    void emptyTree_sizeIsZero() {
        BinaryTree<Integer> tree = new BinaryTree<>(null);
        assertEquals(0, tree.size());
    }

    // -----------------------------------------------------------------------
    // Single-node tree
    // -----------------------------------------------------------------------

    @Test
    void singleNode_preorderContainsOnlyRoot() {
        BinaryTree<String> tree = new BinaryTree<>(new BinaryTree.Node<>("hello"));
        assertEquals(List.of("hello"), tree.preorder());
    }

    @Test
    void singleNode_inorderContainsOnlyRoot() {
        BinaryTree<String> tree = new BinaryTree<>(new BinaryTree.Node<>("hello"));
        assertEquals(List.of("hello"), tree.inorder());
    }

    @Test
    void singleNode_postorderContainsOnlyRoot() {
        BinaryTree<String> tree = new BinaryTree<>(new BinaryTree.Node<>("hello"));
        assertEquals(List.of("hello"), tree.postorder());
    }

    @Test
    void singleNode_levelOrderContainsOnlyRoot() {
        BinaryTree<String> tree = new BinaryTree<>(new BinaryTree.Node<>("hello"));
        assertEquals(List.of("hello"), tree.levelOrder());
    }

    @Test
    void singleNode_heightIsZero() {
        BinaryTree<Integer> tree = new BinaryTree<>(new BinaryTree.Node<>(42));
        assertEquals(0, tree.height());
    }

    @Test
    void singleNode_sizeIsOne() {
        BinaryTree<Integer> tree = new BinaryTree<>(new BinaryTree.Node<>(42));
        assertEquals(1, tree.size());
    }

    // -----------------------------------------------------------------------
    // Standard 5-node tree — traversal order
    // -----------------------------------------------------------------------

    @Test
    void standardTree_preorderIsRootLeftRight() {
        BinaryTree<Integer> tree = buildStandardTree();
        // Expected: 1, 2, 4, 5, 3
        assertEquals(List.of(1, 2, 4, 5, 3), tree.preorder());
    }

    @Test
    void standardTree_inorderIsLeftRootRight() {
        BinaryTree<Integer> tree = buildStandardTree();
        // Expected: 4, 2, 5, 1, 3
        assertEquals(List.of(4, 2, 5, 1, 3), tree.inorder());
    }

    @Test
    void standardTree_postorderIsLeftRightRoot() {
        BinaryTree<Integer> tree = buildStandardTree();
        // Expected: 4, 5, 2, 3, 1
        assertEquals(List.of(4, 5, 2, 3, 1), tree.postorder());
    }

    @Test
    void standardTree_levelOrderIsBreadthFirst() {
        BinaryTree<Integer> tree = buildStandardTree();
        // Expected: 1, 2, 3, 4, 5
        assertEquals(List.of(1, 2, 3, 4, 5), tree.levelOrder());
    }

    // -----------------------------------------------------------------------
    // Standard 5-node tree — structural properties
    // -----------------------------------------------------------------------

    @Test
    void standardTree_heightIsTwo() {
        BinaryTree<Integer> tree = buildStandardTree();
        assertEquals(2, tree.height());
    }

    @Test
    void standardTree_sizeIsFive() {
        BinaryTree<Integer> tree = buildStandardTree();
        assertEquals(5, tree.size());
    }

    // -----------------------------------------------------------------------
    // Traversal result is independent list (mutation does not corrupt tree state)
    // -----------------------------------------------------------------------

    @Test
    void preorder_returnedListIsMutable() {
        BinaryTree<Integer> tree = buildStandardTree();
        List<Integer> list = tree.preorder();
        list.clear(); // must not throw
        // second call still returns correct result
        assertEquals(List.of(1, 2, 4, 5, 3), tree.preorder());
    }

    // -----------------------------------------------------------------------
    // Right-skewed tree (worst-case height == n-1)
    // -----------------------------------------------------------------------

    @Test
    void rightSkewedTree_heightEqualsNMinusOne() {
        // 1 -> 2 -> 3 -> 4  (all right children)
        BinaryTree.Node<Integer> n4 = new BinaryTree.Node<>(4);
        BinaryTree.Node<Integer> n3 = new BinaryTree.Node<>(3, null, n4);
        BinaryTree.Node<Integer> n2 = new BinaryTree.Node<>(2, null, n3);
        BinaryTree.Node<Integer> n1 = new BinaryTree.Node<>(1, null, n2);
        BinaryTree<Integer> tree = new BinaryTree<>(n1);

        assertEquals(3, tree.height()); // 4 nodes, height = 3
        assertEquals(4, tree.size());
    }

    @Test
    void rightSkewedTree_inorderMatchesInsertionOrder() {
        BinaryTree.Node<Integer> n4 = new BinaryTree.Node<>(4);
        BinaryTree.Node<Integer> n3 = new BinaryTree.Node<>(3, null, n4);
        BinaryTree.Node<Integer> n2 = new BinaryTree.Node<>(2, null, n3);
        BinaryTree.Node<Integer> n1 = new BinaryTree.Node<>(1, null, n2);
        BinaryTree<Integer> tree = new BinaryTree<>(n1);

        // In-order on a right-skewed tree visits nodes in insertion order
        assertEquals(List.of(1, 2, 3, 4), tree.inorder());
    }

    @Test
    void leftSkewedTree_postorderVisitsRootLast() {
        // 4 <- 3 <- 2 <- 1  (all left children)
        BinaryTree.Node<Integer> n4 = new BinaryTree.Node<>(4);
        BinaryTree.Node<Integer> n3 = new BinaryTree.Node<>(3, n4, null);
        BinaryTree.Node<Integer> n2 = new BinaryTree.Node<>(2, n3, null);
        BinaryTree.Node<Integer> n1 = new BinaryTree.Node<>(1, n2, null);
        BinaryTree<Integer> tree = new BinaryTree<>(n1);

        List<Integer> post = tree.postorder();
        assertEquals(4, post.size());
        assertEquals(1, post.get(post.size() - 1)); // root is last in post-order
        assertEquals(List.of(4, 3, 2, 1), post);
    }

    // -----------------------------------------------------------------------
    // Duplicate values — tree must not de-duplicate
    // -----------------------------------------------------------------------

    @Test
    void duplicateValues_allAppearInTraversal() {
        //     5
        //    / \
        //   5   5
        BinaryTree.Node<Integer> left  = new BinaryTree.Node<>(5);
        BinaryTree.Node<Integer> right = new BinaryTree.Node<>(5);
        BinaryTree.Node<Integer> root  = new BinaryTree.Node<>(5, left, right);
        BinaryTree<Integer> tree = new BinaryTree<>(root);

        assertEquals(List.of(5, 5, 5), tree.preorder());
        assertEquals(List.of(5, 5, 5), tree.inorder());
        assertEquals(List.of(5, 5, 5), tree.postorder());
        assertEquals(List.of(5, 5, 5), tree.levelOrder());
        assertEquals(3, tree.size());
        assertEquals(1, tree.height());
    }

    // -----------------------------------------------------------------------
    // Generic type — works with non-Integer type (String)
    // -----------------------------------------------------------------------

    @Test
    void stringTree_traversalOrderIsCorrect() {
        //      "b"
        //     /   \
        //   "a"   "c"
        BinaryTree.Node<String> left  = new BinaryTree.Node<>("a");
        BinaryTree.Node<String> right = new BinaryTree.Node<>("c");
        BinaryTree.Node<String> root  = new BinaryTree.Node<>("b", left, right);
        BinaryTree<String> tree = new BinaryTree<>(root);

        assertEquals(List.of("b", "a", "c"), tree.preorder());
        assertEquals(List.of("a", "b", "c"), tree.inorder());
        assertEquals(List.of("a", "c", "b"), tree.postorder());
        assertEquals(List.of("b", "a", "c"), tree.levelOrder());
    }

    // -----------------------------------------------------------------------
    // Node constructor — three-arg form
    // -----------------------------------------------------------------------

    @Test
    void nodeThreeArgConstructor_setsFieldsCorrectly() {
        BinaryTree.Node<Integer> left  = new BinaryTree.Node<>(10);
        BinaryTree.Node<Integer> right = new BinaryTree.Node<>(20);
        BinaryTree.Node<Integer> node  = new BinaryTree.Node<>(5, left, right);

        assertEquals(5,  node.value);
        assertEquals(10, node.left.value);
        assertEquals(20, node.right.value);
    }

    // -----------------------------------------------------------------------
    // Level-order visits all levels before going deeper
    // -----------------------------------------------------------------------

    @Test
    void levelOrder_completeThreeLevelTree() {
        //         1
        //       /   \
        //      2     3
        //     / \   / \
        //    4   5 6   7
        BinaryTree.Node<Integer> n4 = new BinaryTree.Node<>(4);
        BinaryTree.Node<Integer> n5 = new BinaryTree.Node<>(5);
        BinaryTree.Node<Integer> n6 = new BinaryTree.Node<>(6);
        BinaryTree.Node<Integer> n7 = new BinaryTree.Node<>(7);
        BinaryTree.Node<Integer> n2 = new BinaryTree.Node<>(2, n4, n5);
        BinaryTree.Node<Integer> n3 = new BinaryTree.Node<>(3, n6, n7);
        BinaryTree.Node<Integer> n1 = new BinaryTree.Node<>(1, n2, n3);
        BinaryTree<Integer> tree = new BinaryTree<>(n1);

        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7), tree.levelOrder());
        assertEquals(2, tree.height());
        assertEquals(7, tree.size());
    }
}
