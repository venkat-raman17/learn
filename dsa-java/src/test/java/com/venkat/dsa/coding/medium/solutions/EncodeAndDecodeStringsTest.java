package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EncodeAndDecodeStringsTest {

    private final EncodeAndDecodeStrings sol = new EncodeAndDecodeStrings();

    // LeetCode example 1
    @Test
    void example1_roundtrip() {
        List<String> input = Arrays.asList("lint", "code", "love", "you");
        assertEquals(input, sol.decode(sol.encode(input)));
    }

    // LeetCode example 2: single blank string
    @Test
    void example2_emptyStringInList() {
        List<String> input = Collections.singletonList("");
        assertEquals(input, sol.decode(sol.encode(input)));
    }

    // Strings that contain the '#' delimiter character
    @Test
    void stringsContainingHash() {
        List<String> input = Arrays.asList("a#b", "#", "##");
        assertEquals(input, sol.decode(sol.encode(input)));
    }

    // Empty list encodes to empty string and decodes back to empty list
    @Test
    void emptyList() {
        List<String> input = Collections.emptyList();
        assertEquals(input, sol.decode(sol.encode(input)));
    }

    // Single empty string
    @Test
    void singleEmptyString() {
        List<String> input = Collections.singletonList("");
        assertEquals(input, sol.decode(sol.encode(input)));
    }

    // String with spaces and special characters
    @Test
    void specialCharacters() {
        List<String> input = Arrays.asList("hello world", "foo\nbar", "tab\there");
        assertEquals(input, sol.decode(sol.encode(input)));
    }

    // Long strings to verify length prefix parsing
    @Test
    void longString() {
        String big = "a".repeat(1000);
        List<String> input = Arrays.asList(big, "short", big);
        assertEquals(input, sol.decode(sol.encode(input)));
    }

    // Single character strings
    @Test
    void singleCharStrings() {
        List<String> input = Arrays.asList("a", "b", "c");
        assertEquals(input, sol.decode(sol.encode(input)));
    }
}
