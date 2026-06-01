package com.venkat.dsa.trees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link AVLTree}.
 *
 * <p>Every public operation is exercised, including edge cases for empty trees,
 * single-element trees, duplicates, ascending/descending insertion sequences,
 * deletions that trigger all four rotation types, and null-argument validation.</p>
 */
class AVLTreeTest {

    private AVLTree<Integer> tree;

    @BeforeEach
    void setUp() {
        tree = new AVLTree<>();
    }

    // ------------------------------------------------------------------ isEmpty / size on fresh tree

    @Test
    void emptyTree_isEmptyTrue_sizeZero() {
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
    }

    @Test
    void emptyTree_heightIsZero() {
        assertEquals(0, tree.height());
    }

    @Test
    void emptyTree_isBalanced() {
        assertTrue(tree.isBalanced());
    }

    @Test
    void emptyTree_inorderIsEmpty() {
        assertTrue(tree.inorder().isEmpty());
    }

    @Test
    void emptyTree_containsReturnsFalse() {
        assertFalse(tree.contains(42));
    }

    // ------------------------------------------------------------------ Single element

    @Test
    void singleInsert_sizeOne_notEmpty() {
        tree.insert(10);
        assertFalse(tree.isEmpty());
        assertEquals(1, tree.size());
    }

    @Test
    void singleInsert_heightOne() {
        tree.insert(10);
        assertEquals(1, tree.height());
    }

    @Test
    void singleInsert_containsValue() {
        tree.insert(10);
        assertTrue(tree.contains(10));
    }

    @Test
    void singleInsert_doesNotContainOther() {
        tree.insert(10);
        assertFalse(tree.contains(99));
    }

    @Test
    void singleInsert_inorderHasSingleElement() {
        tree.insert(7);
        assertEquals(List.of(7), tree.inorder());
    }

    @Test
    void singleInsert_isBalanced() {
        tree.insert(5);
        assertTrue(tree.isBalanced());
    }

    // ------------------------------------------------------------------ Duplicate handling

    @Test
    void insertDuplicate_sizeUnchanged() {
        tree.insert(10);
        tree.insert(10);
        assertEquals(1, tree.size());
    }

    @Test
    void insertDuplicate_inorderHasOneEntry() {
        tree.insert(5);
        tree.insert(5);
        assertEquals(List.of(5), tree.inorder());
    }

    // ------------------------------------------------------------------ Ascending insertion stays balanced

    @Test
    void insertAscending_isBalanced() {
        for (int i = 1; i <= 16; i++) {
            tree.insert(i);
        }
        assertTrue(tree.isBalanced());
    }

    @Test
    void insertAscending_heightApproximatelyLogN() {
        // For n=16, perfect AVL height is 5 (floor(log2(16))+1). AVL guarantees <= 1.44*log2(n).
        // 1.44 * log2(16) = 1.44 * 4 = 5.76, so height must be <= 6 for n=16.
        for (int i = 1; i <= 16; i++) {
            tree.insert(i);
        }
        int h = tree.height();
        assertTrue(h <= 6, "Expected height <= 6 for 16 ascending inserts, got " + h);
    }

    @Test
    void insertAscending_inorderIsSorted() {
        int n = 10;
        for (int i = 1; i <= n; i++) {
            tree.insert(i);
        }
        List<Integer> sorted = tree.inorder();
        assertEquals(n, sorted.size());
        for (int i = 0; i < sorted.size() - 1; i++) {
            assertTrue(sorted.get(i) < sorted.get(i + 1),
                    "Expected ascending order at index " + i);
        }
    }

    @Test
    void insertDescending_isBalanced() {
        for (int i = 16; i >= 1; i--) {
            tree.insert(i);
        }
        assertTrue(tree.isBalanced());
    }

