package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class HouseRobberIITest {

    private final HouseRobberII solution = new HouseRobberII();

    @Test
    public void testExample1() {
        assertEquals(3, solution.rob(new int[]{2, 3, 2}));
    }

    @Test
    public void testExample2() {
        assertEquals(4, solution.rob(new int[]{1, 2, 3, 1}));
    }

    @Test
    public void testSingleHouse() {
        assertEquals(3, solution.rob(new int[]{3}));
    }
}
