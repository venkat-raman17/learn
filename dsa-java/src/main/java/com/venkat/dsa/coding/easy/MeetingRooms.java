package com.venkat.dsa.coding.easy;

import java.util.Arrays;

/**
 * NeetCode / LeetCode — Meeting Rooms
 *
 * <p>Difficulty: EASY
 * <p>Pattern: Intervals
 * <p>URL: https://leetcode.com/problems/meeting-rooms/
 *
 * <p>Given an array of meeting time intervals where intervals[i] = [start_i, end_i],
 * determine if a person could attend all meetings (i.e., no two intervals overlap).
 *
 * <p>Constraints:
 * <ul>
 *   <li>0 &lt;= intervals.length &lt;= 10^4</li>
 *   <li>0 &lt;= start_i &lt; end_i &lt;= 10^6</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  intervals = [[0,30],[5,10],[15,20]]
 *   Output: false   (0-30 overlaps with both 5-10 and 15-20)
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  intervals = [[7,10],[2,4]]
 *   Output: true
 * </pre>
 *
 * <p>Target: Time O(n log n), Space O(1) (sort in place).
 *
 * <p>Hint 1: Sort intervals by start time. Then a single pass suffices.
 * <p>Hint 2: After sorting, two meetings overlap iff intervals[i][0] < intervals[i-1][1].
 */
public class MeetingRooms {

    /**
     * Returns true if a single person can attend all meetings without any overlap.
     *
     * @param intervals array of [start, end] meeting intervals
     * @return true if no two intervals overlap
     */
    public boolean canAttendMeetings(int[][] intervals) {
        throw new UnsupportedOperationException("implement me");
    }
}
