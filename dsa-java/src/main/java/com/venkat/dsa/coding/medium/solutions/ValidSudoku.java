package com.venkat.dsa.coding.medium.solutions;

import java.util.HashSet;
import java.util.Set;

/**
 * Valid Sudoku (LeetCode 36) — Medium
 *
 * <p>Approach: Make a single pass over all 81 cells. For each filled cell encode three
 * constraints as distinct strings — row membership, column membership, and 3×3 box
 * membership — and insert them into a global HashSet. If any insertion returns
 * {@code false} (duplicate), the board is invalid.
 *
 * <p><b>Time complexity:</b> O(1) — the board is always 9×9 (81 cells, fixed size).<br>
 * <b>Space complexity:</b> O(1) — at most 3×81 = 243 entries in the set (constant).
 *
 * <p><b>Key insight:</b> Encoding each constraint as a self-describing string such as
 * {@code "r0:5"} (row 0 has digit 5) lets a single set track all three rule categories
 * simultaneously without separate arrays.
 */
public class ValidSudoku {

    public boolean isValidSudoku(char[][] board) {
        Set<String> seen = new HashSet<>();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                char ch = board[r][c];
                if (ch == '.') continue;

                // box index: (row/3)*3 + col/3  maps each cell to its 3x3 box (0-8)
                int box = (r / 3) * 3 + (c / 3);

                // each string encodes a (scope, position, digit) triple uniquely
                if (!seen.add("r" + r + ":" + ch)) return false;
                if (!seen.add("c" + c + ":" + ch)) return false;
                if (!seen.add("b" + box + ":" + ch)) return false;
            }
        }
        return true;
    }
}
