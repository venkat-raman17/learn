package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class JumpGameIITest {

    private final JumpGameII solution = new JumpGameII();

    @Test
    void testExample1() {
        assertEquals(2, solution.jump(new int[]{2, 3, 1, 1, 4}));
    }

    @Test
    void testExample2() {
        assertEquals(2, solution.jump(new int[]{2, 3, 0, 1, 4}));
    }

    @Test
    void testSingleElement() {
        assertEquals(0, solution.jump(new int[]{0}));
    }
}
