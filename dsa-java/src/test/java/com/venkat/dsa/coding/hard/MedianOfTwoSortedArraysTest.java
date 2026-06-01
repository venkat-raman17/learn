package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class MedianOfTwoSortedArraysTest {

    private final MedianOfTwoSortedArrays solution = new MedianOfTwoSortedArrays();

    @Test
    public void testOddTotalLength() {
        assertEquals(2.0, solution.findMedianSortedArrays(new int[]{1, 3}, new int[]{2}), 1e-5);
    }

    @Test
    public void testEvenTotalLength() {
        assertEquals(2.5, solution.findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4}), 1e-5);
    }

    @Test
    public void testOneEmptyArray() {
        assertEquals(2.0, solution.findMedianSortedArrays(new int[]{}, new int[]{1, 2, 3}), 1e-5);
    }
}
