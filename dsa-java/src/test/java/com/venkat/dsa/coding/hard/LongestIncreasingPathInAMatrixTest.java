package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class LongestIncreasingPathInAMatrixTest {

    private final LongestIncreasingPathInAMatrix solution = new LongestIncreasingPathInAMatrix();

    @Test
    void testExample1() {
        assertEquals(4, solution.longestIncreasingPath(new int[][]{{9, 9, 4}, {6, 6, 8}, {2, 1, 1}}));
    }

    @Test
    void testExample2() {
        assertEquals(4, solution.longestIncreasingPath(new int[][]{{3, 4, 5}, {3, 2, 6}, {2, 2, 1}}));
    }

    @Test
    void testSingleCell() {
        assertEquals(1, solution.longestIncreasingPath(new int[][]{{1}}));
    }
}
