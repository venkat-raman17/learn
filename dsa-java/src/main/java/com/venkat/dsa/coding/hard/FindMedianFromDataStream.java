package com.venkat.dsa.coding.hard;

/**
 * NeetCode / LeetCode — Find Median from Data Stream
 *
 * <p>Difficulty: HARD
 * <p>Pattern: Heap / Priority Queue
 * <p>URL: https://leetcode.com/problems/find-median-from-data-stream/
 *
 * <p>Design a data structure that supports adding integers from a data stream and finding the
 * median of all elements added so far. If the count is even, the median is the average of the
 * two middle values; if odd, it is the middle value.
 *
 * <p>Constraints:
 * <ul>
 *   <li>-10^5 &lt;= num &lt;= 10^5</li>
 *   <li>There will be at least one element before calling findMedian.</li>
 *   <li>At most 5 * 10^4 calls will be made to addNum and findMedian.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Example 1:
 *   addNum(1)
 *   addNum(2)
 *   findMedian() -> 1.5
 *   addNum(3)
 *   findMedian() -> 2.0
 *
 * Example 2:
 *   addNum(6)
 *   findMedian() -> 6.0
 *   addNum(10)
 *   findMedian() -> 8.0
 * </pre>
 *
 * <p>Target: Time O(log n) addNum, O(1) findMedian, Space O(n)
 *
 * <p>Hint 1: Maintain two heaps: a max-heap for the lower half and a min-heap for the upper half, keeping their sizes equal or differing by at most 1.
 * <p>Hint 2: After each addNum, rebalance so the max-heap size equals or is exactly one more than the min-heap size; the median is then either the max-heap root or the average of both roots.
 */
public class FindMedianFromDataStream {

    public FindMedianFromDataStream() {
        throw new UnsupportedOperationException("implement me");
    }

    public void addNum(int num) {
        throw new UnsupportedOperationException("implement me");
    }

    public double findMedian() {
        throw new UnsupportedOperationException("implement me");
    }
}
