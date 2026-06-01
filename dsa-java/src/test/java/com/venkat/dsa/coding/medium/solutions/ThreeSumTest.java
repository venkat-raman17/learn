package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ThreeSumTest {

    private final ThreeSum sol = new ThreeSum();

    /** Sorts each inner triplet and the outer list for order-independent comparison. */
    private List<List<Integer>> normalize(List<List<Integer>> lists) {
        lists.forEach(Collections::sort);
        lists.sort((a, b) -> {
            for (int i = 0; i < a.size(); i++) {
                int cmp = Integer.compare(a.get(i), b.get(i));
                if (cmp != 0) return cmp;
            }
            return 0;
        });
        return lists;
    }

    // Official LeetCode example 1: [-1,0,1,2,-1,-4] -> [[-1,-1,2],[-1,0,1]]
    @Test
    void officialExample1() {
        List<List<Integer>> result = normalize(sol.threeSum(new int[]{-1, 0, 1, 2, -1, -4}));
        List<List<Integer>> expected = normalize(Arrays.asList(
                Arrays.asList(-1, -1, 2),
                Arrays.asList(-1, 0, 1)
        ));
        assertEquals(expected, result);
    }

    // Official LeetCode example 2: [0,1,1] -> []
    @Test
    void officialExample2() {
        assertTrue(sol.threeSum(new int[]{0, 1, 1}).isEmpty());
    }

    // Official LeetCode example 3: [0,0,0] -> [[0,0,0]]
    @Test
    void officialExample3() {
        List<List<Integer>> result = sol.threeSum(new int[]{0, 0, 0});
        assertEquals(1, result.size());
        assertEquals(Arrays.asList(0, 0, 0), result.get(0));
    }

    @Test
    void allSameNonZero() {
        // [1,1,1] -> no valid triplet
        assertTrue(sol.threeSum(new int[]{1, 1, 1}).isEmpty());
    }

    @Test
    void multipleDuplicateTriplets() {
        // [-2,0,0,2,2] -> [[-2,0,2]]
        List<List<Integer>> result = normalize(sol.threeSum(new int[]{-2, 0, 0, 2, 2}));
        List<List<Integer>> expected = normalize(
                Arrays.asList(Arrays.asList(-2, 0, 2))
        );
        assertEquals(expected, result);
    }

    @Test
    void noTriplets() {
        assertTrue(sol.threeSum(new int[]{1, 2, 3}).isEmpty());
    }

    @Test
    void singleZeroTriplet() {
        List<List<Integer>> result = sol.threeSum(new int[]{-4, -2, -2, -2, 0, 1, 2, 2, 2, 3, 3, 4, 4, 6, 6});
        // Should not throw and must not contain duplicates
        assertNotNull(result);
        // Verify no duplicate triplets
        List<List<Integer>> normalized = normalize(result);
        for (int i = 1; i < normalized.size(); i++) {
            assertNotEquals(normalized.get(i - 1), normalized.get(i),
                    "Duplicate triplet found at index " + i);
        }
    }

    @Test
    void smallArrayTwoElements() {
        // Only 2 elements: can't form a triplet
        assertTrue(sol.threeSum(new int[]{0, 0}).isEmpty());
    }

    @Test
    void allPositive() {
        // [1,2,3,4] -> no valid triplet (all positive, sum > 0)
        assertTrue(sol.threeSum(new int[]{1, 2, 3, 4}).isEmpty());
    }

    @Test
    void allNegative() {
        // [-4,-3,-2,-1] -> no valid triplet (all negative, sum < 0)
        assertTrue(sol.threeSum(new int[]{-4, -3, -2, -1}).isEmpty());
    }
}
