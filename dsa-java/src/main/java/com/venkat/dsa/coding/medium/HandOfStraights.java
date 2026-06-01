package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Hand of Straights
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Greedy
 * <p>URL: https://leetcode.com/problems/hand-of-straights/
 *
 * <p>Alice has a {@code hand} of cards and wants to rearrange them into groups of {@code groupSize}
 * consecutive cards. Return {@code true} if she can do so, {@code false} otherwise.
 *
 * <p><b>Constraints:</b>
 * <ul>
 *   <li>1 &lt;= hand.length &lt;= 10^4</li>
 *   <li>0 &lt;= hand[i] &lt;= 10^9</li>
 *   <li>1 &lt;= groupSize &lt;= hand.length</li>
 * </ul>
 *
 * <p><b>Examples:</b>
 * <pre>
 * Input: hand = [1,2,3,6,2,3,4,7,8], groupSize = 3  Output: true   ([1,2,3],[2,3,4],[6,7,8])
 * Input: hand = [1,2,3,4,5], groupSize = 4           Output: false
 * </pre>
 *
 * <p><b>Target:</b> O(n log n) time, O(n) space
 *
 * <p><b>Hint 1:</b> Use a sorted frequency map; always try to start a new group from the smallest
 * available card.
 * <p><b>Hint 2:</b> For each starting card, decrement the count of each of the next {@code groupSize}
 * consecutive values — if any count goes negative, return false.
 */
public class HandOfStraights {

    /**
     * Returns true if the hand can be rearranged into groups of {@code groupSize} consecutive cards.
     *
     * @param hand      array of card values
     * @param groupSize required size of each consecutive group
     * @return whether a valid rearrangement exists
     */
    public boolean isNStraightHand(int[] hand, int groupSize) {
        throw new UnsupportedOperationException("implement me");
    }
}
