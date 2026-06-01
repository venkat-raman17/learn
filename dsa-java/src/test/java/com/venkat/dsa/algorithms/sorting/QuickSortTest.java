package com.venkat.dsa.algorithms.sorting;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Behavioral tests for {@link QuickSort}.
 *
 * <p>Each test method is named for the behaviour it verifies. Both
 * {@code sortWithLomuto} and {@code sortWithHoare} are exercised to ensure
 * both partition schemes produce a correctly sorted result. The public
 * {@code sort} method (which delegates to Lomuto internally) is also covered.
 */
class QuickSortTest {

    // -----------------------------------------------------------------------
    // Helper — produces an independently sorted copy to compare against
    // -----------------------------------------------------------------------

    private static int[] expected(int... values) {
        int[] copy = values.clone();
        Arrays.sort(copy);
        return copy;
    }

    // -----------------------------------------------------------------------
    // Null input
    // -----------------------------------------------------------------------

    @Test
    void sort_nullArray_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> QuickSort.sort(null));
    }

    @Test
    void sortWithLomuto_nullArray_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> QuickSort.sortWithLomuto(null));
    }

    @Test
    void sortWithHoare_nullArray_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> QuickSort.sortWithHoare(null));
    }

    // -----------------------------------------------------------------------
    // Empty array
    // -----------------------------------------------------------------------

    @Test
    void sort_emptyArray_remainsEmpty() {
        int[] arr = {};
        QuickSort.sort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void sortWithLomuto_emptyArray_remainsEmpty() {
        int[] arr = {};
        QuickSort.sortWithLomuto(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void sortWithHoare_emptyArray_remainsEmpty() {
        int[] arr = {};
        QuickSort.sortWithHoare(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    // -----------------------------------------------------------------------
    // Single element
    // -----------------------------------------------------------------------

    @Test
    void sort_singleElement_unchanged() {
        int[] arr = {42};
        QuickSort.sort(arr);
        assertArrayEquals(new int[]{42}, arr);
    }

    @Test
    void sortWithLomuto_singleElement_unchanged() {
        int[] arr = {7};
        QuickSort.sortWithLomuto(arr);
        assertArrayEquals(new int[]{7}, arr);
    }

    @Test
    void sortWithHoare_singleElement_unchanged() {
        int[] arr = {-3};
        QuickSort.sortWithHoare(arr);
        assertArrayEquals(new int[]{-3}, arr);
    }

    // -----------------------------------------------------------------------
    // Two elements
    // -----------------------------------------------------------------------

    @Test
    void sort_twoElementsOutOfOrder_sorted() {
        int[] arr = {5, 2};
        QuickSort.sort(arr);
        assertArrayEquals(new int[]{2, 5}, arr);
    }

    @Test
    void sort_twoElementsAlreadyOrdered_unchanged() {
        int[] arr = {1, 9};
        QuickSort.sort(arr);
        assertArrayEquals(new int[]{1, 9}, arr);
    }

    // -----------------------------------------------------------------------
    // Already sorted
    // -----------------------------------------------------------------------

    @Test
    void sort_alreadySortedArray_remainsSorted() {
        int[] arr = {1, 2, 3, 4, 5};
        int[] exp = expected(1, 2, 3, 4, 5);
        QuickSort.sort(arr);
        assertArrayEquals(exp, arr);
    }

    @Test
    void sortWithLomuto_alreadySortedArray_remainsSorted() {
        int[] arr = {10, 20, 30, 40};
        int[] exp = expected(10, 20, 30, 40);
        QuickSort.sortWithLomuto(arr);
        assertArrayEquals(exp, arr);
    }

    @Test
    void sortWithHoare_alreadySortedArray_remainsSorted() {
        int[] arr = {0, 1, 2, 3, 4, 5};
        int[] exp = expected(0, 1, 2, 3, 4, 5);
        QuickSort.sortWithHoare(arr);
        assertArrayEquals(exp, arr);
    }

    // -----------------------------------------------------------------------
    // Reverse sorted
    // -----------------------------------------------------------------------

    @Test
    void sort_reverseSortedArray_sortedAscending() {
        int[] arr = {9, 7, 5, 3, 1};
        int[] exp = expected(9, 7, 5, 3, 1);
        QuickSort.sort(arr);
        assertArrayEquals(exp, arr);
    }

    @Test
    void sortWithLomuto_reverseSortedArray_sortedAscending() {
        int[] arr = {100, 50, 25, 10, 5, 1};
        int[] exp = expected(100, 50, 25, 10, 5, 1);
        QuickSort.sortWithLomuto(arr);
        assertArrayEquals(exp, arr);
    }

    @Test
    void sortWithHoare_reverseSortedArray_sortedAscending() {
        int[] arr = {8, 6, 4, 2, 0};
        int[] exp = expected(8, 6, 4, 2, 0);
        QuickSort.sortWithHoare(arr);
        assertArrayEquals(exp, arr);
    }

    // -----------------------------------------------------------------------
    // Duplicates
    // -----------------------------------------------------------------------

    @Test
    void sort_allDuplicates_sortedCorrectly() {
        int[] arr = {3, 3, 3, 3, 3};
        QuickSort.sort(arr);
        assertArrayEquals(new int[]{3, 3, 3, 3, 3}, arr);
    }

    @Test
    void sortWithLomuto_mixedDuplicates_sortedCorrectly() {
        int[] arr = {4, 2, 4, 1, 2, 4};
        int[] exp = expected(4, 2, 4, 1, 2, 4);
        QuickSort.sortWithLomuto(arr);
        assertArrayEquals(exp, arr);
    }

    @Test
    void sortWithHoare_mixedDuplicates_sortedCorrectly() {
        int[] arr = {5, 1, 5, 2, 5, 3, 5};
        int[] exp = expected(5, 1, 5, 2, 5, 3, 5);
        QuickSort.sortWithHoare(arr);
        assertArrayEquals(exp, arr);
    }

    @Test
    void sort_twoDistinctValues_sortedCorrectly() {
        int[] arr = {1, 0, 1, 0, 0, 1, 1, 0};
        int[] exp = expected(1, 0, 1, 0, 0, 1, 1, 0);
        QuickSort.sort(arr);
        assertArrayEquals(exp, arr);
    }

    // -----------------------------------------------------------------------
    // Negative numbers and mixed signs
    // -----------------------------------------------------------------------

    @Test
    void sort_negativeNumbers_sortedAscending() {
        int[] arr = {-3, -1, -7, -2, -5};
        int[] exp = expected(-3, -1, -7, -2, -5);
        QuickSort.sort(arr);
        assertArrayEquals(exp, arr);
    }

    @Test
    void sort_mixedPositiveAndNegative_sortedAscending() {
        int[] arr = {3, -2, 7, 0, -5, 1};
        int[] exp = expected(3, -2, 7, 0, -5, 1);
        QuickSort.sort(arr);
        assertArrayEquals(exp, arr);
    }

    @Test
    void sortWithHoare_mixedSignNumbers_sortedAscending() {
        int[] arr = {-10, 4, -3, 0, 8, -1};
        int[] exp = expected(-10, 4, -3, 0, 8, -1);
        QuickSort.sortWithHoare(arr);
        assertArrayEquals(exp, arr);
    }

    // -----------------------------------------------------------------------
    // Integer boundary values
    // -----------------------------------------------------------------------

    @Test
    void sort_intMinAndMaxValues_sortedCorrectly() {
        int[] arr = {Integer.MAX_VALUE, 0, Integer.MIN_VALUE, 1, -1};
        int[] exp = {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE};
        QuickSort.sort(arr);
        assertArrayEquals(exp, arr);
    }

    @Test
    void sortWithLomuto_intMinAndMaxValues_sortedCorrectly() {
        int[] arr = {Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE};
        int[] exp = {Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE};
        QuickSort.sortWithLomuto(arr);
        assertArrayEquals(exp, arr);
    }

    @Test
    void sortWithHoare_intMinAndMaxValues_sortedCorrectly() {
        int[] arr = {Integer.MAX_VALUE, Integer.MIN_VALUE};
        int[] exp = {Integer.MIN_VALUE, Integer.MAX_VALUE};
        QuickSort.sortWithHoare(arr);
        assertArrayEquals(exp, arr);
    }

    // -----------------------------------------------------------------------
    // Larger / random-like deterministic inputs
    // -----------------------------------------------------------------------

    @Test
    void sort_largeDeterministicArray_sortedCorrectly() {
        // Constructed deterministically: modular sequence, no Random inside test.
        int n = 100;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (i * 37 + 13) % 97; // deterministic, non-monotone
        }
        int[] exp = arr.clone();
        Arrays.sort(exp);
        QuickSort.sort(arr);
        assertArrayEquals(exp, arr);
    }

    @Test
    void sortWithLomuto_largeDeterministicArray_sortedCorrectly() {
        int n = 200;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (i * 53 + 7) % 199 - 99; // deterministic, negative range included
        }
        int[] exp = arr.clone();
        Arrays.sort(exp);
        QuickSort.sortWithLomuto(arr);
        assertArrayEquals(exp, arr);
    }

    @Test
    void sortWithHoare_largeDeterministicArray_sortedCorrectly() {
        int n = 150;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (i * 41 + 19) % 151 - 75;
        }
        int[] exp = arr.clone();
        Arrays.sort(exp);
        QuickSort.sortWithHoare(arr);
        assertArrayEquals(exp, arr);
    }

    // -----------------------------------------------------------------------
    // Verify original array is mutated (not a copy returned)
    // -----------------------------------------------------------------------

    @Test
    void sort_mutatesOriginalArray_notACopy() {
        int[] arr = {5, 3, 1, 4, 2};
        int[] ref = arr; // same reference
        QuickSort.sort(arr);
        assertSame(ref, arr, "sort() must sort in-place; the array reference must not change");
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void sortWithHoare_mutatesOriginalArray_notACopy() {
        int[] arr = {9, 7, 5, 3, 1};
        int[] ref = arr;
        QuickSort.sortWithHoare(arr);
        assertSame(ref, arr);
        assertArrayEquals(new int[]{1, 3, 5, 7, 9}, arr);
    }
}
