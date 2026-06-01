package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class WallsAndGatesTest {

    private static final int INF = Integer.MAX_VALUE;
    private final WallsAndGates solution = new WallsAndGates();

    @Test
    public void testExample1() {
        int[][] rooms = {
            {INF, -1,  0, INF},
            {INF, INF, INF, -1},
            {INF, -1, INF, -1},
            {  0, -1, INF, INF}
        };
        solution.wallsAndGates(rooms);
        assertArrayEquals(new int[]{3, -1, 0, 1}, rooms[0]);
        assertArrayEquals(new int[]{2,  2, 1, -1}, rooms[1]);
        assertArrayEquals(new int[]{1, -1, 2, -1}, rooms[2]);
        assertArrayEquals(new int[]{0, -1, 3,  4}, rooms[3]);
    }

    @Test
    public void testWallOnly() {
        int[][] rooms = {{-1}};
        solution.wallsAndGates(rooms);
        assertEquals(-1, rooms[0][0]);
    }

    @Test
    public void testGateOnly() {
        int[][] rooms = {{0}};
        solution.wallsAndGates(rooms);
        assertEquals(0, rooms[0][0]);
    }
}
