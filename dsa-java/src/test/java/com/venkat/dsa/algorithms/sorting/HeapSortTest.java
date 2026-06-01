package com.venkat.dsa.algorithms.sorting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link HeapSort}.
 *
 * <p>Each test method is named after the behaviour it verifies rather than a
 * sequential number.  All inputs are deterministic so expected outputs can be
 * reasoned about without running code.
 */
class HeapSortTest {

    // -----------------------------------------------------------------------
    // Edge cases
    // -----------------------------------------------------------------------

    @Test
    void sort_nullArray_doesNotThrow() {
        // null should be treated as a no-op, not a NullPointerException.
        assertDoesNotThrow(() -> HeapSort.sort(null));
    }

    @Test
    void sort_emptyArray_remainsEmpty() {
        int[] arr = {};
        HeapSort.sort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void sort_singleElement_unchangedArray() {
        int[] arr = {42};
        HeapSort.sort(arr);
        assertArrayEquals(new int[]{42}, arr);
    }

    // -----------------------------------------------------------------------
    // Two-element arrays (boundary for first swap logic)
    // -----------------------------------------------------------------------

    @Test
    void sort_twoElementsAscending_remainsSorted() {
        int[] arr = {1, 2};
        HeapSort.sort(arr);
        assertArrayEquals(new int[]{1, 2}, arr);
    }

    @Test
    void sort_twoElementsDescending_getsReversed() {
        int[] arr = {5, 3};
        HeapSort.sort(arr);
        assertArrayEquals(new int[]{3, 5}, arr);
    }

    @Test
    void sort_twoEqualElements_orderPreservedAsEqual() {
        int[] arr = {7, 7};
        HeapSort.sort(arr);
        assertArrayEquals(new int[]{7, 7}, arr);
    }

    // -----------------------------------------------------------------------
    // Already sorted input
    // -----------------------------------------------------------------------

    @Test
    void sort_alreadySortedArray_remainsSorted() {
        int[] arr = {1, 2, 3, 4, 5};
        HeapSort.sort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    // -----------------------------------------------------------------------
    // Reverse-sorted input
    // -----------------------------------------------------------------------

    @Test
    void sort_reverseSortedArray_producesSortedResult() {
        int[] arr = {9, 7, 5, 3, 1};
        HeapSort.sort(arr);
        assertArrayEquals(new int[]{1, 3, 5, 7, 9}, arr);
    }

    // -----------------------------------------------------------------------
    // Duplicates
    // -----------------------------------------------------------------------

    @Test
    void sort_allDuplicates_remainsAllSameValue() {
        int[] arr = {4, 4, 4, 4};
        HeapSort.sort(arr);
        assertArrayEquals(new int[]{4, 4, 4, 4}, arr);
    }

    @Test
    void sort_someduplicates_sortedCorrectly() {
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
        HeapSort.sort(arr);
        assertArrayEquals(new int[]{1, 1, 2, 3, 3, 4, 5, 5, 6, 9}, arr);
    }

    // -----------------------------------------------------------------------
    // Negative numbers
    // -----------------------------------------------------------------------

    @Test
    void sort_negativeNumbers_sortedCorrectly() {
        int[] arr = {-3, -1, -4, -1, -5};
        HeapSort.sort(arr);
        assertArrayEquals(new int[]{-5, -4, -3, -1, -1}, arr);
    }

    @Test
    void sort_mixedNegativeAndPositive_sortedCorrectly() {
        int[] arr = {0, -2, 3, -1, 2};
        HeapSort.sort(arr);
        assertArrayEquals(new int[]{-2, -1, 0, 2, 3}, arr);
    }

    // -----------------------------------------------------------------------
    // Boundary values
    // -----------------------------------------------------------------------

    @Test
    void sort_integerMinAndMax_sortedCorrectly() {
        int[] arr = {Integer.MAX_VALUE, 0, Integer.MIN_VALUE, 1, -1};
        HeapSort.sort(arr);
        assertArrayEquals(new int[]{Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE}, arr);
    }

    // -----------------------------------------------------------------------
    // Random-like deterministic input
    // -----------------------------------------------------------------------

    @Test
    void sort_unsortedArray_producesSortedResult() {
        int[] arr = {5, 2, 8, 1, 9, 3, 7, 4, 6};
        HeapSort.sort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, arr);
    }

    @Test
    void sort_largerUnsortedArray_producesSortedResult() {
        // 15 elements — exercises multiple levels of the heap tree.
        int[] arr     = {15, 3, 9, 7, 1, 14, 8, 2, 13, 6, 12, 5, 11, 4, 10};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        HeapSort.sort(arr);
        assertArrayEquals(expected, arr);
    }

    // -----------------------------------------------------------------------
    // Verify original array is mutated (in-place contract)
    // -----------------------------------------------------------------------

    @Test
    void sort_mutatesOriginalArrayInPlace() {
        int[] arr = {3, 1, 2};
        int[] ref = arr; // same reference
        HeapSort.sort(arr);
        assertSame(ref, arr, "sort() must operate in-place on the same array object");
        assertArrayEquals(new int[]{1, 2, 3}, arr);
    }
}
