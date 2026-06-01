package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class GasStationTest {

    private final GasStation solution = new GasStation();

    @Test
    void testExample1() {
        assertEquals(3, solution.canCompleteCircuit(
                new int[]{1, 2, 3, 4, 5},
                new int[]{3, 4, 5, 1, 2}));
    }

    @Test
    void testNoSolution() {
        assertEquals(-1, solution.canCompleteCircuit(
                new int[]{2, 3, 4},
                new int[]{3, 4, 3}));
    }

    @Test
    void testSingleStation() {
        assertEquals(0, solution.canCompleteCircuit(
                new int[]{5},
                new int[]{4}));
    }
}
