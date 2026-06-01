package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class WordBreakTest {

    private final WordBreak solution = new WordBreak();

    @Test
    public void testLeetcode() {
        assertTrue(solution.wordBreak("leetcode", Arrays.asList("leet", "code")));
    }

    @Test
    public void testApplePenApple() {
        assertTrue(solution.wordBreak("applepenapple", Arrays.asList("apple", "pen")));
    }

    @Test
    public void testCatsandog() {
        assertFalse(solution.wordBreak("catsandog", Arrays.asList("cats", "dog", "sand", "and", "cat")));
    }
}
