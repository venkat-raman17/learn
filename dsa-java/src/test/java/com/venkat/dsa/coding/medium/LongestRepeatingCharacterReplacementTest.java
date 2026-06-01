package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("practice — delete when you start")
public class LongestRepeatingCharacterReplacementTest {

    private final LongestRepeatingCharacterReplacement solution =
            new LongestRepeatingCharacterReplacement();

    @Test
    void example1_alternating() {
        assertEquals(4, solution.characterReplacement("ABAB", 2));
    }

    @Test
    void example2_mixed() {
        assertEquals(4, solution.characterReplacement("AABABBA", 1));
    }

    @Test
    void noReplacements() {
        assertEquals(2, solution.characterReplacement("AABB", 0));
    }
}
