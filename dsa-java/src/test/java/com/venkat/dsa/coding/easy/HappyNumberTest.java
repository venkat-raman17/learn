package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class HappyNumberTest {

    private final HappyNumber solution = new HappyNumber();

    @Test
    public void testHappyNumber19() {
        assertTrue(solution.isHappy(19));
    }

    @Test
    public void testUnhappyNumber2() {
        assertFalse(solution.isHappy(2));
    }

    @Test
    public void testHappyNumber1() {
        assertTrue(solution.isHappy(1));
    }
}
