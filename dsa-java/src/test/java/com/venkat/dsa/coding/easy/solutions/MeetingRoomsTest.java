package com.venkat.dsa.coding.easy.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MeetingRoomsTest {

    private final MeetingRooms sol = new MeetingRooms();

    @Test
    void example_noOverlap() {
        // [[0,30],[5,10],[15,20]] -> false (0-30 overlaps with 5-10)
        assertFalse(sol.canAttendMeetings(new int[][]{{0, 30}, {5, 10}, {15, 20}}));
    }

    @Test
    void example_canAttend() {
        // [[7,10],[2,4]] -> true (2-4 ends before 7-10 starts)
        assertTrue(sol.canAttendMeetings(new int[][]{{7, 10}, {2, 4}}));
    }

    @Test
    void empty() {
        assertTrue(sol.canAttendMeetings(new int[][]{}));
    }

    @Test
    void single() {
        assertTrue(sol.canAttendMeetings(new int[][]{{1, 5}}));
    }

    @Test
    void adjacentNotOverlapping() {
        // [1,2] and [2,3] share only an endpoint — not a conflict
        assertTrue(sol.canAttendMeetings(new int[][]{{1, 2}, {2, 3}}));
    }

    @Test
    void exactOverlap() {
        assertFalse(sol.canAttendMeetings(new int[][]{{1, 5}, {1, 5}}));
    }

    @Test
    void threeConsecutive() {
        assertTrue(sol.canAttendMeetings(new int[][]{{0, 5}, {5, 10}, {10, 15}}));
    }
}
