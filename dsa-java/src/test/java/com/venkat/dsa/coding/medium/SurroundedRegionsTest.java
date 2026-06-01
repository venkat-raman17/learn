package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class SurroundedRegionsTest {

    private final SurroundedRegions solution = new SurroundedRegions();

    @Test
    public void testExample1() {
        char[][] board = {
            {'X','X','X','X'},
            {'X','O','O','X'},
            {'X','X','O','X'},
            {'X','O','X','X'}
        };
        solution.solve(board);
        assertArrayEquals(new char[]{'X','X','X','X'}, board[0]);
        assertArrayEquals(new char[]{'X','X','X','X'}, board[1]);
        assertArrayEquals(new char[]{'X','X','X','X'}, board[2]);
        assertArrayEquals(new char[]{'X','O','X','X'}, board[3]);
    }

    @Test
    public void testSingleX() {
        char[][] board = {{'X'}};
        solution.solve(board);
        assertEquals('X', board[0][0]);
    }

    @Test
    public void testAllOs() {
        char[][] board = {
            {'O','O'},
            {'O','O'}
        };
        solution.solve(board);
        // All Os are on the border, none should be flipped
        assertEquals('O', board[0][0]);
        assertEquals('O', board[1][1]);
    }
}
