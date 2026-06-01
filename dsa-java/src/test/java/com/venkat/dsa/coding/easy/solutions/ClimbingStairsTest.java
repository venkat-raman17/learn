package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClimbingStairsTest {

    private final ClimbingStairs solution = new ClimbingStairs();

    @Test
    void example1_twoSteps() {
        // n=2 -> 2 ways: (1+1) or (2)
        assertEquals(2, solution.climbStairs(2));
    }

    @Test
    void example2_threeSteps() {
        // n=3 -> 3 ways: (1+1+1), (1+2), (2+1)
        assertEquals(3, solution.climbStairs(3));
    }

    @Test
    void baseCase_oneStep() {
        assertEquals(1, solution.climbStairs(1));
    }

    @Test
    void fourSteps() {
        // n=4 -> 5 ways (Fib sequence: 1,2,3,5,8,...)
        assertEquals(5, solution.climbStairs(4));
    }

    @Test
    void fiveSteps() {
        assertEquals(8, solution.climbStairs(5));
    }

    @Test
    void largeInput() {
        // n=10 -> Fib(12) = 89  (0-indexed Fib: F(1)=1,F(2)=2,...,F(10)=89)
        assertEquals(89, solution.climbStairs(10));
    }
}
