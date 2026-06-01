package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class DailyTemperaturesTest {

    private final DailyTemperatures solution = new DailyTemperatures();

    @Test
    public void testExample1() {
        assertArrayEquals(
            new int[]{1, 1, 4, 2, 1, 1, 0, 0},
            solution.dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73}));
    }

    @Test
    public void testAscending() {
        assertArrayEquals(
            new int[]{1, 1, 1, 0},
            solution.dailyTemperatures(new int[]{30, 40, 50, 60}));
    }

    @Test
    public void testDescending() {
        assertArrayEquals(
            new int[]{0, 0, 0},
            solution.dailyTemperatures(new int[]{60, 50, 40}));
    }
}
