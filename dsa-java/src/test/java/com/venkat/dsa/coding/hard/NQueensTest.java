package com.venkat.dsa.coding.hard;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class NQueensTest {

    private final NQueens solution = new NQueens();

    @Test
    public void testSolveNQueens_n4() {
        List<List<String>> result = solution.solveNQueens(4);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(board ->
            board.equals(List.of(".Q..", "...Q", "Q...", "..Q."))));
        assertTrue(result.stream().anyMatch(board ->
            board.equals(List.of("..Q.", "Q...", "...Q", ".Q.."))));
    }

    @Test
    public void testSolveNQueens_n1() {
        List<List<String>> result = solution.solveNQueens(1);
        assertEquals(1, result.size());
        assertEquals(List.of("Q"), result.get(0));
    }

    @Test
    public void testSolveNQueens_n8() {
        List<List<String>> result = solution.solveNQueens(8);
        assertNotNull(result);
        // Classic result: 92 solutions for 8-queens
        assertEquals(92, result.size());
    }
}
