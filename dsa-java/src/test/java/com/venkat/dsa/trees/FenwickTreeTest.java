package com.venkat.dsa.trees;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link FenwickTree}.
 * All indices are 0-based (the public API). Expected values are computed by hand.
 */
class FenwickTreeTest {

    // ── construction ─────────────────────────────────────────────────────────

    @Test
    void constructor_size_createsZeroTree() {
        FenwickTree ft = new FenwickTree(5);
        assertEquals(5, ft.size());
        assertEquals(0L, ft.prefixSum(0));
        assertEquals(0L, ft.prefixSum(4));
        assertEquals(0L, ft.rangeSum(0, 4));
    }

    @Test
    void constructor_size_singleElement() {
        FenwickTree ft = new FenwickTree(1);
        assertEquals(1, ft.size());
        assertEquals(0L, ft.prefixSum(0));
    }

    @Test
    void constructor_size_throwsOnZero() {
        assertThrows(IllegalArgumentException.class, () -> new FenwickTree(0));
    }

    @Test
    void constructor_size_throwsOnNegative() {
        assertThrows(IllegalArgumentException.class, () -> new FenwickTree(-3));
    }

    @Test
    void constructor_array_buildsCorrectSums() {
        // values: [1, 2, 3, 4, 5]
        // prefix sums: [1, 3, 6, 10, 15]
        int[] values = {1, 2, 3, 4, 5};
        FenwickTree ft = new FenwickTree(values);
        assertEquals(5, ft.size());
        assertEquals(1L,  ft.prefixSum(0));
        assertEquals(3L,  ft.prefixSum(1));
        assertEquals(6L,  ft.prefixSum(2));
        assertEquals(10L, ft.prefixSum(3));
        assertEquals(15L, ft.prefixSum(4));
    }

    @Test
    void constructor_array_singleElement() {
        FenwickTree ft = new FenwickTree(new int[]{42});
        assertEquals(1, ft.size());
        assertEquals(42L, ft.prefixSum(0));
    }

