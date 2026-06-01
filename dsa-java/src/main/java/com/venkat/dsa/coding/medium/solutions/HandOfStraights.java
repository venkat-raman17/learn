package com.venkat.dsa.coding.medium.solutions;

import java.util.Map;
import java.util.TreeMap;

/**
 * Hand of Straights (LeetCode #846)
 *
 * <p>Sort card ranks via a TreeMap (sorted by key). Greedily consume groups starting from the
 * smallest available rank: for each starting rank, try to decrement counts for {@code groupSize}
 * consecutive ranks. If any count is missing or becomes negative the hand cannot be divided.
 *
 * <p><b>Key insight:</b> The smallest remaining card must always be the start of a new group —
 * there is no benefit in saving it for a later, higher-ranked group.
 *
 * <p><b>Time complexity:</b> O(n log n) — dominated by TreeMap insertions/lookups.<br>
 * <b>Space complexity:</b> O(n) — for the frequency map.
 */
public class HandOfStraights {

    public boolean isNStraightHand(int[] hand, int groupSize) {
        if (hand.length % groupSize != 0) {
            return false;
        }

        // Frequency map sorted by card value
        Map<Integer, Integer> count = new TreeMap<>();
        for (int card : hand) {
            count.merge(card, 1, Integer::sum);
        }

        for (int startCard : count.keySet()) {
            int startCount = count.get(startCard);
            if (startCount == 0) {
                continue; // already consumed
            }

            // Consume 'startCount' groups starting at startCard
            for (int i = 0; i < groupSize; i++) {
                int current = startCard + i;
                int available = count.getOrDefault(current, 0);
                if (available < startCount) {
                    return false; // not enough cards to fill groups
                }
                count.put(current, available - startCount);
            }
        }

        return true;
    }
}