    @Test
    void insertDescending_inorderIsSorted() {
        for (int i = 10; i >= 1; i--) {
            tree.insert(i);
        }
        List<Integer> sorted = tree.inorder();
        assertEquals(10, sorted.size());
        for (int i = 0; i < sorted.size() - 1; i++) {
            assertTrue(sorted.get(i) < sorted.get(i + 1));
        }
    }

    // ------------------------------------------------------------------ All four rotation types

    /**
     * LL rotation: insert 3, 2, 1 → left-left imbalance at node 3, triggers right rotation.
     * Expected in-order: [1, 2, 3], root is 2 (height = 2).
     */
    @Test
    void insertLL_triggersRightRotation_balancedAndSorted() {
        tree.insert(3);
        tree.insert(2);
        tree.insert(1);
        assertTrue(tree.isBalanced());
        assertEquals(List.of(1, 2, 3), tree.inorder());
        assertEquals(2, tree.height());
    }

    /**
     * RR rotation: insert 1, 2, 3 → right-right imbalance at node 1, triggers left rotation.
     * Expected in-order: [1, 2, 3], root is 2 (height = 2).
     */
    @Test
    void insertRR_triggersLeftRotation_balancedAndSorted() {
        tree.insert(1);
        tree.insert(2);
        tree.insert(3);
        assertTrue(tree.isBalanced());
        assertEquals(List.of(1, 2, 3), tree.inorder());
        assertEquals(2, tree.height());
    }

    /**
     * LR rotation: insert 3, 1, 2 → left-right imbalance, triggers left-right double rotation.
     */
    @Test
    void insertLR_triggersDoubleRotation_balancedAndSorted() {
        tree.insert(3);
        tree.insert(1);
        tree.insert(2);
        assertTrue(tree.isBalanced());
        assertEquals(List.of(1, 2, 3), tree.inorder());
        assertEquals(2, tree.height());
    }

    /**
     * RL rotation: insert 1, 3, 2 → right-left imbalance, triggers right-left double rotation.
     */
    @Test
    void insertRL_triggersDoubleRotation_balancedAndSorted() {
        tree.insert(1);
        tree.insert(3);
        tree.insert(2);
        assertTrue(tree.isBalanced());
        assertEquals(List.of(1, 2, 3), tree.inorder());
        assertEquals(2, tree.height());
    }

    // ------------------------------------------------------------------ Delete edge cases

    @Test
    void deleteFromEmpty_noException() {
        assertDoesNotThrow(() -> tree.delete(5));
        assertEquals(0, tree.size());
    }

    @Test
    void deleteNonExistentValue_sizeUnchanged() {
        tree.insert(10);
        tree.delete(99);
        assertEquals(1, tree.size());
    }

    @Test
    void deleteSingleElement_treeBecomesEmpty() {
        tree.insert(10);
        tree.delete(10);
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
        assertEquals(0, tree.height());
    }

    @Test
    void deleteLeafNode_balancedAndSorted() {
        tree.insert(2);
        tree.insert(1);
        tree.insert(3);
        tree.delete(3); // delete leaf
        assertTrue(tree.isBalanced());
        assertEquals(List.of(1, 2), tree.inorder());
    }

    @Test
    void deleteNodeWithOneChild_balancedAndSorted() {
        // Build: 2 with left=1, right=3; right child 3 has right child 4
        tree.insert(2);
        tree.insert(1);
        tree.insert(3);
        tree.insert(4);
        // Delete 3 (one child: 4)
        tree.delete(3);
        assertTrue(tree.isBalanced());
        assertEquals(List.of(1, 2, 4), tree.inorder());
    }

    @Test
    void deleteNodeWithTwoChildren_balancedAndSorted() {
        // Insert 4, 2, 6, 1, 3, 5, 7 → full BST of height 3
        for (int v : new int[]{4, 2, 6, 1, 3, 5, 7}) tree.insert(v);
        tree.delete(2); // two-child deletion, replaced by in-order successor (3)
        assertTrue(tree.isBalanced());
        assertEquals(List.of(1, 3, 4, 5, 6, 7), tree.inorder());
    }

