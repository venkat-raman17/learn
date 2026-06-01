package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class CarFleetTest {

    private final CarFleet solution = new CarFleet();

    @Test
    public void testExample1() {
        assertEquals(3, solution.carFleet(12, new int[]{10, 8, 0, 5, 3}, new int[]{2, 4, 1, 1, 3}));
    }

    @Test
    public void testSingleCar() {
        assertEquals(1, solution.carFleet(10, new int[]{3}, new int[]{3}));
    }

    @Test
    public void testAllMerge() {
        // Both cars arrive at the same time -> 1 fleet
        assertEquals(1, solution.carFleet(10, new int[]{0, 5}, new int[]{2, 1}));
    }
}
