package com.venkat.dsa.coding.medium.solutions;

/**
 * Reverse Integer (LeetCode #7)
 *
 * Approach: Pop digits from x one at a time (x % 10) and push them onto the
 * reversed result (result * 10 + digit). Before each push, check whether
 * multiplying result by 10 and adding the digit would overflow a 32-bit signed
 * integer by comparing against Integer.MAX_VALUE / 10 and Integer.MIN_VALUE / 10.
 *
 * Key insight: The overflow check must happen BEFORE the multiplication, not
 * after, because Java int arithmetic wraps silently. Using long for the
 * accumulator or checking the boundary conditions on result before each step
 * are both valid; we use the boundary-check approach to stay within int range.
 *
 * Time complexity:  O(log |x|) — number of digits in x.
 * Space complexity: O(1).
 */
public class ReverseInteger {

    public int reverse(int x) {
        int result = 0;
        while (x != 0) {
            int digit = x % 10;  // pop last digit (negative for negative x)
            x /= 10;

            // Guard: result * 10 + digit must fit in [-2^31, 2^31 - 1]
            // Check result > MAX/10, or result == MAX/10 and digit > 7 (MAX ends in 7)
            if (result > Integer.MAX_VALUE / 10 ||
                    (result == Integer.MAX_VALUE / 10 && digit > 7)) {
                return 0;
            }
            // Check result < MIN/10, or result == MIN/10 and digit < -8 (MIN ends in -8)
            if (result < Integer.MIN_VALUE / 10 ||
                    (result == Integer.MIN_VALUE / 10 && digit < -8)) {
                return 0;
            }

            result = result * 10 + digit; // push digit onto result
        }
        return result;
    }
}
