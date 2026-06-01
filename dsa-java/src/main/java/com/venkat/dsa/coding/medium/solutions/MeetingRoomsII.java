package com.venkat.dsa.coding.medium.solutions;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Meeting Rooms II (LeetCode 253)
 *
 * <p>Sort meetings by start time. Use a min-heap that tracks the end times of rooms
 * currently in use. For each meeting, if the earliest-ending room finishes before (or at)
 * the current meeting's start, reuse that room (pop and push updated end). Otherwise,
 * allocate a new room (just push). The heap size at the end equals the minimum rooms needed.
 *
 * <p><b>Time complexity:</b> O(n log n) — sort + n heap operations each O(log n).<br>
 * <b>Space complexity:</b> O(n) for the heap.
 *
 * <p><b>Key insight:</b> The heap top always gives the room that becomes free the soonest;
 * checking only that room is sufficient because if even the earliest-ending room is still
 * busy, no existing room can be reused.
 */
public class MeetingRoomsII {

    public int minMeetingRooms(int[][] intervals) {
        if (intervals == null || intervals.length == 0) return 0;

        // Sort by start time
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        // Min-heap of end times of currently allocated rooms
        PriorityQueue<Integer> endTimes = new PriorityQueue<>();

        for (int[] interval : intervals) {
            // If the earliest-ending room is free before this meeting starts, reuse it
            if (!endTimes.isEmpty() && endTimes.peek() <= interval[0]) {
                endTimes.poll(); // free that room
            }
            // Allocate (or re-allocate) a room ending at interval[1]
            endTimes.offer(interval[1]);
        }

        return endTimes.size();
    }
}
