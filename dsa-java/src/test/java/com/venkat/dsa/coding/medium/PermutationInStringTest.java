package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("practice — delete when you start")
public class PermutationInStringTest {

    private final PermutationInString solution = new PermutationInString();

    @Test
    void example1_permutationPresent() {
        assertTrue(solution.checkInclusion("ab", "eidbaooo"));
    }

    @Test
    void example2_permutationAbsent() {
        assertFalse(solution.checkInclusion("ab", "eidboaoo"));
    }

    @Test
    void s1LongerThanS2() {
        assertFalse(solution.checkInclusion("hello", "hi"));
    }
}
