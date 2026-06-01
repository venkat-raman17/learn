package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BurstBalloonsTest {

    private final BurstBalloons sol = new BurstBalloons();

    // LeetCode example 1: [3,1,5,8] -> 167
    // Burst 1: 3*1*5=15, then [3,5,8]
    // Burst 5: 3*5*8=120, then [3,8]
    // Burst 3: 1*3*8=24, then [8]
    // Burst 8: 1*8*1=8 => 15+120+24+8=167
    @Test
    void example1() {
        assertEquals(167, sol.maxCoins(new int[]{3, 1, 5, 8}));
    }

    // LeetCode example 2: [1,5] -> 10
    // Burst 1: 1*1*5=5, then [5]; Burst 5: 1*5*1=5 => 10
    // Or burst 5 first: 1*5*1=5, then [1]; Burst 1: 1*1*1=1 => 6
    // Max = 10
    @Test
    void example2() {
        assertEquals(10, sol.maxCoins(new int[]{1, 5}));
    }

    // Single balloon: 1*val*1 = val
    @Test
    void singleBalloon() {
        assertEquals(7, sol.maxCoins(new int[]{7}));
    }

    // Single balloon value 1
    @Test
    void singleOne() {
        assertEquals(1, sol.maxCoins(new int[]{1}));
    }

    // All ones: [1,1,1] -> burst order matters
    // Burst middle first: 1*1*1=1, then [1,1]
    // Burst either: 1*1*1=1, then [1]: 1*1*1=1 => total 3
    @Test
    void allOnes() {
        assertEquals(3, sol.maxCoins(new int[]{1, 1, 1}));
    }

    // Two balloons equal: [2,2] -> max(burst1first, burst2first)
    // Burst 1st: 1*2*2=4, then [2]: 1*2*1=2 => 6
    // Burst 2nd: 2*2*1=4, then [2]: 1*2*1=2 => 6
    @Test
    void twoEqual() {
        assertEquals(6, sol.maxCoins(new int[]{2, 2}));
    }
}
