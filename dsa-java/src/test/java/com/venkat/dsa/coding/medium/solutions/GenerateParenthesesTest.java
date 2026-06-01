package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class GenerateParenthesesTest {

    private final GenerateParentheses solution = new GenerateParentheses();

    // Official LeetCode examples
    @Test
    void example1_n3() {
        List<String> result = solution.generateParenthesis(3);
        // Catalan(3) = 5 combinations
        assertEquals(5, result.size());
        Set<String> expected = Set.of("((()))", "(()())", "(())()", "()(())", "()()()");
        assertEquals(expected, new HashSet<>(result));
    }

    @Test
    void example2_n1() {
        List<String> result = solution.generateParenthesis(1);
        assertEquals(1, result.size());
        assertEquals("()", result.get(0));
    }

    // Edge / additional cases
    @Test
    void n2_twoResults() {
        List<String> result = solution.generateParenthesis(2);
        assertEquals(2, result.size());
        Set<String> expected = Set.of("(())", "()()");
        assertEquals(expected, new HashSet<>(result));
    }

    @Test
    void n4_fourteenResults() {
        // Catalan(4) = 14
        List<String> result = solution.generateParenthesis(4);
        assertEquals(14, result.size());
    }

    @Test
    void allStringsHaveCorrectLength() {
        int n = 3;
        for (String s : solution.generateParenthesis(n)) {
            assertEquals(2 * n, s.length());
        }
    }

    @Test
    void allStringsAreValid() {
        for (String s : solution.generateParenthesis(4)) {
            assertTrue(isValidParentheses(s), "Invalid combination: " + s);
        }
    }

    @Test
    void noDuplicates() {
        List<String> result = solution.generateParenthesis(4);
        assertEquals(result.size(), new HashSet<>(result).size());
    }

    // Helper: validate a parentheses string
    private boolean isValidParentheses(String s) {
        int count = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') count++;
            else count--;
            if (count < 0) return false;
        }
        return count == 0;
    }
}
