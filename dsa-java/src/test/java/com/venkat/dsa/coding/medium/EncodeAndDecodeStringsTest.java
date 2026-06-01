package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class EncodeAndDecodeStringsTest {

    private final EncodeAndDecodeStrings solution = new EncodeAndDecodeStrings();

    @Test
    public void testRoundTripNormal() {
        List<String> input = List.of("lint", "code", "love", "you");
        assertEquals(input, solution.decode(solution.encode(input)));
    }

    @Test
    public void testRoundTripEmpty() {
        List<String> input = List.of("");
        assertEquals(input, solution.decode(solution.encode(input)));
    }

    @Test
    public void testRoundTripSpecialChars() {
        List<String> input = List.of("hello#world", "foo/bar", "");
        assertEquals(input, solution.decode(solution.encode(input)));
    }
}
