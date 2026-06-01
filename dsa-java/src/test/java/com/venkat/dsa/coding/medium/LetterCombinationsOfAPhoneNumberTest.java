package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class LetterCombinationsOfAPhoneNumberTest {

    private final LetterCombinationsOfAPhoneNumber solution = new LetterCombinationsOfAPhoneNumber();

    @Test
    public void testLetterCombinations_twoDigits() {
        List<String> result = solution.letterCombinations("23");
        assertNotNull(result);
        assertEquals(9, result.size());
        assertTrue(result.contains("ad"));
        assertTrue(result.contains("bf"));
        assertTrue(result.contains("cf"));
    }

    @Test
    public void testLetterCombinations_empty() {
        List<String> result = solution.letterCombinations("");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLetterCombinations_singleDigit() {
        List<String> result = solution.letterCombinations("2");
        assertEquals(3, result.size());
        assertTrue(result.containsAll(List.of("a", "b", "c")));
    }
}
