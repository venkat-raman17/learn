package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class FindMinimumInRotatedSortedArrayTest {

    private final FindMinimumInRotatedSortedArray solution = new FindMinimumInRotatedSortedArray();

    @Test
    public void testRotatedOnce() {
        assertEquals(1, solution.findMin(new int[]{3, 4, 5, 1, 2}));
    }

    @Test
    public void testRotatedMultiple() {
        assertEquals(0, solution.findMin(new int[]{4, 5, 6, 7, 0, 1, 2}));
    }

    @Test
    public void testNoRotation() {
        assertEquals(11, solution.findMin(new int[]{11, 13, 15, 17}));
    }
}
