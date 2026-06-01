package com.venkat.dsa.trees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link BinarySearchTree}.
 *
 * <p>Tree shape used in most tests (insert order: 5, 3, 7, 1, 4, 6, 8):
 * <pre>
 *         5
 *        / \
 *       3   7
 *      / \ / \
 *     1  4 6  8
 * </pre>
 * Expected inorder: [1, 3, 4, 5, 6, 7, 8]
 */
class BinarySearchTreeTest {

    private BinarySearchTree<Integer> bst;

    @BeforeEach
    void setUp() {
        bst = new BinarySearchTree<>();
    }

    // ── isEmpty / size ────────────────────────────────────────────────────────

    @Test
    void newTreeIsEmpty() {
        assertTrue(bst.isEmpty());
        assertEquals(0, bst.size());
    }

    @Test
    void sizeIncreasesWithEachUniqueInsert() {
        bst.insert(10);
        assertEquals(1, bst.size());
        assertFalse(bst.isEmpty());

        bst.insert(5);
        bst.insert(15);
        assertEquals(3, bst.size());
    }

    // ── insert ────────────────────────────────────────────────────────────────

    @Test
    void insertNullThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> bst.insert(null));
    }

    @Test
    void insertDuplicateIsIgnoredAndSizeUnchanged() {
        bst.insert(5);
        bst.insert(5);
        assertEquals(1, bst.size());
    }

    @Test
    void insertMultipleDuplicatesLeaveSizeUnchanged() {
        bst.insert(3);
        bst.insert(3);
        bst.insert(3);
        assertEquals(1, bst.size());
    }

    // ── contains ─────────────────────────────────────────────────────────────

    @Test
    void containsNullThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> bst.contains(null));
    }

    @Test
    void containsReturnsFalseOnEmptyTree() {
        assertFalse(bst.contains(1));
    }

    @Test
    void containsReturnsTrueForInsertedValues() {
        insertStandardTree();
        assertTrue(bst.contains(5));  // root
        assertTrue(bst.contains(3));  // left child
        assertTrue(bst.contains(7));  // right child
        assertTrue(bst.contains(1));  // left leaf
        assertTrue(bst.contains(8));  // right leaf
    }

    @Test
    void containsReturnsFalseForAbsentValues() {
        insertStandardTree();
        assertFalse(bst.contains(0));
        assertFalse(bst.contains(2));
        assertFalse(bst.contains(9));
        assertFalse(bst.contains(100));
    }

    // ── inorder ───────────────────────────────────────────────────────────────

    @Test
    void inorderOnEmptyTreeReturnsEmptyList() {
        assertTrue(bst.inorder().isEmpty());
    }

    @Test
    void inorderOnSingleElementReturnsSingletonList() {
        bst.insert(42);
        assertEquals(List.of(42), bst.inorder());
    }

    @Test
    void inorderReturnsSortedAscendingOrder() {
        insertStandardTree();
        assertEquals(List.of(1, 3, 4, 5, 6, 7, 8), bst.inorder());
    }

    @Test
    void inorderAfterInsertingInReverseOrderIsStillSorted() {
        // Insert in descending order — creates a left-skewed tree
        for (int v : new int[]{9, 7, 5, 3, 1}) {
            bst.insert(v);
        }
        assertEquals(List.of(1, 3, 5, 7, 9), bst.inorder());
    }

    @Test
    void inorderAfterInsertingInAscendingOrderIsStillSorted() {
        // Insert in ascending order — creates a right-skewed tree
        for (int v : new int[]{1, 3, 5, 7, 9}) {
            bst.insert(v);
        }
        assertEquals(List.of(1, 3, 5, 7, 9), bst.inorder());
    }

    // ── min / max ─────────────────────────────────────────────────────────────

    @Test
    void minOnEmptyTreeThrowsNoSuchElement() {
        assertThrows(NoSuchElementException.class, () -> bst.min());
    }

    @Test
    void maxOnEmptyTreeThrowsNoSuchElement() {
        assertThrows(NoSuchElementException.class, () -> bst.max());
    }

    @Test
    void minReturnsSmallestValue() {
        insertStandardTree();
        assertEquals(1, bst.min());
    }

    @Test
    void maxReturnsLargestValue() {
        insertStandardTree();
        assertEquals(8, bst.max());
    }

    @Test
    void minAndMaxOnSingleElementAreEqual() {
        bst.insert(42);
        assertEquals(42, bst.min());
        assertEquals(42, bst.max());
    }

    // ── delete: leaf node ─────────────────────────────────────────────────────

    @Test
    void deleteNullThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> bst.delete(null));
    }

    @Test
    void deleteOnEmptyTreeDoesNothing() {
        assertDoesNotThrow(() -> bst.delete(99));
        assertEquals(0, bst.size());
    }

    @Test
    void deleteAbsentValueDoesNothing() {
        insertStandardTree();
        int originalSize = bst.size();
        bst.delete(99);
        assertEquals(originalSize, bst.size());
        assertEquals(List.of(1, 3, 4, 5, 6, 7, 8), bst.inorder());
    }

    @Test
    void deleteLeafNodeLeftLeaf() {
        // Delete leaf 1 (left leaf under 3)
        insertStandardTree();
        bst.delete(1);
        assertFalse(bst.contains(1));
        assertEquals(6, bst.size());
        assertEquals(List.of(3, 4, 5, 6, 7, 8), bst.inorder());
    }

    @Test
    void deleteLeafNodeRightLeaf() {
        // Delete leaf 8 (right leaf under 7)
        insertStandardTree();
        bst.delete(8);
        assertFalse(bst.contains(8));
        assertEquals(6, bst.size());
        assertEquals(List.of(1, 3, 4, 5, 6, 7), bst.inorder());
    }

    @Test
    void deleteOnlyNodeLeavesTreeEmpty() {
        bst.insert(5);
        bst.delete(5);
        assertTrue(bst.isEmpty());
        assertTrue(bst.inorder().isEmpty());
    }

    // ── delete: one child ─────────────────────────────────────────────────────

    @Test
    void deleteNodeWithOnlyRightChild() {
        // Build: root=5, right=10, right.right=15  (10 has only a right child)
        bst.insert(5);
        bst.insert(10);
        bst.insert(15);
        bst.delete(10);
        assertFalse(bst.contains(10));
        assertTrue(bst.contains(15));
        assertEquals(2, bst.size());
        assertEquals(List.of(5, 15), bst.inorder());
    }

    @Test
    void deleteNodeWithOnlyLeftChild() {
        // Build: root=10, left=5, left.left=1  (5 has only a left child)
        bst.insert(10);
        bst.insert(5);
        bst.insert(1);
        bst.delete(5);
        assertFalse(bst.contains(5));
        assertTrue(bst.contains(1));
        assertEquals(2, bst.size());
        assertEquals(List.of(1, 10), bst.inorder());
    }

    // ── delete: two children ──────────────────────────────────────────────────

    @Test
    void deleteNodeWithTwoChildrenUsesInorderSuccessor() {
        // Delete node 3 (has children 1 and 4). Inorder successor of 3 is 4.
        // After delete: 4 takes 3's place, subtree becomes 4 with left child 1.
        insertStandardTree();
        bst.delete(3);
        assertFalse(bst.contains(3));
        assertTrue(bst.contains(1));
        assertTrue(bst.contains(4));
        assertEquals(6, bst.size());
        assertEquals(List.of(1, 4, 5, 6, 7, 8), bst.inorder());
    }

    @Test
    void deleteRootWithTwoChildren() {
        // Delete root 5. Inorder successor is 6.
        insertStandardTree();
        bst.delete(5);
        assertFalse(bst.contains(5));
        assertTrue(bst.contains(6));
        assertEquals(6, bst.size());
        assertEquals(List.of(1, 3, 4, 6, 7, 8), bst.inorder());
    }

    @Test
    void deleteRightChildWithTwoChildren() {
        // Delete node 7 (children: 6 and 8). Inorder successor is 8.
        insertStandardTree();
        bst.delete(7);
        assertFalse(bst.contains(7));
        assertTrue(bst.contains(6));
        assertTrue(bst.contains(8));
        assertEquals(6, bst.size());
        assertEquals(List.of(1, 3, 4, 5, 6, 8), bst.inorder());
    }

    // ── delete: successive deletions ──────────────────────────────────────────

    @Test
    void deleteAllNodesOneByOneResultsInEmptyTree() {
        insertStandardTree();
        for (int v : new int[]{1, 3, 4, 5, 6, 7, 8}) {
            bst.delete(v);
        }
        assertTrue(bst.isEmpty());
        assertEquals(0, bst.size());
    }

    @Test
    void deleteAndReinsertMaintainsCorrectStructure() {
        insertStandardTree();
        bst.delete(5); // root, two children
        bst.insert(5);
        assertTrue(bst.contains(5));
        assertEquals(7, bst.size());
        assertEquals(List.of(1, 3, 4, 5, 6, 7, 8), bst.inorder());
    }

    // ── boundary / type tests ─────────────────────────────────────────────────

    @Test
    void worksWithStringType() {
        BinarySearchTree<String> strBst = new BinarySearchTree<>();
        strBst.insert("banana");
        strBst.insert("apple");
        strBst.insert("cherry");
        assertEquals(List.of("apple", "banana", "cherry"), strBst.inorder());
        assertEquals("apple", strBst.min());
        assertEquals("cherry", strBst.max());
    }

    @Test
    void worksWithNegativeIntegers() {
        for (int v : new int[]{-3, -1, -5, -2, -4}) {
            bst.insert(v);
        }
        assertEquals(List.of(-5, -4, -3, -2, -1), bst.inorder());
        assertEquals(-5, bst.min());
        assertEquals(-1, bst.max());
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    /**
     * Inserts values 5, 3, 7, 1, 4, 6, 8 to produce the balanced tree used by
     * most tests.
     */
    private void insertStandardTree() {
        for (int v : new int[]{5, 3, 7, 1, 4, 6, 8}) {
            bst.insert(v);
        }
    }
}
