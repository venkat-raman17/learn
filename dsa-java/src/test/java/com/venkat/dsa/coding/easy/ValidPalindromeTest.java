package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class ValidPalindromeTest {

    private final ValidPalindrome solution = new ValidPalindrome();

    @Test
    void testValidPalindromeWithSpacesAndPunctuation() {
        assertTrue(solution.isPalindrome("A man, a plan, a canal: Panama"));
    }

    @Test
    void testNotAPalindrome() {
        assertFalse(solution.isPalindrome("race a car"));
    }

    @Test
    void testEmptyStringAfterFiltering() {
        assertTrue(solution.isPalindrome(" "));
    }
}
