package com.venkat.dsa.coding.hard.solutions;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class WordSearchIITest {

    private WordSearchII solver = new WordSearchII();

    /** Convenience: convert result list to a set for order-independent comparison. */
    private Set<String> toSet(List<String> list) {
        return new HashSet<>(list);
    }

    // -----------------------------------------------------------------------
    // Official LeetCode example 1
    // board:  o a a n
    //         e t a e
    //         i h k r
    //         i f l v
    // words: ["oath","pea","eat","rain"]  →  ["eat","oath"]
    //
    // "eat": e(1,3)->a(1,2)->t(1,1)  ✓
    // "oath": o(0,0)->a(0,1)->t(1,1)->h(2,1)  ✓
    // "pea": no 'p' on board  ✗
    // "rain": r(2,3) neighbors are e(1,3),v(3,3),k(2,2) — no 'a' adjacent  ✗
    // -----------------------------------------------------------------------
    @Test
    void testOfficialExample1() {
        char[][] board = {
            {'o','a','a','n'},
            {'e','t','a','e'},
            {'i','h','k','r'},
            {'i','f','l','v'}
        };
        String[] words = {"oath","pea","eat","rain"};
        Set<String> result = toSet(solver.findWords(board, words));
        assertEquals(Set.of("eat","oath"), result);
    }

    // -----------------------------------------------------------------------
    // Official LeetCode example 2
    // board:  a b
    //         c d
    // words: ["abcb"]  →  []
    // "abcb": a(0,0)->b(0,1)->c(1,?) — (0,1) neighbors are (0,0)=a and (1,1)=d; no 'c'. ✗
    // -----------------------------------------------------------------------
    @Test
    void testOfficialExample2() {
        char[][] board = {
            {'a','b'},
            {'c','d'}
        };
        String[] words = {"abcb"};
        List<String> result = solver.findWords(board, words);
        assertTrue(result.isEmpty(), "abcb requires reusing 'b', which is not allowed");
    }

    // -----------------------------------------------------------------------
    // Single cell board
    // -----------------------------------------------------------------------
    @Test
    void testSingleCell() {
        char[][] board = {{'a'}};
        List<String> found = solver.findWords(board, new String[]{"a","b"});
        assertEquals(Set.of("a"), toSet(found));
    }

    // -----------------------------------------------------------------------
    // Word that snakes through the entire board
    // board:  a b c
    //         d e f
    //         g h i
    // "abcfihgde" winds through every cell
    // path: a(0,0)->b(0,1)->c(0,2)->f(1,2)->i(2,2)->h(2,1)->g(2,0)->d(1,0)->e(1,1)
    // -----------------------------------------------------------------------
    @Test
    void testSnakePath() {
        char[][] board = {
            {'a','b','c'},
            {'d','e','f'},
            {'g','h','i'}
        };
        String[] words = {"abcfihgde", "abc", "xyz"};
        Set<String> result = toSet(solver.findWords(board, words));
        assertTrue(result.contains("abcfihgde"), "full snake path should be found");
        assertTrue(result.contains("abc"),       "'abc' top row");
        assertFalse(result.contains("xyz"),      "'xyz' not on board");
    }

    // -----------------------------------------------------------------------
    // Duplicate words in input — each should appear at most once in output
    // -----------------------------------------------------------------------
    @Test
    void testDuplicateWordsInInput() {
        char[][] board = {
            {'a','b'},
            {'c','d'}
        };
        // "ab" can be found; listed twice in input
        String[] words = {"ab","ab","cd"};
        List<String> result = solver.findWords(board, words);
        // "ab": a(0,0)->b(0,1) ✓  "cd": c(1,0)->d(1,1) ✓
        long abCount = result.stream().filter("ab"::equals).count();
        long cdCount = result.stream().filter("cd"::equals).count();
        assertEquals(1, abCount, "'ab' should appear exactly once");
        assertEquals(1, cdCount, "'cd' should appear exactly once");
    }

    // -----------------------------------------------------------------------
    // No matching words
    // -----------------------------------------------------------------------
    @Test
    void testNoneFound() {
        char[][] board = {
            {'x','x'},
            {'x','x'}
        };
        List<String> result = solver.findWords(board, new String[]{"abc","def"});
        assertTrue(result.isEmpty());
    }

    // -----------------------------------------------------------------------
    // All words found on a 1×n board
    // board: a b c d e
    // words: "abcde", "abc", "cde", "bcd"
    // -----------------------------------------------------------------------
    @Test
    void testLinearBoard() {
        char[][] board = {{'a','b','c','d','e'}};
        String[] words = {"abcde","abc","cde","bcd","xyz"};
        Set<String> result = toSet(solver.findWords(board, words));
        assertTrue(result.contains("abcde"));
        assertTrue(result.contains("abc"));
        assertTrue(result.contains("cde"));
        assertTrue(result.contains("bcd"));
        assertFalse(result.contains("xyz"));
    }

    // -----------------------------------------------------------------------
    // Word that cannot be formed because it would reuse a cell
    // board:  a a
    // word "aaa" needs 3 'a's but only 2 exist
    // -----------------------------------------------------------------------
    @Test
    void testCellReuseNotAllowed() {
        char[][] board = {{'a','a'}};
        List<String> result = solver.findWords(board, new String[]{"aaa"});
        assertTrue(result.isEmpty(), "'aaa' needs 3 cells but board only has 2");
    }

    // -----------------------------------------------------------------------
    // Board restored after search (board should not be mutated permanently)
    // -----------------------------------------------------------------------
    @Test
    void testBoardNotMutated() {
        char[][] board = {
            {'a','b'},
            {'c','d'}
        };
        solver.findWords(board, new String[]{"ab","cd"});
        // After DFS all '#' sentinels must be restored
        assertEquals('a', board[0][0]);
        assertEquals('b', board[0][1]);
        assertEquals('c', board[1][0]);
        assertEquals('d', board[1][1]);
    }
}
