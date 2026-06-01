package com.venkat.dsa.coding.easy.solutions;

/**
 * Happy Number (LeetCode 202)
 *
 * <p>A number is "happy" if repeatedly replacing it with the sum of the squares of its digits
 * eventually reaches 1. Use Floyd's cycle detection (slow/fast pointers) on the sequence of
 * digit-square sums to detect either termination at 1 (happy) or a cycle (not happy).
 *
 * <p><b>Key insight:</b> Any unhappy number eventually enters a cycle that never includes 1,
 * so cycle detection suffices — no hash set needed.
 *
 * <p><b>Time complexity:</b> O(log n) per step, O(log n) steps before cycle — effectively O(log n).
 * <b>Space complexity:</b> O(1) (Floyd's algorithm uses two pointers only).
 */
public class HappyNumber {

    /** Computes sum of squares of digits of n. */
    private int digitSquareSum(int n) {
        int sum = 0;
        while (n > 0) {
            int d = n % 10;
            sum += d * d;
            n /= 10;
        }
        return sum;
    }

    /**
     * Returns true if n is a happy number.
     *
     * @param n positive integer
     * @return true iff n is happy
     */
    public boolean isHappy(int n) {
        int slow = n;
        int fast = digitSquareSum(n); // fast starts one step ahead

        // Floyd's cycle detection: if they meet at 1 => happy, else => cycle
        while (fast != 1 && slow != fast) {
            slow = digitSquareSum(slow);           // one step
            fast = digitSquareSum(digitSquareSum(fast)); // two steps
        }
        return fast == 1;
    }
}
