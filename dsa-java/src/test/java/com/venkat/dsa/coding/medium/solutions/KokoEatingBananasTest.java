package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KokoEatingBananasTest {

    private final KokoEatingBananas sol = new KokoEatingBananas();

    // --- Official LeetCode examples ---

    @Test
    void example1() {
        // piles=[3,6,7,11], h=8 => 4
        // At speed 4: ceil(3/4)=1, ceil(6/4)=2, ceil(7/4)=2, ceil(11/4)=3 => total=8 hours
        assertEquals(4, sol.minEatingSpeed(new int[]{3, 6, 7, 11}, 8));
    }

    @Test
    void example2() {
        // piles=[30,11,23,4,20], h=5 => 30
        // 5 piles, 5 hours => must eat each pile in exactly 1 hour => speed = max pile = 30
        assertEquals(30, sol.minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 5));
    }

    @Test
    void example3() {
        // piles=[30,11,23,4,20], h=6 => 23
        // At speed 23: ceil(30/23)=2, ceil(11/23)=1, ceil(23/23)=1, ceil(4/23)=1, ceil(20/23)=1 => total=6
        assertEquals(23, sol.minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 6));
    }

    // --- Edge cases ---

    @Test
    void singlePile_exactHours() {
        // piles=[10], h=10 => speed 1 (eat one banana per hour, 10 hours)
        assertEquals(1, sol.minEatingSpeed(new int[]{10}, 10));
    }

    @Test
    void singlePile_oneHour() {
        // piles=[10], h=1 => speed 10 (must eat all 10 in one hour)
        assertEquals(10, sol.minEatingSpeed(new int[]{10}, 1));
    }

    @Test
    void allSamePiles() {
        // piles=[4,4,4,4], h=4 => speed 4 (each pile takes exactly 1 hour)
        assertEquals(4, sol.minEatingSpeed(new int[]{4, 4, 4, 4}, 4));
    }

    @Test
    void largeH_allowsSpeed1() {
        // piles=[1,1,1,1], h=100 => speed 1
        assertEquals(1, sol.minEatingSpeed(new int[]{1, 1, 1, 1}, 100));
    }

    @Test
    void largePiles() {
        // piles=[1000000000], h=2 => 500000000
        // At speed 500000000: ceil(1000000000/500000000) = 2 hours
        assertEquals(500000000, sol.minEatingSpeed(new int[]{1000000000}, 2));
    }
}
