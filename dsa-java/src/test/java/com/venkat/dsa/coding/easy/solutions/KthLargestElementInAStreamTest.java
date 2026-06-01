package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KthLargestElementInAStreamTest {

    // -----------------------------------------------------------------------
    // Official example 1 (LeetCode #703)
    // k=3, nums=[4,5,8,2], then add calls: 3,5,10,9,4
    // After init: sorted = [2,4,5,8] -> k=3 largest are [4,5,8] -> heap min=4
    // add(3)  -> heap=[4,5,8],    peek=4  (3 discarded)
    // add(5)  -> heap=[5,5,8],    peek=5
    // add(10) -> heap=[5,8,10],   peek=5  (5 is the 3rd largest)
    // add(9)  -> heap=[8,9,10],   peek=8  (5 evicted? let's verify:
    //            after add(9): heap=[5,8,9,10] size=4>3 -> poll min=5 -> heap=[8,9,10] peek=8)
    // add(4)  -> add 4 -> heap=[4,8,9,10] size=4>3 -> poll=4 -> heap=[8,9,10] peek=8
    // -----------------------------------------------------------------------
    @Test
    void testOfficialExample() {
        KthLargestElementInAStream kth = new KthLargestElementInAStream(3, new int[]{4, 5, 8, 2});
        assertEquals(4,  kth.add(3));
        assertEquals(5,  kth.add(5));
        assertEquals(5,  kth.add(10));
        assertEquals(8,  kth.add(9));
        assertEquals(8,  kth.add(4));
    }

    // k=1: always returns the current maximum
    @Test
    void testKEqualsOne() {
        KthLargestElementInAStream kth = new KthLargestElementInAStream(1, new int[]{});
        assertEquals(5,  kth.add(5));
        assertEquals(10, kth.add(10));
        assertEquals(10, kth.add(3));
    }

    // Empty initial array, k=2
    // add(1) -> heap=[1] size<2 -> peek=1
    // add(2) -> heap=[1,2] -> peek=1
    // add(3) -> heap=[2,3] (1 evicted) -> peek=2
    @Test
    void testEmptyInitialArray() {
        KthLargestElementInAStream kth = new KthLargestElementInAStream(2, new int[]{});
        assertEquals(1, kth.add(1));
        assertEquals(1, kth.add(2));
        assertEquals(2, kth.add(3));
    }

    // Negative numbers
    // k=2, nums=[-5,-3], sorted=[-5,-3], k=2 largest=[-5,-3], peek=-5
    // add(-1) -> heap=[-3,-1] (size was 2 after init with -5,-3; add -1 -> size=3>2 -> poll=-5 -> [-3,-1]) peek=-3
    @Test
    void testNegativeNumbers() {
        KthLargestElementInAStream kth = new KthLargestElementInAStream(2, new int[]{-5, -3});
        assertEquals(-3, kth.add(-1));
        assertEquals(-1, kth.add(0));
    }

    // All equal elements
    @Test
    void testAllEqualElements() {
        KthLargestElementInAStream kth = new KthLargestElementInAStream(3, new int[]{7, 7, 7, 7});
        assertEquals(7, kth.add(7));
    }
}
