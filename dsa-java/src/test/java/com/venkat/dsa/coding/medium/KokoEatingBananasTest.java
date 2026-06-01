package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class KokoEatingBananasTest {

    private final KokoEatingBananas solution = new KokoEatingBananas();

    @Test
    public void testExample1() {
        assertEquals(4, solution.minEatingSpeed(new int[]{3, 6, 7, 11}, 8));
    }

    @Test
    public void testExample2() {
        assertEquals(30, solution.minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 5));
    }

    @Test
    public void testSinglePile() {
        assertEquals(3, solution.minEatingSpeed(new int[]{3}, 3));
    }
}
