package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MedianOfTwoSortedArraysTest {

    private static final double DELTA = 1e-5;
    private final MedianOfTwoSortedArrays sol = new MedianOfTwoSortedArrays();

    // --- Official LeetCode examples ---

    @Test
    void example1_evenTotal() {
        // nums1=[1,3], nums2=[2] => merged [1,2,3] => median 2.0
        assertEquals(2.0, sol.findMedianSortedArrays(new int[]{1, 3}, new int[]{2}), DELTA);
    }

    @Test
    void example2_oddTotal() {
        // nums1=[1,2], nums2=[3,4] => merged [1,2,3,4] => median (2+3)/2 = 2.5
        assertEquals(2.5, sol.findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4}), DELTA);
    }

    // --- Edge cases ---

    @Test
    void bothSingleElement_odd() {
        // [1], [2] => merged [1,2] => median (1+2)/2=1.5
        assertEquals(1.5, sol.findMedianSortedArrays(new int[]{1}, new int[]{2}), DELTA);
    }

    @Test
    void bothSingleElement_same() {
        // [3], [3] => 3.0
        assertEquals(3.0, sol.findMedianSortedArrays(new int[]{3}, new int[]{3}), DELTA);
    }

    @Test
    void firstArrayEmpty() {
        // [], [1,2,3] => median 2.0
        assertEquals(2.0, sol.findMedianSortedArrays(new int[]{}, new int[]{1, 2, 3}), DELTA);
    }

    @Test
    void secondArrayEmpty() {
        // [1,2,3,4], [] => median (2+3)/2=2.5
        assertEquals(2.5, sol.findMedianSortedArrays(new int[]{1, 2, 3, 4}, new int[]{}), DELTA);
    }

    @Test
    void noOverlap_firstAllSmaller() {
        // [1,2], [3,4,5] => merged [1,2,3,4,5] => median 3.0
        assertEquals(3.0, sol.findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4, 5}), DELTA);
    }

    @Test
    void noOverlap_secondAllSmaller() {
        // [6,7], [1,2,3] => merged [1,2,3,6,7] => median 3.0
        assertEquals(3.0, sol.findMedianSortedArrays(new int[]{6, 7}, new int[]{1, 2, 3}), DELTA);
    }

    @Test
    void largerArrays_evenTotal() {
        // [1,3,5,7], [2,4,6,8] => merged [1,2,3,4,5,6,7,8] => median (4+5)/2=4.5
        assertEquals(4.5, sol.findMedianSortedArrays(new int[]{1, 3, 5, 7}, new int[]{2, 4, 6, 8}), DELTA);
    }

    @Test
    void negativeNumbers() {
        // [-5,-3,-1], [-4,-2,0] => merged [-5,-4,-3,-2,-1,0] => median (-3+-2)/2=-2.5
        assertEquals(-2.5, sol.findMedianSortedArrays(new int[]{-5, -3, -1}, new int[]{-4, -2, 0}), DELTA);
    }

    @Test
    void duplicates() {
        // [1,1,1], [1,1] => merged [1,1,1,1,1] => median 1.0
        assertEquals(1.0, sol.findMedianSortedArrays(new int[]{1, 1, 1}, new int[]{1, 1}), DELTA);
    }

    @Test
    void singleVsMultiple() {
        // [2], [1,3,4,5] => merged [1,2,3,4,5] => median 3.0
        assertEquals(3.0, sol.findMedianSortedArrays(new int[]{2}, new int[]{1, 3, 4, 5}), DELTA);
    }

    @Test
    void partitionAtBoundary_i0() {
        // [5], [1,2,3,4] => merged [1,2,3,4,5] => median 3.0
        // partition i=0 in nums1 means all of nums1 is on right side
        assertEquals(3.0, sol.findMedianSortedArrays(new int[]{5}, new int[]{1, 2, 3, 4}), DELTA);
    }

    @Test
    void partitionAtBoundary_iMax() {
        // [1], [2,3,4,5] => merged [1,2,3,4,5] => median 3.0
        // partition i=1 means all of nums1 is on left side
        assertEquals(3.0, sol.findMedianSortedArrays(new int[]{1}, new int[]{2, 3, 4, 5}), DELTA);
    }
}