    @Test
    void deleteRoot_balancedAndSorted() {
        tree.insert(2);
        tree.insert(1);
        tree.insert(3);
        tree.delete(2);
        assertTrue(tree.isBalanced());
        assertEquals(List.of(1, 3), tree.inorder());
    }

    @Test
    void deleteKeepsBalance_ascendingSequenceThenDelete() {
        for (int i = 1; i <= 15; i++) tree.insert(i);
        // Delete several nodes and verify balance is maintained throughout
        for (int i = 1; i <= 15; i += 3) {
            tree.delete(i);
            assertTrue(tree.isBalanced(), "Unbalanced after deleting " + i);
        }
    }

    @Test
    void deleteAll_treeIsEmpty() {
        int[] values = {5, 3, 7, 1, 4, 6, 8};
        for (int v : values) tree.insert(v);
        for (int v : values) tree.delete(v);
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
        assertEquals(0, tree.height());
    }

    // ------------------------------------------------------------------ Mixed operations

    @Test
    void insertAndDeleteInterleaved_inorderAlwaysSorted() {
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(3);
        tree.delete(5);
        tree.insert(7);
        tree.insert(12);
        tree.delete(15);

        List<Integer> result = tree.inorder();
        // Expected: 3, 7, 10, 12
        assertEquals(List.of(3, 7, 10, 12), result);
        assertTrue(tree.isBalanced());
    }

    @Test
    void contains_afterInsertAndDelete() {
        tree.insert(1);
        tree.insert(2);
        tree.insert(3);
        tree.delete(2);
        assertTrue(tree.contains(1));
        assertFalse(tree.contains(2));
        assertTrue(tree.contains(3));
    }

    // ------------------------------------------------------------------ Generic type: String

    @Test
    void stringTree_insertAndInorder() {
        AVLTree<String> strTree = new AVLTree<>();
        strTree.insert("banana");
        strTree.insert("apple");
        strTree.insert("cherry");
        strTree.insert("date");
        assertTrue(strTree.isBalanced());
        assertEquals(List.of("apple", "banana", "cherry", "date"), strTree.inorder());
    }

    @Test
    void stringTree_deleteAndContains() {
        AVLTree<String> strTree = new AVLTree<>();
        strTree.insert("b");
        strTree.insert("a");
        strTree.insert("c");
        strTree.delete("a");
        assertFalse(strTree.contains("a"));
        assertTrue(strTree.contains("b"));
        assertTrue(strTree.isBalanced());
    }

    // ------------------------------------------------------------------ Null argument validation

    @Test
    void insertNull_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> tree.insert(null));
    }

    @Test
    void deleteNull_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> tree.delete(null));
    }

    @Test
    void containsNull_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> tree.contains(null));
    }

    // ------------------------------------------------------------------ Size tracking

    @Test
    void sizeTracksInsertsAndDeletes() {
        assertEquals(0, tree.size());
        tree.insert(1);
        assertEquals(1, tree.size());
        tree.insert(2);
        tree.insert(3);
        assertEquals(3, tree.size());
        tree.insert(2); // duplicate
        assertEquals(3, tree.size());
        tree.delete(2);
        assertEquals(2, tree.size());
        tree.delete(99); // non-existent
        assertEquals(2, tree.size());
    }

    // ------------------------------------------------------------------ Larger random-ish input

    @Test
    void largeInput_balancedAndCorrectSize() {
        // Insert primes up to 50 in ascending order
        int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47};
        for (int p : primes) tree.insert(p);
        assertEquals(primes.length, tree.size());
        assertTrue(tree.isBalanced());
        // Inorder should equal sorted list (already sorted here)
        List<Integer> sorted = tree.inorder();
        for (int i = 0; i < primes.length; i++) {
            assertEquals(primes[i], sorted.get(i));
        }
    }
}
