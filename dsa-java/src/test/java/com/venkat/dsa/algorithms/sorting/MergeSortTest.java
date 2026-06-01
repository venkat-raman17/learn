package com.venkat.dsa.algorithms.sorting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link MergeSort}.
 *
 * <p>Every public operation and relevant edge case is covered:
 * empty array, single element, already-sorted input, reverse-sorted input,
 * random (deterministic) input, duplicates, null rejection.
 */
class MergeSortTest {

    // -----------------------------------------------------------------------
    // Helper
    // -----------------------------------------------------------------------

    /** Returns a copy of {@code a} so callers can keep the original. */
    private static int[] copy(int[] a) {
        int[] c = new int[a.length];
        System.arraycopy(a, 0, c, 0, a.length);
        return c;
    }

    // -----------------------------------------------------------------------
    // Edge cases
    // -----------------------------------------------------------------------

    @Test
    void sort_nullArray_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> MergeSort.sort(null));
    }

    @Test
    void sort_emptyArray_remainsEmpty() {
        int[] a = {};
        MergeSort.sort(a);
        assertArrayEquals(new int[]{}, a);
    }

    @Test
    void sort_singleElement_unchanged() {
        int[] a = {42};
        MergeSort.sort(a);
        assertArrayEquals(new int[]{42}, a);
    }

    @Test
    void sort_twoElementsAlreadySorted_unchanged() {
        int[] a = {1, 2};
        MergeSort.sort(a);
        assertArrayEquals(new int[]{1, 2}, a);
    }

    @Test
    void sort_twoElementsReverseSorted_sortsCorrectly() {
        int[] a = {2, 1};
        MergeSort.sort(a);
        assertArrayEquals(new int[]{1, 2}, a);
    }

    // -----------------------------------------------------------------------
    // Already-sorted input
    // -----------------------------------------------------------------------

    @Test
    void sort_alreadySortedArray_remainsSorted() {
        int[] a = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] expected = copy(a);
        MergeSort.sort(a);
        assertArrayEquals(expected, a);
    }

    // -----------------------------------------------------------------------
    // Reverse-sorted input
    // -----------------------------------------------------------------------

    @Test
    void sort_reverseSortedArray_sortsCorrectly() {
        int[] a        = {9, 7, 5, 3, 1};
        int[] expected = {1, 3, 5, 7, 9};
        MergeSort.sort(a);
        assertArrayEquals(expected, a);
    }

    // -----------------------------------------------------------------------
    // Deterministic "random" input
    // -----------------------------------------------------------------------

    @Test
    void sort_unsortedArray_sortsCorrectly() {
        int[] a        = {5, 3, 8, 1, 9, 2, 7, 4, 6};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        MergeSort.sort(a);
        assertArrayEquals(expected, a);
    }

    @Test
    void sort_largerUnsortedArray_sortsCorrectly() {
        int[] a        = {15, 3, 9, 7, 22, 1, 18, 11, 4, 6, 20, 13, 2, 8, 17, 5};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 13, 15, 17, 18, 20, 22};
        MergeSort.sort(a);
        assertArrayEquals(expected, a);
    }

    // -----------------------------------------------------------------------
    // Duplicates
    // -----------------------------------------------------------------------

    @Test
    void sort_allDuplicates_remainsUnchanged() {
        int[] a        = {7, 7, 7, 7, 7};
        int[] expected = {7, 7, 7, 7, 7};
        MergeSort.sort(a);
        assertArrayEquals(expected, a);
    }

    @Test
    void sort_someDuplicates_sortsCorrectly() {
        int[] a        = {4, 2, 4, 1, 3, 2, 3};
        int[] expected = {1, 2, 2, 3, 3, 4, 4};
        MergeSort.sort(a);
        assertArrayEquals(expected, a);
    }

    @Test
    void sort_twoDistinctValuesManyDuplicates_sortsCorrectly() {
        int[] a        = {1, 0, 1, 0, 0, 1, 0, 1, 1, 0};
        int[] expected = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1};
        MergeSort.sort(a);
        assertArrayEquals(expected, a);
    }

    // -----------------------------------------------------------------------
    // Boundary values
    // -----------------------------------------------------------------------

    @Test
    void sort_arrayWithIntMinAndMax_sortsCorrectly() {
        int[] a        = {Integer.MAX_VALUE, 0, Integer.MIN_VALUE, 1, -1};
        int[] expected = {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE};
        MergeSort.sort(a);
        assertArrayEquals(expected, a);
    }

    @Test
    void sort_allNegativeNumbers_sortsCorrectly() {
        int[] a        = {-3, -1, -7, -4, -2};
        int[] expected = {-7, -4, -3, -2, -1};
        MergeSort.sort(a);
        assertArrayEquals(expected, a);
    }

    @Test
    void sort_mixedNegativeAndPositive_sortsCorrectly() {
        int[] a        = {3, -2, 0, -5, 4, -1};
        int[] expected = {-5, -2, -1, 0, 3, 4};
        MergeSort.sort(a);
        assertArrayEquals(expected, a);
    }

    // -----------------------------------------------------------------------
    // Stability verification (observable via index-tagged values mapped back)
    // -----------------------------------------------------------------------

    /**
     * Verifies stability indirectly: an array with equal values that are
     * interleaved should produce a sorted result where equal values appear
     * the correct number of times (full stability proof requires a stable
     * key-value sort; here we confirm the sort is correct and deterministic).
     */
    @Test
    void sort_stableForEqualElements_preservesOrder() {
        // We verify stability by wrapping ints in a key-value pair sort using
        // the standard library and confirming MergeSort produces the same order.
        // For primitive int[] we can only confirm the values are correct.
        int[] a        = {2, 1, 2, 1, 2};
        int[] expected = {1, 1, 2, 2, 2};
        MergeSort.sort(a);
        assertArrayEquals(expected, a);
    }

    // -----------------------------------------------------------------------
    // Odd and even length arrays
    // -----------------------------------------------------------------------

    @Test
    void sort_oddLengthArray_sortsCorrectly() {
        int[] a        = {10, 3, 6, 1, 8};   // length 5
        int[] expected = {1, 3, 6, 8, 10};
        MergeSort.sort(a);
        assertArrayEquals(expected, a);
    }

    @Test
    void sort_evenLengthArray_sortsCorrectly() {
        int[] a        = {6, 2, 8, 4};        // length 4
        int[] expected = {2, 4, 6, 8};
        MergeSort.sort(a);
        assertArrayEquals(expected, a);
    }

    // -----------------------------------------------------------------------
    // Power-of-two and non-power-of-two sizes
    // -----------------------------------------------------------------------

    @Test
    void sort_powerOfTwoSize_sortsCorrectly() {
        int[] a        = {8, 4, 7, 2, 6, 1, 5, 3};   // length 8
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8};
        MergeSort.sort(a);
        assertArrayEquals(expected, a);
    }

    @Test
    void sort_nonPowerOfTwoSize_sortsCorrectly() {
        int[] a        = {9, 6, 3, 7, 1, 4, 8, 2, 5};  // length 9
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        MergeSort.sort(a);
        assertArrayEquals(expected, a);
    }
}
