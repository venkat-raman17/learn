package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KthLargestElementInAnArrayTest {

    private final KthLargestElementInAnArray sol = new KthLargestElementInAnArray();

    // Official example 1: [3,2,1,5,6,4], k=2 -> 5
    // Sorted desc: [6,5,4,3,2,1]; 2nd largest = 5
    @Test
    void testOfficialExample1() {
        assertEquals(5, sol.findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2));
    }

    // Official example 2: [3,2,3,1,2,4,5,5,6], k=4 -> 4
    // Sorted desc: [6,5,5,4,3,3,2,2,1]; 4th largest = 4
    @Test
    void testOfficialExample2() {
        assertEquals(4, sol.findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4));
    }

    // k=1 -> maximum element
    @Test
    void testKEqualsOne() {
        assertEquals(9, sol.findKthLargest(new int[]{1, 9, 3, 5}, 1));
    }

    // k equals array length -> minimum element
    @Test
    void testKEqualsLength() {
        assertEquals(1, sol.findKthLargest(new int[]{4, 2, 1, 3}, 4));
    }

    // Single element
    @Test
    void testSingleElement() {
        assertEquals(7, sol.findKthLargest(new int[]{7}, 1));
    }

    // All elements equal
    @Test
    void testAllEqual() {
        assertEquals(5, sol.findKthLargest(new int[]{5, 5, 5, 5}, 3));
    }

    // Negative numbers: [-1,-2,-3,-4,-5], k=2 -> -2
    @Test
    void testNegativeNumbers() {
        assertEquals(-2, sol.findKthLargest(new int[]{-1, -2, -3, -4, -5}, 2));
    }

    // Mixed positive and negative: [-3,1,0,2,-1], k=3 -> 0
    // Sorted desc: [2,1,0,-1,-3]; 3rd = 0
    @Test
    void testMixedNumbers() {
        assertEquals(0, sol.findKthLargest(new int[]{-3, 1, 0, 2, -1}, 3));
    }
}
