package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class HouseRobberTest {

    private final HouseRobber solution = new HouseRobber();

    @Test
    public void testExample1() {
        assertEquals(4, solution.rob(new int[]{1, 2, 3, 1}));
    }

    @Test
    public void testExample2() {
        assertEquals(12, solution.rob(new int[]{2, 7, 9, 3, 1}));
    }

    @Test
    public void testSingleHouse() {
        assertEquals(5, solution.rob(new int[]{5}));
    }
}
