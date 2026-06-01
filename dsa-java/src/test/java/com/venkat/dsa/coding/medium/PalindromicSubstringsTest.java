package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class PalindromicSubstringsTest {

    private final PalindromicSubstrings solution = new PalindromicSubstrings();

    @Test
    public void testAbc() {
        assertEquals(3, solution.countSubstrings("abc"));
    }

    @Test
    public void testAaa() {
        assertEquals(6, solution.countSubstrings("aaa"));
    }

    @Test
    public void testSingleChar() {
        assertEquals(1, solution.countSubstrings("a"));
    }
}
