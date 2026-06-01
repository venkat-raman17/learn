package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CheapestFlightsWithinKStopsTest {

    private final CheapestFlightsWithinKStops sol = new CheapestFlightsWithinKStops();

    @Test
    void testExample1() {
        // LeetCode example 1: src=0, dst=2, k=1
        // Path 0->1->2 costs 100+100=200 (1 stop) — within k=1
        int[][] flights = {{0, 1, 100}, {1, 2, 100}, {0, 2, 500}};
        assertEquals(200, sol.findCheapestPrice(3, flights, 0, 2, 1));
    }

    @Test
    void testExample2() {
        // LeetCode example 2: k=0 forces direct flight 0->2 costing 500
        int[][] flights = {{0, 1, 100}, {1, 2, 100}, {0, 2, 500}};
        assertEquals(500, sol.findCheapestPrice(3, flights, 0, 2, 0));
    }

    @Test
    void testNoPath() {
        // Destination unreachable within k stops
        int[][] flights = {{0, 1, 100}};
        assertEquals(-1, sol.findCheapestPrice(3, flights, 0, 2, 1));
    }

    @Test
    void testSourceEqualsDestination() {
        int[][] flights = {{0, 1, 100}};
        assertEquals(0, sol.findCheapestPrice(2, flights, 0, 0, 0));
    }

    @Test
    void testLongerPath() {
        // 4 nodes: cheaper path needs exactly 2 stops
        // 0->1->2->3 = 1+1+1 = 3, direct 0->3 = 10
        int[][] flights = {{0, 1, 1}, {1, 2, 1}, {2, 3, 1}, {0, 3, 10}};
        assertEquals(3, sol.findCheapestPrice(4, flights, 0, 3, 2));
    }

    @Test
    void testKStopsNotEnough() {
        // Cheaper path needs 2 stops but k=1, must take direct (cost 10)
        int[][] flights = {{0, 1, 1}, {1, 2, 1}, {2, 3, 1}, {0, 3, 10}};
        assertEquals(10, sol.findCheapestPrice(4, flights, 0, 3, 1));
    }

    @Test
    void testLeetCode787Example3() {
        // n=4, flights: 0->1=1, 0->2=5, 1->2=1, 2->3=1; src=0, dst=3, k=1
        // Paths within 1 stop: 0->2->3=6; k=1 allows 2 edges
        // 0->1->2 uses 1 stop but 2->3 would need another, so not reachable in k=1 via 0->1->2->3
        // Actually: 0->2->3 = 5+1 = 6 (1 stop = 2 edges, valid)
        int[][] flights = {{0, 1, 1}, {0, 2, 5}, {1, 2, 1}, {2, 3, 1}};
        assertEquals(6, sol.findCheapestPrice(4, flights, 0, 3, 1));
    }
}
