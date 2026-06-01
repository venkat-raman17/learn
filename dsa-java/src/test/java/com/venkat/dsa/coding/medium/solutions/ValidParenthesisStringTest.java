package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidParenthesisStringTest {

    private final ValidParenthesisString solution = new ValidParenthesisString();

    @Test
    void example1_valid() {
        // "()" is clearly valid
        assertTrue(solution.checkValidString("()"));
    }

    @Test
    void example2_valid() {
        // "(*)" -> * can be empty string -> "()" valid
        assertTrue(solution.checkValidString("(*)"));
    }

    @Test
    void example3_valid() {
        // "(*))" -> * as '(' -> "(())" valid
        assertTrue(solution.checkValidString("(*))"));
    }

    @Test
    void allStars() {
        // "***" -> all can be empty -> valid
        assertTrue(solution.checkValidString("***"));
    }

    @Test
    void emptyString() {
        assertTrue(solution.checkValidString(""));
    }

    @Test
    void onlyStar() {
        assertTrue(solution.checkValidString("*"));
    }

    @Test
    void unbalancedOpen() {
        // "((" no stars to close -> invalid
        assertFalse(solution.checkValidString("(("));
    }

    @Test
    void unbalancedClose() {
        assertFalse(solution.checkValidString("))"));
    }

    @Test
    void starAtStart_closingNeeded() {
        // "*)" -> * as '(' -> "()" valid
        assertTrue(solution.checkValidString("*)"));
    }

    @Test
    void extraOpenCannotBeFixed() {
        // "(((*" -> max hi after processing: lo=3,hi=4 then star lo=2,hi=5; lo never 0 -> false
        // Let's trace: '(' lo=1,hi=1; '(' lo=2,hi=2; '(' lo=3,hi=3; '*' lo=2,hi=4 -> lo!=0
        assertFalse(solution.checkValidString("(((*"));
    }

    @Test
    void complexValid() {
        // "((*(*)" -> trace:
        // ( lo=1,hi=1
        // ( lo=2,hi=2
        // * lo=1,hi=3
        // ( lo=2,hi=4
        // * lo=1,hi=5
        // ) lo=0,hi=4  -> lo==0 -> true
        assertTrue(solution.checkValidString("((*(*)" ));
    }

    @Test
    void starAsEmpty() {
        // "()" with extra star in middle: "(*)" valid (star as empty)
        assertTrue(solution.checkValidString("(*)"));
    }

    @Test
    void tooManyClosing() {
        // "())" -> hi after ')' ')' goes negative at second ')'? trace:
        // '(' lo=1,hi=1; ')' lo=0,hi=0; ')' lo=-1->0, hi=-1 -> hi<0 -> false
        assertFalse(solution.checkValidString("())"));
    }
}
