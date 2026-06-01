package com.venkat.dsa.trees;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link SegmentTree}.
 *
 * <p>All inputs are deterministic so expected values can be verified by hand.
 * Tests cover: construction, full-range sum, subrange queries, single-element
 * queries, point updates, boundary indices, and all documented error conditions.
 */
class SegmentTreeTest {

    // -------------------------------------------------------------------------
    // Helper: a small canonical array used across many tests
    // arr = [1, 3, 5, 7, 9, 11]
    // prefix sums: 1, 4, 9, 16, 25, 36
    // -------------------------------------------------------------------------
    private static final int[] SAMPLE = {1, 3, 5, 7, 9, 11};

    // -------------------------------------------------------------------------
    // Construction / build
    // -------------------------------------------------------------------------

    @Test
    void buildFromSingleElement_sizeIsOne() {
        SegmentTree st = new SegmentTree(new int[]{42});
        assertEquals(1, st.size());
    }

    @Test
    void buildFromMultipleElements_sizeMatchesInput() {
        SegmentTree st = new SegmentTree(SAMPLE);
        assertEquals(SAMPLE.length, st.size());
    }

    // -------------------------------------------------------------------------
    // Full-range sum
    // -------------------------------------------------------------------------

    @Test
    void queryFullRange_returnsCorrectSum() {
        // sum(1,3,5,7,9,11) = 36
        SegmentTree st = new SegmentTree(SAMPLE);
        assertEquals(36, st.query(0, 5));
    }

    @Test
    void queryFullRange_singleElement_returnsValue() {
        SegmentTree st = new SegmentTree(new int[]{42});
        assertEquals(42, st.query(0, 0));
    }

    // -------------------------------------------------------------------------
    // Subrange queries
    // -------------------------------------------------------------------------

    @Test
    void queryFirstHalf_returnsCorrectSum() {
        // sum(1,3,5) = 9
        SegmentTree st = new SegmentTree(SAMPLE);
        assertEquals(9, st.query(0, 2));
    }

    @Test
    void querySecondHalf_returnsCorrectSum() {
        // sum(7,9,11) = 27
        SegmentTree st = new SegmentTree(SAMPLE);
        assertEquals(27, st.query(3, 5));
    }

    @Test
    void querySingleElementIndex0_returnsValue() {
        SegmentTree st = new SegmentTree(SAMPLE);
        assertEquals(1, st.query(0, 0));
    }

    @Test
    void querySingleElementLastIndex_returnsValue() {
        SegmentTree st = new SegmentTree(SAMPLE);
        assertEquals(11, st.query(5, 5));
    }

    @Test
    void querySingleElementMiddle_returnsValue() {
        // arr[2] = 5
        SegmentTree st = new SegmentTree(SAMPLE);
        assertEquals(5, st.query(2, 2));
    }

    @Test
    void queryMiddleRange_returnsCorrectSum() {
        // sum(3,5,7) = 15  (indices 1..3)
        SegmentTree st = new SegmentTree(SAMPLE);
        assertEquals(15, st.query(1, 3));
    }

    @Test
    void queryTwoAdjacentElements_returnsCorrectSum() {
        // sum(9,11) = 20  (indices 4..5)
        SegmentTree st = new SegmentTree(SAMPLE);
        assertEquals(20, st.query(4, 5));
    }

    // -------------------------------------------------------------------------
    // Point updates — verify change propagates into subsequent queries
    // -------------------------------------------------------------------------

    @Test
    void updateFirstElement_queryReflectsChange() {
        // Change arr[0] from 1 to 10; full-range sum becomes 45
        SegmentTree st = new SegmentTree(SAMPLE);
        st.update(0, 10);
        assertEquals(45, st.query(0, 5));
    }

    @Test
    void updateLastElement_queryReflectsChange() {
        // Change arr[5] from 11 to 1; full-range sum becomes 26
        SegmentTree st = new SegmentTree(SAMPLE);
        st.update(5, 1);
        assertEquals(26, st.query(0, 5));
    }

    @Test
    void updateMiddleElement_queryReflectsChange() {
        // Change arr[2] from 5 to 50; query(1,3) was 15 → becomes 60
        SegmentTree st = new SegmentTree(SAMPLE);
        st.update(2, 50);
        assertEquals(60, st.query(1, 3));
    }

