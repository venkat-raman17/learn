package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MeetingRoomsIITest {

    private final MeetingRoomsII sol = new MeetingRoomsII();

    @Test
    void example1() {
        // [[0,30],[5,10],[15,20]] -> 2
        assertEquals(2, sol.minMeetingRooms(new int[][]{{0, 30}, {5, 10}, {15, 20}}));
    }

    @Test
    void example2() {
        // [[7,10],[2,4]] -> 1 (no overlap)
        assertEquals(1, sol.minMeetingRooms(new int[][]{{7, 10}, {2, 4}}));
    }

    @Test
    void empty() {
        assertEquals(0, sol.minMeetingRooms(new int[][]{}));
    }

    @Test
    void single() {
        assertEquals(1, sol.minMeetingRooms(new int[][]{{1, 5}}));
    }

    @Test
    void allSameTime() {
        // 3 meetings at the same time need 3 rooms
        assertEquals(3, sol.minMeetingRooms(new int[][]{{1, 4}, {1, 4}, {1, 4}}));
    }

    @Test
    void sequential() {
        // Meetings back-to-back: only 1 room needed
        assertEquals(1, sol.minMeetingRooms(new int[][]{{1, 2}, {2, 3}, {3, 4}}));
    }

    @Test
    void peakOverlap() {
        // [[1,5],[2,6],[3,7],[4,8]] -> 4 rooms all overlap at t=4
        assertEquals(4, sol.minMeetingRooms(new int[][]{{1, 5}, {2, 6}, {3, 7}, {4, 8}}));
    }

    @Test
    void reuseRoom() {
        // [[0,10],[5,15],[10,20]] -> 2 ([0,10] and [10,20] can share, [5,15] needs own)
        assertEquals(2, sol.minMeetingRooms(new int[][]{{0, 10}, {5, 15}, {10, 20}}));
    }
}
