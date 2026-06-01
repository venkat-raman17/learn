package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SurroundedRegionsTest {

    private final SurroundedRegions sol = new SurroundedRegions();

    private char[][] copy(char[][] g) {
        char[][] c = new char[g.length][];
        for (int i = 0; i < g.length; i++) c[i] = g[i].clone();
        return c;
    }

    @Test
    void example1() {
        char[][] board = {
            {'X','X','X','X'},
            {'X','O','O','X'},
            {'X','X','O','X'},
            {'X','O','X','X'}
        };
        sol.solve(board);
        char[][] expected = {
            {'X','X','X','X'},
            {'X','X','X','X'},
            {'X','X','X','X'},
            {'X','O','X','X'}  // bottom-left 'O' touches border — stays
        };
        assertArrayEquals(expected, board);
    }

    @Test
    void example2_singleCell_X() {
        char[][] board = {{'X'}};
        sol.solve(board);
        assertArrayEquals(new char[][]{{'X'}}, board);
    }

    @Test
    void allOs_allOnBorder() {
        // 3x3 all 'O' — every 'O' is connected to the border, none flipped
        char[][] board = {
            {'O','O','O'},
            {'O','O','O'},
            {'O','O','O'}
        };
        char[][] original = copy(board);
        sol.solve(board);
        assertArrayEquals(original, board);
    }

    @Test
    void innerOsSurrounded() {
        char[][] board = {
            {'X','X','X','X'},
            {'X','O','O','X'},
            {'X','O','O','X'},
            {'X','X','X','X'}
        };
        sol.solve(board);
        // All inner 'O' become 'X'
        char[][] expected = {
            {'X','X','X','X'},
            {'X','X','X','X'},
            {'X','X','X','X'},
            {'X','X','X','X'}
        };
        assertArrayEquals(expected, board);
    }

    @Test
    void borderOsUnchanged() {
        char[][] board = {
            {'O','X','O'},
            {'X','X','X'},
            {'O','X','O'}
        };
        sol.solve(board);
        // All 'O' are on the border — none should be flipped
        char[][] expected = {
            {'O','X','O'},
            {'X','X','X'},
            {'O','X','O'}
        };
        assertArrayEquals(expected, board);
    }
}
