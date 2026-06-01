package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class CheapestFlightsWithinKStopsTest {

    private final CheapestFlightsWithinKStops solution = new CheapestFlightsWithinKStops();

    @Test
    public void testExample1() {
        int[][] flights = {{0, 1, 100}, {1, 2, 100}, {2, 0, 100}, {1, 3, 600}, {2, 3, 200}};
        assertEquals(700, solution.findCheapestPrice(4, flights, 0, 3, 1));
    }

    @Test
    public void testExample2() {
        int[][] flights = {{0, 1, 100}, {1, 2, 100}, {0, 2, 500}};
        assertEquals(200, solution.findCheapestPrice(3, flights, 0, 2, 1));
    }

    @Test
    public void testNoRoute() {
        int[][] flights = {{0, 1, 100}};
        assertEquals(-1, solution.findCheapestPrice(3, flights, 0, 2, 1));
    }
}
