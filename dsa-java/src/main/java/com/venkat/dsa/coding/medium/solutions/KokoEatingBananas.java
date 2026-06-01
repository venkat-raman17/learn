package com.venkat.dsa.coding.medium.solutions;

/**
 * Koko Eating Bananas (LeetCode #875)
 *
 * Approach: Binary search on the eating speed k in the range [1, max(piles)].
 * For each candidate speed, compute the total hours needed (ceil(pile/k) per
 * pile); if the total hours fit within h, try a slower speed, otherwise try
 * faster. The minimum valid speed is the answer.
 *
 * Key insight: The feasibility function "can Koko finish all piles at speed k
 * within h hours?" is monotone — true for all speeds >= answer, false below —
 * so binary search finds the minimum true value in O(n log(max)) time.
 *
 * Time complexity:  O(n log M) where n = piles.length, M = max(piles).
 * Space complexity: O(1) — only loop variables.
 */
public class KokoEatingBananas {

    public int minEatingSpeed(int[] piles, int h) {
        int lo = 1;
        int hi = 0;
        for (int p : piles) hi = Math.max(hi, p); // upper bound: eat largest pile in 1 hour

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canFinish(piles, mid, h)) {
                hi = mid; // mid works; try slower
            } else {
                lo = mid + 1; // mid too slow; go faster
            }
        }

        return lo; // lo == hi: the minimum valid speed
    }

    /** Returns true if eating at speed k finishes all piles within h hours. */
    private boolean canFinish(int[] piles, int k, int h) {
        long hours = 0;
        for (int p : piles) {
            hours += (p + k - 1) / k; // ceil(p / k) without floating point
        }
        return hours <= h;
    }
}
