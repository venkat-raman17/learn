package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@Disabled("practice — delete when you start")
public class SlidingWindowMaximumTest {

    private final SlidingWindowMaximum solution = new SlidingWindowMaximum();

    @Test
    void example1_multipleWindows() {
        assertArrayEquals(
                new int[]{3, 3, 5, 5, 6, 7},
                solution.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
    }

    @Test
    void example2_singleElement() {
        assertArrayEquals(
                new int[]{1},
                solution.maxSlidingWindow(new int[]{1}, 1));
    }

    @Test
    void windowEqualsArraySize() {
        assertArrayEquals(
                new int[]{3},
                solution.maxSlidingWindow(new int[]{1, 2, 3}, 3));
    }
}
