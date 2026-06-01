package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class FindMedianFromDataStreamTest {

    @Test
    void testExample1() {
        FindMedianFromDataStream obj = new FindMedianFromDataStream();
        obj.addNum(1);
        obj.addNum(2);
        assertEquals(1.5, obj.findMedian(), 1e-5);
        obj.addNum(3);
        assertEquals(2.0, obj.findMedian(), 1e-5);
    }

    @Test
    void testSingleElement() {
        FindMedianFromDataStream obj = new FindMedianFromDataStream();
        obj.addNum(6);
        assertEquals(6.0, obj.findMedian(), 1e-5);
        obj.addNum(10);
        assertEquals(8.0, obj.findMedian(), 1e-5);
    }

    @Test
    void testNegativeNumbers() {
        FindMedianFromDataStream obj = new FindMedianFromDataStream();
        obj.addNum(-1);
        obj.addNum(-2);
        obj.addNum(-3);
        assertEquals(-2.0, obj.findMedian(), 1e-5);
    }
}
