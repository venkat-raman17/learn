package com.venkat.dsa.algorithms.searching;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BinarySearch}.
 *
 * <p>Coverage includes: found/not-found, empty arrays, single-element arrays, duplicates,
 * boundary targets (below min, above max, equal to min/max), lower/upper bound semantics,
 * rotated-array search at various pivot positions, and null-input error conditions.
 */
class BinarySearchTest {

    // -----------------------------------------------------------------------
    // search — basic
    // -----------------------------------------------------------------------

    @Test
    void search_emptyArray_returnsMinusOne() {
        assertEquals(-1, BinarySearch.search(new int[]{}, 5));
    }

    @Test
    void search_singleElement_found() {
        assertEquals(0, BinarySearch.search(new int[]{7}, 7));
    }

    @Test
    void search_singleElement_notFound() {
        assertEquals(-1, BinarySearch.search(new int[]{7}, 3));
    }

    @Test
    void search_targetAtFirstIndex() {
        int[] arr = {1, 3, 5, 7, 9};
        assertEquals(0, BinarySearch.search(arr, 1));
    }

    @Test
    void search_targetAtLastIndex() {
        int[] arr = {1, 3, 5, 7, 9};
        assertEquals(4, BinarySearch.search(arr, 9));
    }

    @Test
    void search_targetInMiddle() {
        int[] arr = {2, 4, 6, 8, 10};
        assertEquals(2, BinarySearch.search(arr, 6));
    }

    @Test
    void search_targetBelowMin_returnsMinusOne() {
        int[] arr = {3, 5, 7};
        assertEquals(-1, BinarySearch.search(arr, 1));
    }

    @Test
    void search_targetAboveMax_returnsMinusOne() {
        int[] arr = {3, 5, 7};
        assertEquals(-1, BinarySearch.search(arr, 10));
    }

    @Test
    void search_duplicates_returnsAValidIndex() {
        int[] arr = {1, 2, 2, 2, 3};
        int idx = BinarySearch.search(arr, 2);
        assertTrue(idx >= 1 && idx <= 3, "Expected index 1, 2, or 3 but got " + idx);
        assertEquals(2, arr[idx]);
    }

