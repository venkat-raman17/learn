package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FindMedianFromDataStreamTest {

    private static final double DELTA = 1e-9;

    // Official example (LeetCode #295):
    // addNum(1), addNum(2), findMedian -> 1.5
    // addNum(3),            findMedian -> 2.0
    @Test
    void testOfficialExample() {
        FindMedianFromDataStream mf = new FindMedianFromDataStream();
        mf.addNum(1);
        mf.addNum(2);
        assertEquals(1.5, mf.findMedian(), DELTA);
        mf.addNum(3);
        assertEquals(2.0, mf.findMedian(), DELTA);
    }

    // Single element
    @Test
    void testSingleElement() {
        FindMedianFromDataStream mf = new FindMedianFromDataStream();
        mf.addNum(42);
        assertEquals(42.0, mf.findMedian(), DELTA);
    }

    // Two elements: median = average
    @Test
    void testTwoElements() {
        FindMedianFromDataStream mf = new FindMedianFromDataStream();
        mf.addNum(3);
        mf.addNum(7);
        // sorted: [3,7] -> median = (3+7)/2 = 5.0
        assertEquals(5.0, mf.findMedian(), DELTA);
    }

    // Three elements: median is the middle
    @Test
    void testThreeElements() {
        FindMedianFromDataStream mf = new FindMedianFromDataStream();
        mf.addNum(5);
        mf.addNum(3);
        mf.addNum(8);
        // sorted: [3,5,8] -> median = 5.0
        assertEquals(5.0, mf.findMedian(), DELTA);
    }

    // Inserting in decreasing order
    // stream: 10,9,8,7,6
    // after 10:   [10] -> median=10
    // after 9:    [9,10] -> median=9.5
    // after 8:    [8,9,10] -> median=9
    // after 7:    [7,8,9,10] -> median=8.5
    // after 6:    [6,7,8,9,10] -> median=8
    @Test
    void testDecreasingOrder() {
        FindMedianFromDataStream mf = new FindMedianFromDataStream();
        mf.addNum(10);
        assertEquals(10.0, mf.findMedian(), DELTA);
        mf.addNum(9);
        assertEquals(9.5, mf.findMedian(), DELTA);
        mf.addNum(8);
        assertEquals(9.0, mf.findMedian(), DELTA);
        mf.addNum(7);
        assertEquals(8.5, mf.findMedian(), DELTA);
        mf.addNum(6);
        assertEquals(8.0, mf.findMedian(), DELTA);
    }

    // Negative numbers
    // stream: -5, -3, -1
    // after -5: [-5] -> -5
    // after -3: [-5,-3] -> -4.0
    // after -1: [-5,-3,-1] -> -3.0
    @Test
    void testNegativeNumbers() {
        FindMedianFromDataStream mf = new FindMedianFromDataStream();
        mf.addNum(-5);
        assertEquals(-5.0, mf.findMedian(), DELTA);
        mf.addNum(-3);
        assertEquals(-4.0, mf.findMedian(), DELTA);
        mf.addNum(-1);
        assertEquals(-3.0, mf.findMedian(), DELTA);
    }

    // Duplicates
    // stream: 2,2,2,2 -> median always 2
    @Test
    void testDuplicates() {
        FindMedianFromDataStream mf = new FindMedianFromDataStream();
        for (int i = 0; i < 4; i++) {
            mf.addNum(2);
        }
        assertEquals(2.0, mf.findMedian(), DELTA);
    }

    // Interleaved small and large values
    // stream: 1,100,2,99,3
    // sorted at each step:
    // [1]         -> 1.0
    // [1,100]     -> 50.5
    // [1,2,100]   -> 2.0
    // [1,2,99,100]-> 50.5
    // [1,2,3,99,100]-> 3.0
    @Test
    void testInterleavedValues() {
        FindMedianFromDataStream mf = new FindMedianFromDataStream();
        mf.addNum(1);
        assertEquals(1.0, mf.findMedian(), DELTA);
        mf.addNum(100);
        assertEquals(50.5, mf.findMedian(), DELTA);
        mf.addNum(2);
        assertEquals(2.0, mf.findMedian(), DELTA);
        mf.addNum(99);
        assertEquals(50.5, mf.findMedian(), DELTA);
        mf.addNum(3);
        assertEquals(3.0, mf.findMedian(), DELTA);
    }
}
