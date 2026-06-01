package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class MergeTripletsToFormTargetTripletTest {

    private final MergeTripletsToFormTargetTriplet solution = new MergeTripletsToFormTargetTriplet();

    @Test
    void testExample1() {
        assertTrue(solution.mergeTriplets(
                new int[][]{{2, 5, 3}, {1, 8, 4}, {1, 7, 5}},
                new int[]{2, 7, 5}));
    }

    @Test
    void testExample2() {
        assertFalse(solution.mergeTriplets(
                new int[][]{{3, 4, 5}, {4, 5, 6}},
                new int[]{3, 2, 5}));
    }

    @Test
    void testExactMatch() {
        assertTrue(solution.mergeTriplets(
                new int[][]{{1, 2, 3}},
                new int[]{1, 2, 3}));
    }
}
