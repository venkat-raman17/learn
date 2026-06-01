package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class PartitionEqualSubsetSumTest {

    private final PartitionEqualSubsetSum solution = new PartitionEqualSubsetSum();

    @Test
    public void testExample1() {
        assertTrue(solution.canPartition(new int[]{1, 5, 11, 5}));
    }

    @Test
    public void testExample2() {
        assertFalse(solution.canPartition(new int[]{1, 2, 3, 5}));
    }

    @Test
    public void testOddSum() {
        assertFalse(solution.canPartition(new int[]{1, 2, 4}));
    }
}
