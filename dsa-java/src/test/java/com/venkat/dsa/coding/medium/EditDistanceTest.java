package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class EditDistanceTest {

    private final EditDistance solution = new EditDistance();

    @Test
    void testExample1() {
        assertEquals(3, solution.minDistance("horse", "ros"));
    }

    @Test
    void testExample2() {
        assertEquals(5, solution.minDistance("intention", "execution"));
    }

    @Test
    void testEmptyStrings() {
        assertEquals(0, solution.minDistance("", ""));
    }
}
