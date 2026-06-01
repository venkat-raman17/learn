package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidPalindromeTest {

    private final ValidPalindrome sol = new ValidPalindrome();

    // Official LeetCode example 1: "A man, a plan, a canal: Panama" -> true
    @Test
    void officialExample1() {
        assertTrue(sol.isPalindrome("A man, a plan, a canal: Panama"));
    }

    // Official LeetCode example 2: "race a car" -> false
    @Test
    void officialExample2() {
        assertFalse(sol.isPalindrome("race a car"));
    }

    // Official LeetCode example 3: " " (space only) -> true (empty after filtering)
    @Test
    void officialExample3() {
        assertTrue(sol.isPalindrome(" "));
    }

    @Test
    void emptyString() {
        assertTrue(sol.isPalindrome(""));
    }

    @Test
    void singleAlphanumericChar() {
        assertTrue(sol.isPalindrome("a"));
        assertTrue(sol.isPalindrome("1"));
    }

    @Test
    void allNonAlphanumeric() {
        assertTrue(sol.isPalindrome("!!!"));
    }

    @Test
    void simpleNumericPalindrome() {
        assertTrue(sol.isPalindrome("121"));
    }

    @Test
    void mixedCasePalindrome() {
        assertTrue(sol.isPalindrome("AbBa"));
    }

    @Test
    void mixedCaseNotPalindrome() {
        assertFalse(sol.isPalindrome("AbCd"));
    }

    @Test
    void punctuationOnlyMeaningfulCharsArePalindrome() {
        // ".,a." -> only 'a' remains -> palindrome
        assertTrue(sol.isPalindrome(".,a."));
    }

    @Test
    void twoCharPalindrome() {
        assertTrue(sol.isPalindrome("aa"));
    }

    @Test
    void twoCharNotPalindrome() {
        assertFalse(sol.isPalindrome("ab"));
    }
}
