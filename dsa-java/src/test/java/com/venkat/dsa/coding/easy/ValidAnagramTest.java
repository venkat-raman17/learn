package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class ValidAnagramTest {

    private final ValidAnagram solution = new ValidAnagram();

    @Test
    public void testValidAnagram() {
        assertTrue(solution.isAnagram("anagram", "nagaram"));
    }

    @Test
    public void testNotAnagram() {
        assertFalse(solution.isAnagram("rat", "car"));
    }

    @Test
    public void testDifferentLengths() {
        assertFalse(solution.isAnagram("a", "ab"));
    }
}
