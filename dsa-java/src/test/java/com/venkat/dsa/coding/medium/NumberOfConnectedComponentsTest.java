package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class NumberOfConnectedComponentsTest {

    private final NumberOfConnectedComponents solution = new NumberOfConnectedComponents();

    @Test
    public void testTwoComponents() {
        assertEquals(2, solution.countComponents(5, new int[][]{{0,1},{1,2},{3,4}}));
    }

    @Test
    public void testOneComponent() {
        assertEquals(1, solution.countComponents(5, new int[][]{{0,1},{1,2},{2,3},{3,4}}));
    }

    @Test
    public void testNoEdges() {
        assertEquals(4, solution.countComponents(4, new int[][]{}));
    }
}
