package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * LeetCode #287 constraint: array of n+1 integers where each integer is in [1, n].
 * All test inputs satisfy this constraint so Floyd's implicit linked-list
 * traversal never accesses an out-of-bounds index.
 */
class FindTheDuplicateNumberTest {

    private final FindTheDuplicateNumber sol = new FindTheDuplicateNumber();

    @Test
    void example1_duplicateIs2() {
        // n=4, vals in [1,4]: [1,3,4,2,2] => 2
        assertEquals(2, sol.findDuplicate(new int[]{1, 3, 4, 2, 2}));
    }

    @Test
    void example2_duplicateIs3() {
        // n=4, vals in [1,4]: [3,1,3,4,2] => 3
        assertEquals(3, sol.findDuplicate(new int[]{3, 1, 3, 4, 2}));
    }

    @Test
    void duplicateIsLargest() {
        // n=4, vals in [1,4]: [1,2,3,4,4] => 4
        assertEquals(4, sol.findDuplicate(new int[]{1, 2, 3, 4, 4}));
    }

    @Test
    void duplicateIsSmallest() {
        // n=4, vals in [1,4]: [2,1,3,4,2] => 2
        assertEquals(2, sol.findDuplicate(new int[]{2, 1, 3, 4, 2}));
    }

    @Test
    void smallestCase_twoElements() {
        // n=1, vals in [1,1]: [1,1] => 1
        assertEquals(1, sol.findDuplicate(new int[]{1, 1}));
    }

    @Test
    void duplicateInLongerArray() {
        // n=6, vals in [1,6]: [1,2,3,4,5,6,5] => 5
        assertEquals(5, sol.findDuplicate(new int[]{1, 2, 3, 4, 5, 6, 5}));
    }

    @Test
    void duplicateAtFront() {
        // n=4, vals in [1,4]: [3,1,3,4,2] is already covered;
        // another: [1,3,4,1,2] => 1 (n=4)
        assertEquals(1, sol.findDuplicate(new int[]{1, 3, 4, 1, 2}));
    }
}
