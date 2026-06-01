package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class NetworkDelayTimeTest {

    private final NetworkDelayTime solution = new NetworkDelayTime();

    @Test
    public void testExample1() {
        int[][] times = {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
        assertEquals(2, solution.networkDelayTime(times, 4, 2));
    }

    @Test
    public void testExample2() {
        int[][] times = {{1, 2, 1}};
        assertEquals(1, solution.networkDelayTime(times, 2, 1));
    }

    @Test
    public void testUnreachableNode() {
        int[][] times = {{1, 2, 1}};
        assertEquals(-1, solution.networkDelayTime(times, 2, 2));
    }
}
