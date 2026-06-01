package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class MeetingRoomsIITest {

    private final MeetingRoomsII solution = new MeetingRoomsII();

    @Test
    void testTwoRoomsNeeded() {
        int[][] intervals = {{0, 30}, {5, 10}, {15, 20}};
        assertEquals(2, solution.minMeetingRooms(intervals));
    }

    @Test
    void testOneRoomNeeded() {
        int[][] intervals = {{7, 10}, {2, 4}};
        assertEquals(1, solution.minMeetingRooms(intervals));
    }

    @Test
    void testAllOverlapping() {
        int[][] intervals = {{1, 10}, {2, 9}, {3, 8}};
        assertEquals(3, solution.minMeetingRooms(intervals));
    }
}
