package com.venkat.dsa.coding.easy.solutions;

/**
 * Climbing Stairs (LeetCode 70)
 *
 * Approach: Bottom-up dynamic programming (Fibonacci pattern). Let dp[i] be the
 * number of distinct ways to reach step i. You can arrive from step i-1 (one step)
 * or step i-2 (two steps), so dp[i] = dp[i-1] + dp[i-2]. We only need the last
 * two values, so space is O(1).
 *
 * Key insight: The answer is the (n+1)-th Fibonacci number; iterating forward
 * avoids any recursion overhead.
 *
 * Time:  O(n)
 * Space: O(1)
 */
public class ClimbingStairs {

    public int climbStairs(int n) {
        if (n <= 2) return n;

        int prev2 = 1; // ways to reach step 1
        int prev1 = 2; // ways to reach step 2

        for (int i = 3; i <= n; i++) {
            int curr = prev1 + prev2; // dp[i] = dp[i-1] + dp[i-2]
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
}
