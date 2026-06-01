package com.venkat.dsa.coding.easy.solutions;

import java.util.Arrays;

/**
 * Meeting Rooms (LeetCode 252)
 *
 * <p>Sort all intervals by start time. Then do a single linear scan: if any interval's
 * start time is strictly less than the previous interval's end time, there is an overlap
 * and a person cannot attend all meetings.
 *
 * <p><b>Time complexity:</b> O(n log n) — dominated by the sort.<br>
 * <b>Space complexity:</b> O(1) extra (or O(n) if sorting is not in-place).
 *
 * <p><b>Key insight:</b> After sorting by start, an overlap can only happen between two
 * consecutive intervals, so one pass suffices.
 */
public class MeetingRooms {

    public boolean canAttendMeetings(int[][] intervals) {
        // Sort by start time so overlaps are always between adjacent entries
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        for (int i = 1; i < intervals.length; i++) {
            // If the current meeting starts before the previous one ends → conflict
            if (intervals[i][0] < intervals[i - 1][1]) {
                return false;
            }
        }
        return true;
    }
}
