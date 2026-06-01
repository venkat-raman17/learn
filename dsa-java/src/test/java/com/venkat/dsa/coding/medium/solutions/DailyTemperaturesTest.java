package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DailyTemperaturesTest {

    private final DailyTemperatures solution = new DailyTemperatures();

    // Official LeetCode examples
    @Test
    void example1() {
        // temperatures = [73,74,75,71,69,72,76,73]
        // answers:        [ 1, 1, 4, 2, 1, 1, 0, 0]
        assertArrayEquals(
            new int[]{1, 1, 4, 2, 1, 1, 0, 0},
            solution.dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73})
        );
    }

    @Test
    void example2_allDecreasing() {
        // No future day is warmer
        assertArrayEquals(
            new int[]{0, 0, 0},
            solution.dailyTemperatures(new int[]{30, 20, 10})
        );
    }

    @Test
    void example3_allIncreasing() {
        assertArrayEquals(
            new int[]{1, 1, 0},
            solution.dailyTemperatures(new int[]{30, 40, 50})
        );
    }

    // Edge cases
    @Test
    void singleElement() {
        assertArrayEquals(new int[]{0}, solution.dailyTemperatures(new int[]{50}));
    }

    @Test
    void allSameTemperature() {
        assertArrayEquals(
            new int[]{0, 0, 0, 0},
            solution.dailyTemperatures(new int[]{70, 70, 70, 70})
        );
    }

    @Test
    void twoElements_warmerSecond() {
        assertArrayEquals(new int[]{1, 0}, solution.dailyTemperatures(new int[]{50, 60}));
    }

    @Test
    void twoElements_warmerFirst() {
        assertArrayEquals(new int[]{0, 0}, solution.dailyTemperatures(new int[]{60, 50}));
    }

    @Test
    void valleyThenPeak() {
        // [100, 50, 60, 70, 80] — index 0 never gets warmer
        assertArrayEquals(
            new int[]{0, 1, 1, 1, 0},
            solution.dailyTemperatures(new int[]{100, 50, 60, 70, 80})
        );
    }

    @Test
    void lastElementAlwaysZero() {
        int[] temps = {30, 60, 90};
        int[] result = solution.dailyTemperatures(temps);
        assertEquals(0, result[result.length - 1]);
    }
}
