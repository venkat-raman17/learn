package com.venkat.dsa.algorithms.sorting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link CountingSort}.
 *
 * <p>Covers: general sort, duplicates, empty array, single element,
 * all-negative input, mixed negatives and positives, max-value boundary,
 * stability (preserved relative order of equal keys), and null input.</p>
 */
class CountingSortTest {

    // ---------------------------------------------------------------
    // Basic correctness
    // ---------------------------------------------------------------

    @Test
    void sortUnsortedArray_returnsAscendingOrder() {
        int[] input    = {5, 3, 1, 4, 2};
        int[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, CountingSort.sort(input));
    }

    @Test
    void sortAlreadySortedArray_returnsSameOrder() {
        int[] input    = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, CountingSort.sort(input));
    }

    @Test
    void sortReverseSortedArray_returnsAscendingOrder() {
        int[] input    = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, CountingSort.sort(input));
    }

    // ---------------------------------------------------------------
    // Duplicates
    // ---------------------------------------------------------------

    @Test
    void sortArrayWithDuplicates_keepsAllOccurrences() {
        int[] input    = {4, 4, 2, 2, 1};
        int[] expected = {1, 2, 2, 4, 4};
        assertArrayEquals(expected, CountingSort.sort(input));
    }

    @Test
    void sortAllIdenticalElements_returnsSameValues() {
        int[] input    = {7, 7, 7, 7};
        int[] expected = {7, 7, 7, 7};
        assertArrayEquals(expected, CountingSort.sort(input));
    }

    // ---------------------------------------------------------------
    // Edge cases — empty and single element
    // ---------------------------------------------------------------

    @Test
    void sortEmptyArray_returnsEmptyArray() {
        int[] result = CountingSort.sort(new int[]{});
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void sortSingleElement_returnsThatElement() {
        int[] input    = {42};
        int[] expected = {42};
        assertArrayEquals(expected, CountingSort.sort(input));
    }

    // ---------------------------------------------------------------
    // Negative numbers
    // ---------------------------------------------------------------

    @Test
    void sortAllNegativeValues_returnsAscendingOrder() {
        int[] input    = {-3, -1, -5, -2, -4};
        int[] expected = {-5, -4, -3, -2, -1};
        assertArrayEquals(expected, CountingSort.sort(input));
    }

    @Test
    void sortMixedNegativeAndPositiveValues_returnsAscendingOrder() {
        int[] input    = {-2, 3, 0, -1, 2};
        int[] expected = {-2, -1, 0, 2, 3};
        assertArrayEquals(expected, CountingSort.sort(input));
    }

    @Test
    void sortArrayContainingZero_handlesZeroCorrectly() {
        int[] input    = {0, -1, 1, 0, -2, 2};
        int[] expected = {-2, -1, 0, 0, 1, 2};
        assertArrayEquals(expected, CountingSort.sort(input));
    }

    // ---------------------------------------------------------------
    // Boundary — includes maximum value in range
    // ---------------------------------------------------------------

    @Test
    void sortArrayIncludingMaxValue_handlesMaxValueCorrectly() {
        // Range is exactly 2 — safe for count array allocation.
        int max = Integer.MAX_VALUE;
        int[] input    = {max, max - 1};
        int[] expected = {max - 1, max};
        assertArrayEquals(expected, CountingSort.sort(input));
    }

    @Test
    void sortArrayIncludingMinValue_handlesMinValueCorrectly() {
        int min = Integer.MIN_VALUE;
        int[] input    = {min + 1, min};
        int[] expected = {min, min + 1};
        assertArrayEquals(expected, CountingSort.sort(input));
    }

    // ---------------------------------------------------------------
    // Stability
    // ---------------------------------------------------------------

    /**
     * Stability cannot be observed with plain int values (equal ints are
     * indistinguishable), but we can verify that all duplicates survive
     * intact and in sorted position, which is the observable side-effect
     * of a stable sort on primitive arrays.
     */
    @Test
    void sortWithMultipleDuplicates_preservesCorrectCount() {
        int[] input    = {3, 1, 3, 2, 1, 3};
        int[] expected = {1, 1, 2, 3, 3, 3};
        assertArrayEquals(expected, CountingSort.sort(input));
    }

    // ---------------------------------------------------------------
    // Output independence — original array must not be mutated
    // ---------------------------------------------------------------

    @Test
    void sort_doesNotMutateOriginalArray() {
        int[] input  = {4, 2, 3, 1};
        int[] backup = {4, 2, 3, 1};
        CountingSort.sort(input);
        assertArrayEquals(backup, input, "sort() must not modify the input array");
    }

    @Test
    void sort_returnsNewArray() {
        int[] input  = {1, 2, 3};
        int[] result = CountingSort.sort(input);
        assertNotSame(input, result, "sort() must return a new array, not the original");
    }

    // ---------------------------------------------------------------
    // Error conditions
    // ---------------------------------------------------------------

    @Test
    void sortNullInput_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CountingSort.sort(null));
    }
}
