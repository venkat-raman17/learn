package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class KthLargestElementInAStreamTest {

    @Test
    void testExample1() {
        KthLargestElementInAStream obj = new KthLargestElementInAStream(3, new int[]{4, 5, 8, 2});
        assertEquals(4, obj.add(3));
        assertEquals(5, obj.add(5));
        assertEquals(5, obj.add(10));
        assertEquals(8, obj.add(9));
        assertEquals(8, obj.add(4));
    }

    @Test
    void testKEquals1EmptyInitial() {
        KthLargestElementInAStream obj = new KthLargestElementInAStream(1, new int[]{});
        assertEquals(3, obj.add(3));
        assertEquals(5, obj.add(5));
        assertEquals(5, obj.add(2));
    }

    @Test
    void testKEquals2() {
        KthLargestElementInAStream obj = new KthLargestElementInAStream(2, new int[]{0});
        assertEquals(-1, obj.add(-1));
        assertEquals(1, obj.add(1));
        assertEquals(2, obj.add(2));
    }
}
