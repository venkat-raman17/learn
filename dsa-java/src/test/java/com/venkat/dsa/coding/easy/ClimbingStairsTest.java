package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class ClimbingStairsTest {

    private final ClimbingStairs solution = new ClimbingStairs();

    @Test
    public void testTwoSteps() {
        assertEquals(2, solution.climbStairs(2));
    }

    @Test
    public void testThreeSteps() {
        assertEquals(3, solution.climbStairs(3));
    }

    @Test
    public void testOneStep() {
        assertEquals(1, solution.climbStairs(1));
    }
}
