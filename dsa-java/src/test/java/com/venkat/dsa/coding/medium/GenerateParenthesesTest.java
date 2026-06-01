package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class GenerateParenthesesTest {

    private final GenerateParentheses solution = new GenerateParentheses();

    @Test
    public void testN1() {
        List<String> result = solution.generateParenthesis(1);
        assertEquals(1, result.size());
        assertTrue(result.contains("()"));
    }

    @Test
    public void testN3Size() {
        List<String> result = solution.generateParenthesis(3);
        assertEquals(5, result.size());
    }

    @Test
    public void testN3Contents() {
        List<String> result = solution.generateParenthesis(3);
        assertTrue(result.containsAll(List.of("((()))", "(()())", "(())()", "()(())", "()()()")));
    }
}
