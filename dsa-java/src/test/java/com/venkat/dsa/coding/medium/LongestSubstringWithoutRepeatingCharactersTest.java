package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("practice — delete when you start")
public class LongestSubstringWithoutRepeatingCharactersTest {

    private final LongestSubstringWithoutRepeatingCharacters solution =
            new LongestSubstringWithoutRepeatingCharacters();

    @Test
    void example1_mixedRepeats() {
        assertEquals(3, solution.lengthOfLongestSubstring("abcabcbb"));
    }

    @Test
    void example2_allSame() {
        assertEquals(1, solution.lengthOfLongestSubstring("bbbbb"));
    }

    @Test
    void example3_allUnique() {
        assertEquals(3, solution.lengthOfLongestSubstring("pwwkew"));
    }
}
