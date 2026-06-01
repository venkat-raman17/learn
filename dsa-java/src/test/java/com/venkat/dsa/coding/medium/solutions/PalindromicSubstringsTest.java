package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PalindromicSubstringsTest {

    private final PalindromicSubstrings solution = new PalindromicSubstrings();

    @Test
    void example1() {
        // "abc" -> "a","b","c" = 3
        assertEquals(3, solution.countSubstrings("abc"));
    }

    @Test
    void example2() {
        // "aaa" -> "a","a","a","aa","aa","aaa" = 6
        assertEquals(6, solution.countSubstrings("aaa"));
    }

    @Test
    void singleChar() {
        assertEquals(1, solution.countSubstrings("a"));
    }

    @Test
    void twoSameChars() {
        // "aa" -> "a","a","aa" = 3
        assertEquals(3, solution.countSubstrings("aa"));
    }

    @Test
    void palindromeInput() {
        // "racecar": expand-around-center trace:
        // i=0 'r': odd=1 (r); even=0 -> +1
        // i=1 'a': odd=1 (a); even=0 -> +1
        // i=2 'c': odd=1 (c); even=0 -> +1
        // i=3 'e': odd: e(1), cec(1), aceca(1), racecar(1) = 4; even=0 -> +4  (wait: c==c, a==a, r==r -> 3 expansions = 4 palindromes)
        // Wait recount i=3: center=e; expand: (3,3)->e=1, (2,4)->cec=1, (1,5)->aceca=1, (0,6)->racecar=1 = 4 total
        // i=4 'c': odd=1 (c); even=0 -> +1
        // i=5 'a': odd=1 (a); even=0 -> +1
        // i=6 'r': odd=1 (r); even=0 -> +1
        // Total = 1+1+1+4+1+1+1 = 10
        assertEquals(10, solution.countSubstrings("racecar"));
    }

    @Test
    void mixedString() {
        // "abba": a,b,b,a = 4 single; bb,abba = 2 longer; total = 6
        assertEquals(6, solution.countSubstrings("abba"));
    }
}
