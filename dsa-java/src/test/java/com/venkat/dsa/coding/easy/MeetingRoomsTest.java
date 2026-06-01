package com.venkat.dsa.coding.easy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class MeetingRoomsTest {

    private final MeetingRooms solution = new MeetingRooms();

    @Test
    void testOverlappingMeetings() {
        int[][] intervals = {{0, 30}, {5, 10}, {15, 20}};
        assertFalse(solution.canAttendMeetings(intervals));
    }

    @Test
    void testNonOverlappingMeetings() {
        int[][] intervals = {{7, 10}, {2, 4}};
        assertTrue(solution.canAttendMeetings(intervals));
    }

    @Test
    void testEmptyIntervals() {
        int[][] intervals = {};
        assertTrue(solution.canAttendMeetings(intervals));
    }
}
