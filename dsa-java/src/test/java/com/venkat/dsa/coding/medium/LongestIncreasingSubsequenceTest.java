package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class LongestIncreasingSubsequenceTest {

    private final LongestIncreasingSubsequence solution = new LongestIncreasingSubsequence();

    @Test
    public void testExample1() {
        assertEquals(4, solution.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
    }

    @Test
    public void testExample2() {
        assertEquals(4, solution.lengthOfLIS(new int[]{0, 1, 0, 3, 2, 3}));
    }

    @Test
    public void testAllSame() {
        assertEquals(1, solution.lengthOfLIS(new int[]{7, 7, 7, 7}));
    }
}
