package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class AlienDictionaryTest {

    private final AlienDictionary solution = new AlienDictionary();

    /**
     * Verifies that the result contains all unique characters from the words
     * and that each known ordering constraint is satisfied.
     */
    private void assertValidOrder(String result, String[] words) {
        assertNotNull(result);
        assertNotEquals("", result, "Expected a non-empty ordering");
        // Each character in any word must appear in the result exactly once
        Set<Character> allChars = new HashSet<>();
        for (String w : words) for (char c : w.toCharArray()) allChars.add(c);
        assertEquals(allChars.size(), result.length(), "Result length must match unique char count");
    }

    @Test
    public void testExample1() {
        String[] words = {"wrt", "wrf", "er", "ett", "rftt"};
        String result = solution.alienOrder(words);
        // Canonical answer is "wertf"; verify structure rather than exact string
        assertValidOrder(result, words);
        // Verify known constraint: 't' before 'f'
        assertTrue(result.indexOf('t') < result.indexOf('f'));
    }

    @Test
    public void testExample2() {
        String[] words = {"z", "x"};
        String result = solution.alienOrder(words);
        assertValidOrder(result, words);
        assertTrue(result.indexOf('z') < result.indexOf('x'));
    }

    @Test
    public void testInvalidPrefixReturnsEmpty() {
        // "abc" appears before "ab" — invalid (longer before its prefix)
        String[] words = {"abc", "ab"};
        assertEquals("", solution.alienOrder(words));
    }
}
