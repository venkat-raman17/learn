package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HappyNumberTest {

    private final HappyNumber sol = new HappyNumber();

    @Test
    void example1_happy19() {
        // 1^2+9^2=82 -> 8^2+2^2=68 -> 6^2+8^2=100 -> 1^2+0+0=1 => happy
        assertTrue(sol.isHappy(19));
    }

    @Test
    void example2_unhappy2() {
        assertFalse(sol.isHappy(2));
    }

    @Test
    void happy1() {
        // 1 is trivially happy
        assertTrue(sol.isHappy(1));
    }

    @Test
    void happy7() {
        assertTrue(sol.isHappy(7));
    }

    @Test
    void unhappy4() {
        assertFalse(sol.isHappy(4));
    }

    @Test
    void unhappy11() {
        // 1+1=2 -> enters unhappy cycle
        assertFalse(sol.isHappy(11));
    }

    @Test
    void largeHappy100() {
        // 1+0+0=1 => happy
        assertTrue(sol.isHappy(100));
    }
}
