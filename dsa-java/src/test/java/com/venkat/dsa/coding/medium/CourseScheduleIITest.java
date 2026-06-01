package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

@Disabled("practice — delete when you start")
public class CourseScheduleIITest {

    private final CourseScheduleII solution = new CourseScheduleII();

    @Test
    public void testTwoCourses() {
        int[] order = solution.findOrder(2, new int[][]{{1,0}});
        assertArrayEquals(new int[]{0,1}, order);
    }

    @Test
    public void testCycleReturnsEmpty() {
        int[] order = solution.findOrder(2, new int[][]{{1,0},{0,1}});
        assertArrayEquals(new int[]{}, order);
    }

    @Test
    public void testFourCourses() {
        int[] order = solution.findOrder(4, new int[][]{{1,0},{2,0},{3,1},{3,2}});
        assertNotNull(order);
        assertEquals(4, order.length);
        // course 0 must come before 1, 2; courses 1,2 before 3
        int pos0 = indexOf(order, 0), pos1 = indexOf(order, 1),
            pos2 = indexOf(order, 2), pos3 = indexOf(order, 3);
        assertTrue(pos0 < pos1);
        assertTrue(pos0 < pos2);
        assertTrue(pos1 < pos3);
        assertTrue(pos2 < pos3);
    }

    private int indexOf(int[] arr, int val) {
        for (int i = 0; i < arr.length; i++) if (arr[i] == val) return i;
        return -1;
    }
}
