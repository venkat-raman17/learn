package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class UniquePathsTest {

    private final UniquePaths solution = new UniquePaths();

    @Test
    void testExample1() {
        assertEquals(28, solution.uniquePaths(3, 7));
    }

    @Test
    void testExample2() {
        assertEquals(3, solution.uniquePaths(3, 2));
    }

    @Test
    void testSingleCell() {
        assertEquals(1, solution.uniquePaths(1, 1));
    }
}
