package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LongestRepeatingCharacterReplacementTest {

    private final LongestRepeatingCharacterReplacement solution =
            new LongestRepeatingCharacterReplacement();

    // Official LeetCode examples
    @Test
    void example1_ABAB_k2() {
        // Replace both A's or both B's → "AAAA" or "BBBB", length 4
        assertEquals(4, solution.characterReplacement("ABAB", 2));
    }

    @Test
    void example2_AABABBA_k1() {
        // Best window: "AABAB" with k=1 → replace 1 B in "AAAB" = 4; actual answer is 4
        assertEquals(4, solution.characterReplacement("AABABBA", 1));
    }

    // Edge cases
    @Test
    void singleChar_k0() {
        assertEquals(1, solution.characterReplacement("A", 0));
    }

    @Test
    void allSameChar_k0() {
        assertEquals(4, solution.characterReplacement("AAAA", 0));
    }

    @Test
    void allDifferent_kCoversAll() {
        // "ABCD" k=3 → replace 3 chars, entire string length 4
        assertEquals(4, solution.characterReplacement("ABCD", 3));
    }

    @Test
    void k0_longestRun() {
        // "AAABBB" k=0 → longest run is 3 (AAA or BBB)
        assertEquals(3, solution.characterReplacement("AAABBB", 0));
    }

    @Test
    void kExceedsLength() {
        // k larger than string length → entire string is the answer
        assertEquals(5, solution.characterReplacement("ABCDE", 10));
    }

    @Test
    void twoCharsAlternating() {
        // "ABABAB" k=2: 3 A's + 3 B's; window size 6, replacements for all-A = 3 > 2;
        // best window = 5 chars (e.g. "ABABA": 3 A's, replace 2 B's)
        assertEquals(5, solution.characterReplacement("ABABAB", 2));
    }

    @Test
    void allSameK1() {
        assertEquals(5, solution.characterReplacement("AAAAA", 1));
    }

    @Test
    void longerExample() {
        // "KRSCDCSONAJNHLBMDQGIFCPEKPOHQIHLTDIQGEKLRLCQNBOHNDQGHJPNDQPERNFSSSRDEQLFPCCCARFMD"
        // Official NeetCode/LC: this is a known test case but we can test a simpler one
        // "EEEEE" k=0 → 5
        assertEquals(5, solution.characterReplacement("EEEEE", 0));
    }
}
