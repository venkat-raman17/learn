package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class BurstBalloonsTest {

    private final BurstBalloons solution = new BurstBalloons();

    @Test
    void testExample1() {
        assertEquals(167, solution.maxCoins(new int[]{3, 1, 5, 8}));
    }

    @Test
    void testExample2() {
        assertEquals(10, solution.maxCoins(new int[]{1, 5}));
    }

    @Test
    void testSingleBalloon() {
        assertEquals(1, solution.maxCoins(new int[]{1}));
    }
}
