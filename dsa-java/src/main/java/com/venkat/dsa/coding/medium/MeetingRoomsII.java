package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Meeting Rooms II
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Intervals
 * <p>URL: https://leetcode.com/problems/meeting-rooms-ii/
 *
 * <p>Given an array of meeting time intervals where intervals[i] = [start_i, end_i],
 * return the minimum number of conference rooms required to hold all meetings.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= intervals.length &lt;= 10^4</li>
 *   <li>0 &lt;= start_i &lt; end_i &lt;= 10^6</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 *   Input:  intervals = [[0,30],[5,10],[15,20]]
 *   Output: 2
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 *   Input:  intervals = [[7,10],[2,4]]
 *   Output: 1
 * </pre>
 *
 * <p>Target: Time O(n log n), Space O(n).
 *
 * <p>Hint 1: Separate start times and end times into two sorted arrays. Use two pointers
 * to sweep through events and track concurrent meetings.
 * <p>Hint 2: Alternatively, use a min-heap of end times: for each meeting sorted by start,
 * if the earliest-ending room is free (heap top &lt;= current start), reuse it; else add a room.
 */
public class MeetingRoomsII {

    /**
     * Returns the minimum number of conference rooms needed.
     *
     * @param intervals array of [start, end] meeting intervals
     * @return minimum number of rooms required
     */
    public int minMeetingRooms(int[][] intervals) {
        throw new UnsupportedOperationException("implement me");
    }
}
