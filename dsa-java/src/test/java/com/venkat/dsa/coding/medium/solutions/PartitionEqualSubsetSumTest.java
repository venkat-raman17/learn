package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PartitionEqualSubsetSumTest {

    private final PartitionEqualSubsetSum solution = new PartitionEqualSubsetSum();

    @Test
    void example1() {
        // [1,5,11,5] -> total=22, target=11 -> [1,5,5] and [11] -> true
        assertTrue(solution.canPartition(new int[]{1, 5, 11, 5}));
    }

    @Test
    void example2_impossible() {
        // [1,2,3,5] -> total=11 (odd) -> false
        assertFalse(solution.canPartition(new int[]{1, 2, 3, 5}));
    }

    @Test
    void twoEqualElements() {
        // [3,3] -> each partition = [3] -> true
        assertTrue(solution.canPartition(new int[]{3, 3}));
    }

    @Test
    void twoUnequalElements() {
        // [1,2] -> total=3 (odd) -> false
        assertFalse(solution.canPartition(new int[]{1, 2}));
    }

    @Test
    void allSame_evenCount() {
        // [4,4,4,4] -> total=16, target=8 -> [4,4] each side -> true
        assertTrue(solution.canPartition(new int[]{4, 4, 4, 4}));
    }

    @Test
    void largerArray_possible() {
        // [2,2,3,5] -> total=12, target=6 -> [1,5] no, [2,4] no... [3+2+1?]
        // [2,2,3,5]: possible subsets summing to 6: {2,2} no (=4), {3,2,1} no,
        // {5,1} no, {2,4} no, {3,3} no. Actually let's check {2+4}=no, {3+2}=5 no wait:
        // elements: 2,2,3,5. sum=12, target=6. subsets: {2,4}no. {2,2,?} 2+2=4 need 2 more
        //   but remaining is {3,5} = no extra 2. {3,3}=no. Actually {2+4}=no. Hmm.
        // Wait: we need subset from {2,2,3,5} summing to 6.
        //   Try: 2+2=4(no), 3+2=5(no), 5+1=no, 2+3=5(no), 5+2=7(no), 2+2+3=7(no), 3+3=no
        //   Actually none sum to 6 from these elements. So answer is false.
        assertFalse(solution.canPartition(new int[]{2, 2, 3, 5}));
    }

    @Test
    void largerArray_trueCase() {
        // [1,2,3,4,5,6,7] -> total=28, target=14 -> [1,6,7] or [3,4,7] etc. -> true
        assertTrue(solution.canPartition(new int[]{1, 2, 3, 4, 5, 6, 7}));
    }

    @Test
    void singleElement_impossible() {
        // [1] -> total=1 (odd) -> false
        assertFalse(solution.canPartition(new int[]{1}));
    }
}
