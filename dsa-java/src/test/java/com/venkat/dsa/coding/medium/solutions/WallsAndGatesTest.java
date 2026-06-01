package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WallsAndGatesTest {

    private static final int INF = Integer.MAX_VALUE;
    private static final int W = -1; // wall sentinel

    private final WallsAndGates sol = new WallsAndGates();

    @Test
    void example1() {
        // LeetCode official example
        int[][] rooms = {
            {INF,  W,  0,  INF},
            {INF, INF, INF,  W},
            {INF,  W, INF,  W},
            {0,   W, INF, INF}
        };
        sol.wallsAndGates(rooms);

        int[][] expected = {
            {3, W, 0, 1},
            {2, 2, 1, W},
            {1, W, 2, W},
            {0, W, 3, 4}
        };
        assertArrayEquals(expected, rooms);
    }

    @Test
    void noGates_roomsUnchanged() {
        int[][] rooms = {
            {INF, W},
            {INF, INF}
        };
        sol.wallsAndGates(rooms);
        // No gate reachable, so INF cells remain INF
        assertEquals(INF, rooms[0][0]);
        assertEquals(INF, rooms[1][0]);
        assertEquals(INF, rooms[1][1]);
    }

    @Test
    void allGates() {
        int[][] rooms = {{0, 0}, {0, 0}};
        sol.wallsAndGates(rooms);
        // Already all gates, nothing changes
        assertArrayEquals(new int[][]{{0, 0}, {0, 0}}, rooms);
    }

    @Test
    void singleGate() {
        int[][] rooms = {
            {INF, INF, INF},
            {INF,  0, INF},
            {INF, INF, INF}
        };
        sol.wallsAndGates(rooms);
        int[][] expected = {
            {2, 1, 2},
            {1, 0, 1},
            {2, 1, 2}
        };
        assertArrayEquals(expected, rooms);
    }

    @Test
    void emptyGrid() {
        sol.wallsAndGates(new int[0][0]); // should not throw
    }
}
