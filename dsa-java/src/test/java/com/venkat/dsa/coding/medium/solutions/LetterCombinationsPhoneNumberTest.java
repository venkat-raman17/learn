package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LetterCombinationsPhoneNumberTest {

    private final LetterCombinationsPhoneNumber solution = new LetterCombinationsPhoneNumber();

    @Test
    void example1_twoDigits() {
        // "23" -> ["ad","ae","af","bd","be","bf","cd","ce","cf"]
        List<String> result = solution.letterCombinations("23");
        assertEquals(9, result.size());
        assertTrue(result.contains("ad"));
        assertTrue(result.contains("ae"));
        assertTrue(result.contains("af"));
        assertTrue(result.contains("bd"));
        assertTrue(result.contains("be"));
        assertTrue(result.contains("bf"));
        assertTrue(result.contains("cd"));
        assertTrue(result.contains("ce"));
        assertTrue(result.contains("cf"));
    }

    @Test
    void example2_emptyInput() {
        List<String> result = solution.letterCombinations("");
        assertTrue(result.isEmpty());
    }

    @Test
    void example3_singleDigit() {
        // "2" -> ["a","b","c"]
        List<String> result = solution.letterCombinations("2");
        assertEquals(3, result.size());
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
        assertTrue(result.contains("c"));
    }

    @Test
    void digitWith4Letters() {
        // "7" maps to "pqrs" -> 4 combinations
        List<String> result = solution.letterCombinations("7");
        assertEquals(4, result.size());
        assertTrue(result.contains("p"));
        assertTrue(result.contains("q"));
        assertTrue(result.contains("r"));
        assertTrue(result.contains("s"));
    }

    @Test
    void threeDigits_countOnly() {
        // "234" -> 3*3*3 = 27 combinations
        List<String> result = solution.letterCombinations("234");
        assertEquals(27, result.size());
    }

    @Test
    void allCombinationsHaveCorrectLength() {
        List<String> result = solution.letterCombinations("23");
        for (String combo : result) {
            assertEquals(2, combo.length());
        }
    }
}
