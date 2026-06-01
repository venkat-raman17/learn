package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class KthLargestElementInAnArrayTest {

    private final KthLargestElementInAnArray solution = new KthLargestElementInAnArray();

    @Test
    void testExample1() {
        assertEquals(5, solution.findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2));
    }

    @Test
    void testExample2() {
        assertEquals(4, solution.findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4));
    }

    @Test
    void testSingleElement() {
        assertEquals(7, solution.findKthLargest(new int[]{7}, 1));
    }
}
