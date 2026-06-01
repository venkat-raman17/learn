package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class SearchInRotatedSortedArrayTest {

    private final SearchInRotatedSortedArray solution = new SearchInRotatedSortedArray();

    @Test
    public void testTargetFound() {
        assertEquals(4, solution.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0));
    }

    @Test
    public void testTargetNotFound() {
        assertEquals(-1, solution.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 3));
    }

    @Test
    public void testSingleElement() {
        assertEquals(-1, solution.search(new int[]{1}, 0));
    }
}
