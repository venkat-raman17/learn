package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NQueensTest {

    private final NQueens solution = new NQueens();

    @Test
    void example1_n4() {
        // n=4 -> 2 solutions
        List<List<String>> result = solution.solveNQueens(4);
        assertEquals(2, result.size());

        // Solution 1: [".Q..","...Q","Q...","..Q."]
        assertTrue(result.contains(List.of(".Q..", "...Q", "Q...", "..Q.")));
        // Solution 2: ["..Q.","Q...","...Q",".Q.."]
        assertTrue(result.contains(List.of("..Q.", "Q...", "...Q", ".Q..")));
    }

    @Test
    void example2_n1() {
        List<List<String>> result = solution.solveNQueens(1);
        assertEquals(1, result.size());
        assertEquals(List.of("Q"), result.get(0));
    }

    @Test
    void n2_noSolution() {
        List<List<String>> result = solution.solveNQueens(2);
        assertTrue(result.isEmpty());
    }

    @Test
    void n3_noSolution() {
        List<List<String>> result = solution.solveNQueens(3);
        assertTrue(result.isEmpty());
    }

    @Test
    void n5_count() {
        // Known: 10 solutions for n=5
        List<List<String>> result = solution.solveNQueens(5);
        assertEquals(10, result.size());
    }

    @Test
    void n6_count() {
        // Known: 4 solutions for n=6
        List<List<String>> result = solution.solveNQueens(6);
        assertEquals(4, result.size());
    }

    @Test
    void allSolutionsValid() {
        // For each n=4 solution, verify no two queens share row/col/diagonal
        List<List<String>> solutions = solution.solveNQueens(4);
        for (List<String> board : solutions) {
            assertValidPlacement(board);
        }
    }

    private void assertValidPlacement(List<String> board) {
        int n = board.size();
        Set<Integer> cols = new HashSet<>();
        Set<Integer> leftDiag = new HashSet<>();
        Set<Integer> rightDiag = new HashSet<>();

        for (int r = 0; r < n; r++) {
            String row = board.get(r);
            assertEquals(n, row.length());
            int queenCount = 0;
            for (int c = 0; c < n; c++) {
                if (row.charAt(c) == 'Q') {
                    queenCount++;
                    assertTrue(cols.add(c), "Column conflict at col " + c);
                    assertTrue(leftDiag.add(r - c), "Left diagonal conflict at (" + r + "," + c + ")");
                    assertTrue(rightDiag.add(r + c), "Right diagonal conflict at (" + r + "," + c + ")");
                }
            }
            assertEquals(1, queenCount, "Row " + r + " must have exactly one queen");
        }
        assertEquals(n, cols.size(), "Must have queens in n distinct columns");
    }
}
