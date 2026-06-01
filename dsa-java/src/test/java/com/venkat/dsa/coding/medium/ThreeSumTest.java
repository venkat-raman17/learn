package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class ThreeSumTest {

    private final ThreeSum solution = new ThreeSum();

    @Test
    void testTwoTriplets() {
        List<List<Integer>> result = solution.threeSum(new int[]{-1, 0, 1, 2, -1, -4});
        assertEquals(2, result.size());
        assertTrue(result.contains(List.of(-1, -1, 2)));
        assertTrue(result.contains(List.of(-1, 0, 1)));
    }

    @Test
    void testNoTriplet() {
        List<List<Integer>> result = solution.threeSum(new int[]{0, 1, 1});
        assertTrue(result.isEmpty());
    }

    @Test
    void testAllZeros() {
        List<List<Integer>> result = solution.threeSum(new int[]{0, 0, 0});
        assertEquals(1, result.size());
        assertTrue(result.contains(List.of(0, 0, 0)));
    }
}
