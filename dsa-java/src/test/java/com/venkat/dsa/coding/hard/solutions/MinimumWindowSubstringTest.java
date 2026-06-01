package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MinimumWindowSubstringTest {

    private final MinimumWindowSubstring solution = new MinimumWindowSubstring();

    // Official LeetCode examples
    @Test
    void example1_ADOBECODEBANC() {
        assertEquals("BANC", solution.minWindow("ADOBECODEBANC", "ABC"));
    }

    @Test
    void example2_singleChar_match() {
        assertEquals("a", solution.minWindow("a", "a"));
    }

    @Test
    void example3_noSolution() {
        // t="a" is not in s="b"
        assertEquals("", solution.minWindow("a", "b"));
    }

    // Edge cases
    @Test
    void tLongerThanS() {
        assertEquals("", solution.minWindow("ab", "abc"));
    }

    @Test
    void exactMatch() {
        // s == t → answer is s itself
        assertEquals("abc", solution.minWindow("abc", "abc"));
    }

    @Test
    void tHasDuplicates() {
        // t="aa" needs two a's; "aab" has them at indices 0-1
        assertEquals("aa", solution.minWindow("aab", "aa"));
    }

    @Test
    void windowAtEnd() {
        // "dcba" with t="ba" → "ba" at end
        assertEquals("ba", solution.minWindow("dcba", "ba"));
    }

    @Test
    void windowAtStart() {
        assertEquals("ab", solution.minWindow("abcd", "ab"));
    }

    @Test
    void multipleValidWindows() {
        // "ADOBECODEBANC" t="ABC" — verified answer "BANC" in example1
        // Extra: "xyz" t="x" → "x"
        assertEquals("x", solution.minWindow("xyz", "x"));
    }

    @Test
    void repeatingCharsInS() {
        // s="aaaaaaaaaaaabbbbbcdd", t="abcdd"
        // Need: a=1,b=1,c=1,d=2. The shortest window containing all is "bbbbbcdd" (end)
        // length 8, or we can find "abbbbbcdd" = 9 chars from 'a' at index 11 onward?
        // Let's trace: s = "aaaaaaaaaaaabbbbbcdd"
        // indices: a(0-11), b(12-16), c(17), d(18), d(19)
        // smallest window = from index 11 ('a') to 19 ('d') = "abbbbbcdd" length 9
        // Actually from any one 'a' to the last 'd': shortest is index 11..19 = 9 chars
        assertEquals(9, solution.minWindow("aaaaaaaaaaaabbbbbcdd", "abcdd").length());
    }

    @Test
    void emptyS() {
        assertEquals("", solution.minWindow("", "a"));
    }

    @Test
    void emptyT() {
        assertEquals("", solution.minWindow("abc", ""));
    }
}
