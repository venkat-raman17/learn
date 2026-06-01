package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class RedundantConnectionTest {

    private final RedundantConnection solution = new RedundantConnection();

    @Test
    public void testExample1() {
        assertArrayEquals(new int[]{2,3},
            solution.findRedundantConnection(new int[][]{{1,2},{1,3},{2,3}}));
    }

    @Test
    public void testExample2() {
        assertArrayEquals(new int[]{1,4},
            solution.findRedundantConnection(new int[][]{{1,2},{2,3},{3,4},{1,4},{1,5}}));
    }

    @Test
    public void testThreeNodeCycle() {
        assertArrayEquals(new int[]{1,3},
            solution.findRedundantConnection(new int[][]{{1,2},{2,3},{1,3}}));
    }
}