    @Test
    void search_nullArray_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> BinarySearch.search(null, 5));
    }

    // -----------------------------------------------------------------------
    // lowerBound
    // -----------------------------------------------------------------------

    @Test
    void lowerBound_emptyArray_returnsZero() {
        assertEquals(0, BinarySearch.lowerBound(new int[]{}, 5));
    }

    @Test
    void lowerBound_targetPresentOnce_returnsItsIndex() {
        int[] arr = {1, 3, 5, 7, 9};
        // arr[2] == 5
        assertEquals(2, BinarySearch.lowerBound(arr, 5));
    }

    @Test
    void lowerBound_targetIsDuplicated_returnsFirstOccurrence() {
        int[] arr = {1, 2, 2, 2, 3};
        // first 2 is at index 1
        assertEquals(1, BinarySearch.lowerBound(arr, 2));
    }

    @Test
    void lowerBound_targetBelowAllElements_returnsZero() {
        int[] arr = {3, 5, 7, 9};
        assertEquals(0, BinarySearch.lowerBound(arr, 1));
    }

    @Test
    void lowerBound_targetAboveAllElements_returnsArrayLength() {
        int[] arr = {3, 5, 7, 9};
        assertEquals(4, BinarySearch.lowerBound(arr, 11));
    }

    @Test
    void lowerBound_targetEqualToMin_returnsZero() {
        int[] arr = {3, 5, 7, 9};
        assertEquals(0, BinarySearch.lowerBound(arr, 3));
    }

    @Test
    void lowerBound_targetEqualToMax_returnsLastIndex() {
        int[] arr = {3, 5, 7, 9};
        assertEquals(3, BinarySearch.lowerBound(arr, 9));
    }

    @Test
    void lowerBound_targetAbsentBetweenElements_returnsNextHigherIndex() {
        int[] arr = {1, 3, 5, 7, 9};
        // 4 is absent; first element >= 4 is arr[2] = 5
        assertEquals(2, BinarySearch.lowerBound(arr, 4));
    }

    @Test
    void lowerBound_nullArray_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> BinarySearch.lowerBound(null, 5));
    }

    // -----------------------------------------------------------------------
    // upperBound
    // -----------------------------------------------------------------------

    @Test
    void upperBound_emptyArray_returnsZero() {
        assertEquals(0, BinarySearch.upperBound(new int[]{}, 5));
    }

    @Test
    void upperBound_targetPresentOnce_returnsIndexAfterIt() {
        int[] arr = {1, 3, 5, 7, 9};
        // arr[2] == 5; first element > 5 is arr[3] == 7
        assertEquals(3, BinarySearch.upperBound(arr, 5));
    }

    @Test
    void upperBound_targetIsDuplicated_returnsIndexAfterLastOccurrence() {
        int[] arr = {1, 2, 2, 2, 3};
        // last 2 is at index 3; first element > 2 is arr[4] == 3
        assertEquals(4, BinarySearch.upperBound(arr, 2));
    }

    @Test
    void upperBound_targetBelowAllElements_returnsZero() {
        int[] arr = {3, 5, 7, 9};
        assertEquals(0, BinarySearch.upperBound(arr, 1));
    }

    @Test
    void upperBound_targetAboveAllElements_returnsArrayLength() {
        int[] arr = {3, 5, 7, 9};
        assertEquals(4, BinarySearch.upperBound(arr, 11));
    }

    @Test
    void upperBound_targetEqualToMin_returnsOne() {
        int[] arr = {3, 5, 7, 9};
        // first element > 3 is arr[1] == 5
        assertEquals(1, BinarySearch.upperBound(arr, 3));
    }

    @Test
    void upperBound_targetEqualToMax_returnsArrayLength() {
        int[] arr = {3, 5, 7, 9};
        assertEquals(4, BinarySearch.upperBound(arr, 9));
    }

    @Test
    void upperBound_targetAbsentBetweenElements_returnsNextHigherIndex() {
        int[] arr = {1, 3, 5, 7, 9};
        // 4 is absent; first element > 4 is arr[2] == 5
        assertEquals(2, BinarySearch.upperBound(arr, 4));
    }

    @Test
    void upperBound_nullArray_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> BinarySearch.upperBound(null, 5));
    }

    // -----------------------------------------------------------------------
    // lowerBound / upperBound range semantics
    // -----------------------------------------------------------------------

    @Test
    void lowerAndUpperBound_rangeCoversAllDuplicates() {
        int[] arr = {1, 2, 2, 2, 3};
        int lb = BinarySearch.lowerBound(arr, 2);
        int ub = BinarySearch.upperBound(arr, 2);
        // [lb, ub) = [1, 4) covers indices 1,2,3 — three 2s
        assertEquals(1, lb);
        assertEquals(4, ub);
        assertEquals(3, ub - lb);
    }

    @Test
    void lowerAndUpperBound_absentTarget_rangeIsEmpty() {
        int[] arr = {1, 3, 5, 7};
        int lb = BinarySearch.lowerBound(arr, 4);
        int ub = BinarySearch.upperBound(arr, 4);
        // 4 not present; lb == ub (empty range)
        assertEquals(lb, ub);
    }

    // -----------------------------------------------------------------------
    // searchRotated
    // -----------------------------------------------------------------------

    @Test
    void searchRotated_emptyArray_returnsMinusOne() {
        assertEquals(-1, BinarySearch.searchRotated(new int[]{}, 5));
    }

    @Test
    void searchRotated_singleElement_found() {
        assertEquals(0, BinarySearch.searchRotated(new int[]{42}, 42));
    }

    @Test
    void searchRotated_singleElement_notFound() {
        assertEquals(-1, BinarySearch.searchRotated(new int[]{42}, 7));
    }

    @Test
    void searchRotated_noRotation_sortedArray_found() {
        // rotation point = 0 (not rotated)
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        assertEquals(3, BinarySearch.searchRotated(arr, 4));
    }

    @Test
    void searchRotated_noRotation_sortedArray_notFound() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        assertEquals(-1, BinarySearch.searchRotated(arr, 8));
    }

    @Test
    void searchRotated_pivotAtIndex1_targetInRightHalf() {
        // original [1,2,3,4,5,6,7] rotated 1 step -> [7,1,2,3,4,5,6]
        int[] arr = {7, 1, 2, 3, 4, 5, 6};
        assertEquals(4, BinarySearch.searchRotated(arr, 4));
    }

    @Test
    void searchRotated_pivotAtIndex1_targetInLeftHalf() {
        int[] arr = {7, 1, 2, 3, 4, 5, 6};
        assertEquals(0, BinarySearch.searchRotated(arr, 7));
    }

    @Test
    void searchRotated_pivotNearMiddle_targetFound() {
        // original [0,1,2,4,5,6,7] rotated at index 4 -> [4,5,6,7,0,1,2]
        int[] arr = {4, 5, 6, 7, 0, 1, 2};
        assertEquals(4, BinarySearch.searchRotated(arr, 0));
    }

    @Test
    void searchRotated_pivotNearMiddle_targetInLeftPart() {
        int[] arr = {4, 5, 6, 7, 0, 1, 2};
        assertEquals(1, BinarySearch.searchRotated(arr, 5));
    }

    @Test
    void searchRotated_pivotNearMiddle_targetNotFound() {
        int[] arr = {4, 5, 6, 7, 0, 1, 2};
        assertEquals(-1, BinarySearch.searchRotated(arr, 3));
    }

    @Test
    void searchRotated_pivotAtLastIndex_targetFirst() {
        // original [1,2,3,4,5] rotated 1 -> [5,1,2,3,4]
        int[] arr = {5, 1, 2, 3, 4};
        assertEquals(0, BinarySearch.searchRotated(arr, 5));
    }

    @Test
    void searchRotated_pivotAtLastIndex_targetLast() {
        int[] arr = {5, 1, 2, 3, 4};
        assertEquals(4, BinarySearch.searchRotated(arr, 4));
    }

    @Test
    void searchRotated_targetBelowMin_returnsMinusOne() {
        int[] arr = {4, 5, 6, 7, 0, 1, 2};
        assertEquals(-1, BinarySearch.searchRotated(arr, -1));
    }

    @Test
    void searchRotated_targetAboveMax_returnsMinusOne() {
        int[] arr = {4, 5, 6, 7, 0, 1, 2};
        assertEquals(-1, BinarySearch.searchRotated(arr, 8));
    }

    @Test
    void searchRotated_twoElements_rotated_targetFirst() {
        int[] arr = {2, 1};
        assertEquals(0, BinarySearch.searchRotated(arr, 2));
    }

    @Test
    void searchRotated_twoElements_rotated_targetSecond() {
        int[] arr = {2, 1};
        assertEquals(1, BinarySearch.searchRotated(arr, 1));
    }

    @Test
    void searchRotated_nullArray_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> BinarySearch.searchRotated(null, 5));
    }
}
