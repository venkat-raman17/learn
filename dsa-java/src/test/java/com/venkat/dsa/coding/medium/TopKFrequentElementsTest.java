package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class TopKFrequentElementsTest {

    private final TopKFrequentElements solution = new TopKFrequentElements();

    @Test
    public void testTopTwo() {
        int[] result = solution.topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2);
        Arrays.sort(result);
        assertArrayEquals(new int[]{1, 2}, result);
    }

    @Test
    public void testSingleElement() {
        int[] result = solution.topKFrequent(new int[]{1}, 1);
        assertArrayEquals(new int[]{1}, result);
    }

    @Test
    public void testTopOne() {
        int[] result = solution.topKFrequent(new int[]{4, 4, 4, 5, 5, 6}, 1);
        assertArrayEquals(new int[]{4}, result);
    }
}