    @Test
    void constructor_array_throwsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> new FenwickTree((int[]) null));
    }

    @Test
    void constructor_array_throwsOnEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new FenwickTree(new int[]{}));
    }

    // ── prefixSum ────────────────────────────────────────────────────────────

    @Test
    void prefixSum_allIndices_returnsCorrectRunningSum() {
        // values: [3, 1, 4, 1, 5]
        // prefix: [3, 4, 8, 9, 14]
        FenwickTree ft = new FenwickTree(new int[]{3, 1, 4, 1, 5});
        assertEquals(3L,  ft.prefixSum(0));
        assertEquals(4L,  ft.prefixSum(1));
        assertEquals(8L,  ft.prefixSum(2));
        assertEquals(9L,  ft.prefixSum(3));
        assertEquals(14L, ft.prefixSum(4));
    }

    @Test
    void prefixSum_negativeValues_handledCorrectly() {
        // values: [-2, 3, -1, 4]
        // prefix: [-2, 1, 0, 4]
        FenwickTree ft = new FenwickTree(new int[]{-2, 3, -1, 4});
        assertEquals(-2L, ft.prefixSum(0));
        assertEquals(1L,  ft.prefixSum(1));
        assertEquals(0L,  ft.prefixSum(2));
        assertEquals(4L,  ft.prefixSum(3));
    }

    @Test
    void prefixSum_outOfBounds_throws() {
        FenwickTree ft = new FenwickTree(4);
        assertThrows(IndexOutOfBoundsException.class, () -> ft.prefixSum(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> ft.prefixSum(4));
    }

    // ── rangeSum ─────────────────────────────────────────────────────────────

    @Test
    void rangeSum_fullRange_equalsTotalSum() {
        // values: [1, 2, 3, 4, 5] -> total = 15
        FenwickTree ft = new FenwickTree(new int[]{1, 2, 3, 4, 5});
        assertEquals(15L, ft.rangeSum(0, 4));
    }

    @Test
    void rangeSum_singleElementRange_returnsElementValue() {
        // values: [10, 20, 30]
        FenwickTree ft = new FenwickTree(new int[]{10, 20, 30});
        assertEquals(10L, ft.rangeSum(0, 0));
        assertEquals(20L, ft.rangeSum(1, 1));
        assertEquals(30L, ft.rangeSum(2, 2));
    }

    @Test
    void rangeSum_middleRange_returnsCorrectSum() {
        // values: [1, 2, 3, 4, 5]
        // rangeSum(1,3) = 2+3+4 = 9
        FenwickTree ft = new FenwickTree(new int[]{1, 2, 3, 4, 5});
        assertEquals(9L, ft.rangeSum(1, 3));
    }

    @Test
    void rangeSum_startingFromZero_equalsPrefixSum() {
        FenwickTree ft = new FenwickTree(new int[]{5, 3, 8, 2});
        assertEquals(ft.prefixSum(2), ft.rangeSum(0, 2));
    }

    @Test
    void rangeSum_leftGreaterThanRight_throws() {
        FenwickTree ft = new FenwickTree(new int[]{1, 2, 3});
        assertThrows(IllegalArgumentException.class, () -> ft.rangeSum(2, 1));
    }

    @Test
    void rangeSum_outOfBounds_throws() {
        FenwickTree ft = new FenwickTree(3);
        assertThrows(IndexOutOfBoundsException.class, () -> ft.rangeSum(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> ft.rangeSum(0, 3));
    }

    // ── update ───────────────────────────────────────────────────────────────

    @Test
    void update_singlePoint_affectsAllPrefixSumsAboveIt() {
        // Start with [0,0,0,0,0], add 7 at index 2
        // prefix sums become: [0, 0, 7, 7, 7]
        FenwickTree ft = new FenwickTree(5);
        ft.update(2, 7);
        assertEquals(0L, ft.prefixSum(0));
        assertEquals(0L, ft.prefixSum(1));
        assertEquals(7L, ft.prefixSum(2));
        assertEquals(7L, ft.prefixSum(3));
        assertEquals(7L, ft.prefixSum(4));
    }

    @Test
    void update_negativeDelta_decreasesSum() {
        // Start with [10, 10, 10], subtract 3 at index 1
        // prefix: [10, 17, 27]
        FenwickTree ft = new FenwickTree(new int[]{10, 10, 10});
        ft.update(1, -3);
        assertEquals(10L, ft.prefixSum(0));
        assertEquals(17L, ft.prefixSum(1));
        assertEquals(27L, ft.prefixSum(2));
    }

    @Test
    void update_multipleUpdatesAccumulate() {
        // Start with all zeros, add 1 at index 0 three times -> element 0 = 3
        FenwickTree ft = new FenwickTree(3);
        ft.update(0, 1);
        ft.update(0, 1);
        ft.update(0, 1);
        assertEquals(3L, ft.prefixSum(0));
        assertEquals(3L, ft.prefixSum(2));
    }

    @Test
    void update_lastIndex_onlyLastPrefixSumChanges() {
        // All zeros; add 5 at last index (4) of size-5 tree
        // Only prefixSum(4) changes to 5
        FenwickTree ft = new FenwickTree(5);
        ft.update(4, 5);
        assertEquals(0L, ft.prefixSum(0));
        assertEquals(0L, ft.prefixSum(3));
        assertEquals(5L, ft.prefixSum(4));
    }

    @Test
    void update_firstIndex_allPrefixSumsChange() {
        // All zeros; add 9 at index 0
        // Every prefixSum(i) for i in [0,4] becomes 9
        FenwickTree ft = new FenwickTree(5);
        ft.update(0, 9);
        for (int i = 0; i < 5; i++) {
            assertEquals(9L, ft.prefixSum(i), "prefixSum(" + i + ") should be 9");
        }
    }

    @Test
    void update_outOfBounds_throws() {
        FenwickTree ft = new FenwickTree(3);
        assertThrows(IndexOutOfBoundsException.class, () -> ft.update(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> ft.update(3, 1));
    }

    // ── combined update + query ───────────────────────────────────────────────

    @Test
    void updateThenRangeSum_reflectsChange() {
        // values: [1, 2, 3, 4, 5]  rangeSum(1,3) = 9
        // update index 2 += 10  -> element at 2 becomes 13
        // rangeSum(1,3) = 2 + 13 + 4 = 19
        FenwickTree ft = new FenwickTree(new int[]{1, 2, 3, 4, 5});
        ft.update(2, 10);
        assertEquals(19L, ft.rangeSum(1, 3));
    }

    @Test
    void buildThenUpdateAndQuery_complexSequence() {
        // values: [5, 3, 2, 7, 1]
        // initial prefix: [5, 8, 10, 17, 18]
        FenwickTree ft = new FenwickTree(new int[]{5, 3, 2, 7, 1});
        assertEquals(17L, ft.prefixSum(3));
        assertEquals(10L, ft.rangeSum(2, 4)); // values[2..4] = 2 + 7 + 1

        // update index 0 by +2 -> element 0 becomes 7
        ft.update(0, 2);
        assertEquals(7L,  ft.prefixSum(0));
        assertEquals(20L, ft.prefixSum(4)); // 18 + 2 = 20

        // update index 3 by -4 -> element 3 becomes 3
        ft.update(3, -4); // element 3: 7 -> 3, array now [7, 3, 2, 3, 1]
        assertEquals(15L, ft.prefixSum(3)); // 7 + 3 + 2 + 3
        assertEquals(16L, ft.prefixSum(4)); // + 1
    }

    @Test
    void largeTree_prefixSumBoundary() {
        // Build tree of 1000 elements all set to 1
        // prefixSum(999) should be 1000
        // rangeSum(500, 999) should be 500
        FenwickTree ft = new FenwickTree(1000);
        for (int i = 0; i < 1000; i++) {
            ft.update(i, 1);
        }
        assertEquals(1000L, ft.prefixSum(999));
        assertEquals(500L,  ft.rangeSum(500, 999));
    }

    @Test
    void duplicateValues_handledCorrectly() {
        // values: [4, 4, 4, 4]
        // prefix: [4, 8, 12, 16]
        FenwickTree ft = new FenwickTree(new int[]{4, 4, 4, 4});
        assertEquals(4L,  ft.prefixSum(0));
        assertEquals(8L,  ft.prefixSum(1));
        assertEquals(12L, ft.prefixSum(2));
        assertEquals(16L, ft.prefixSum(3));
        assertEquals(8L,  ft.rangeSum(1, 2));
    }

    @Test
    void zeroDeltaUpdate_doesNotChangeAnything() {
        FenwickTree ft = new FenwickTree(new int[]{1, 2, 3});
        long before = ft.prefixSum(2);
        ft.update(1, 0);
        assertEquals(before, ft.prefixSum(2));
    }
}
