package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MergeTripletsToFormTargetTripletTest {

    private final MergeTripletsToFormTargetTriplet solution = new MergeTripletsToFormTargetTriplet();

    @Test
    void example1_possible() {
        // triplets=[[2,5,3],[1,8,4],[1,7,5]], target=[2,7,5]
        // [2,5,3] valid (none exceeds [2,7,5]): max so far [2,5,3]
        // [1,8,4] invalid (8>7): skip
        // [1,7,5] valid: max [2,7,5] == target -> true
        assertTrue(solution.mergeTriplets(
                new int[][]{{2, 5, 3}, {1, 8, 4}, {1, 7, 5}},
                new int[]{2, 7, 5}));
    }

    @Test
    void example2_impossible() {
        // triplets=[[3,4,5],[4,5,6]], target=[3,2,5]
        // [3,4,5] invalid (4>2): skip
        // [4,5,6] invalid (4>3): skip
        // nothing merged -> [0,0,0] != [3,2,5]
        assertFalse(solution.mergeTriplets(
                new int[][]{{3, 4, 5}, {4, 5, 6}},
                new int[]{3, 2, 5}));
    }

    @Test
    void singleTripletEqualsTarget() {
        assertTrue(solution.mergeTriplets(
                new int[][]{{1, 2, 3}},
                new int[]{1, 2, 3}));
    }

    @Test
    void singleTripletBelowTarget() {
        assertFalse(solution.mergeTriplets(
                new int[][]{{1, 1, 1}},
                new int[]{1, 2, 1}));
    }

    @Test
    void allTripletsInvalid() {
        assertFalse(solution.mergeTriplets(
                new int[][]{{5, 5, 5}, {6, 6, 6}},
                new int[]{4, 4, 4}));
    }

    @Test
    void needMultipleTriplets() {
        // target=[3,3,3]; no single triplet covers all three components
        // [3,1,1] + [1,3,1] + [1,1,3] -> max=[3,3,3]
        assertTrue(solution.mergeTriplets(
                new int[][]{{3, 1, 1}, {1, 3, 1}, {1, 1, 3}},
                new int[]{3, 3, 3}));
    }

    @Test
    void extraValidTripletsDoNotHurt() {
        // Having extra valid triplets below target is fine
        assertTrue(solution.mergeTriplets(
                new int[][]{{1, 1, 1}, {2, 2, 2}, {3, 3, 3}},
                new int[]{3, 3, 3}));
    }
}
