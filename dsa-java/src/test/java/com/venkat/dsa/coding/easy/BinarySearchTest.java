package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class BinarySearchTest {

    private final BinarySearch solution = new BinarySearch();

    @Test
    public void testTargetFound() {
        assertEquals(4, solution.search(new int[]{-1, 0, 3, 5, 9, 12}, 9));
    }

    @Test
    public void testTargetNotFound() {
        assertEquals(-1, solution.search(new int[]{-1, 0, 3, 5, 9, 12}, 2));
    }

    @Test
    public void testSingleElement() {
        assertEquals(0, solution.search(new int[]{5}, 5));
    }
}