    @Test
    void updateElement_disjointRangeUnchanged() {
        // Change arr[0]; query(3,5) should still be 27
        SegmentTree st = new SegmentTree(SAMPLE);
        st.update(0, 100);
        assertEquals(27, st.query(3, 5));
    }

    @Test
    void multipleUpdates_queryReflectsAllChanges() {
        // Start: [1,3,5,7,9,11], sum=36
        // update(1, 0) → [1,0,5,7,9,11], sum=33
        // update(4, 1) → [1,0,5,7,1,11], sum=25
        SegmentTree st = new SegmentTree(SAMPLE);
        st.update(1, 0);
        st.update(4, 1);
        assertEquals(25, st.query(0, 5));
    }

    @Test
    void updateToNegativeValue_queryReflectsChange() {
        // Change arr[3] from 7 to -7; full-range sum becomes 36 - 14 = 22
        SegmentTree st = new SegmentTree(SAMPLE);
        st.update(3, -7);
        assertEquals(22, st.query(0, 5));
    }

    @Test
    void updateSingleElementTree_queryReturnsNewValue() {
        SegmentTree st = new SegmentTree(new int[]{5});
        st.update(0, 99);
        assertEquals(99, st.query(0, 0));
    }

    // -------------------------------------------------------------------------
    // Boundary / edge values in the array itself
    // -------------------------------------------------------------------------

    @Test
    void arrayWithAllZeros_queryReturnsZero() {
        SegmentTree st = new SegmentTree(new int[]{0, 0, 0, 0});
        assertEquals(0, st.query(0, 3));
    }

    @Test
    void arrayWithNegatives_queryReturnsCorrectSum() {
        // [-3, -1, 2, 4]  sum = 2
        SegmentTree st = new SegmentTree(new int[]{-3, -1, 2, 4});
        assertEquals(2, st.query(0, 3));
    }

    @Test
    void arrayWithNegatives_subrangeQuery() {
        // [-3, -1, 2, 4]  query(1,2) = -1+2 = 1
        SegmentTree st = new SegmentTree(new int[]{-3, -1, 2, 4});
        assertEquals(1, st.query(1, 2));
    }

    @Test
    void largerArray_fullRangeSum() {
        // arr = [1..10], sum = 55
        int[] arr = new int[10];
        for (int i = 0; i < 10; i++) arr[i] = i + 1;
        SegmentTree st = new SegmentTree(arr);
        assertEquals(55, st.query(0, 9));
    }

    // -------------------------------------------------------------------------
    // Error conditions — constructor
    // -------------------------------------------------------------------------

    @Test
    void constructWithNull_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new SegmentTree(null));
    }

    @Test
    void constructWithEmptyArray_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new SegmentTree(new int[]{}));
    }

    // -------------------------------------------------------------------------
    // Error conditions — query
    // -------------------------------------------------------------------------

    @Test
    void queryNegativeLeft_throwsIllegalArgumentException() {
        SegmentTree st = new SegmentTree(SAMPLE);
        assertThrows(IllegalArgumentException.class, () -> st.query(-1, 2));
    }

    @Test
    void queryRightBeyondEnd_throwsIllegalArgumentException() {
        SegmentTree st = new SegmentTree(SAMPLE);
        assertThrows(IllegalArgumentException.class, () -> st.query(0, 6));
    }

    @Test
    void queryInvertedRange_throwsIllegalArgumentException() {
        SegmentTree st = new SegmentTree(SAMPLE);
        assertThrows(IllegalArgumentException.class, () -> st.query(3, 1));
    }

    // -------------------------------------------------------------------------
    // Error conditions — update
    // -------------------------------------------------------------------------

    @Test
    void updateNegativeIndex_throwsIllegalArgumentException() {
        SegmentTree st = new SegmentTree(SAMPLE);
        assertThrows(IllegalArgumentException.class, () -> st.update(-1, 10));
    }

    @Test
    void updateIndexBeyondEnd_throwsIllegalArgumentException() {
        SegmentTree st = new SegmentTree(SAMPLE);
        assertThrows(IllegalArgumentException.class, () -> st.update(6, 10));
    }
}
