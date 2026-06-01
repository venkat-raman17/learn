package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class JumpGameTest {

    private final JumpGame solution = new JumpGame();

    @Test
    void testCanReach() {
        assertTrue(solution.canJump(new int[]{2, 3, 1, 1, 4}));
    }

    @Test
    void testCannotReach() {
        assertFalse(solution.canJump(new int[]{3, 2, 1, 0, 4}));
    }

    @Test
    void testSingleElement() {
        assertTrue(solution.canJump(new int[]{0}));
    }
}
