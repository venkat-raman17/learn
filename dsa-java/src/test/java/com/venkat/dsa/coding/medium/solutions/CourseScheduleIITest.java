package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class CourseScheduleIITest {

    private final CourseScheduleII sol = new CourseScheduleII();

    /** Verify that the returned order is a valid topological order for the given prerequisites. */
    private boolean isValidOrder(int[] order, int numCourses, int[][] prereqs) {
        if (order.length != numCourses) return false;
        // Build position map
        int[] pos = new int[numCourses];
        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < order.length; i++) {
            if (!seen.add(order[i])) return false; // duplicate
            pos[order[i]] = i;
        }
        // Check every prereq pair
        for (int[] p : prereqs) {
            if (pos[p[1]] >= pos[p[0]]) return false; // p[1] must come before p[0]
        }
        return true;
    }

    @Test
    void example1_twoCoursesLinear() {
        int[] order = sol.findOrder(2, new int[][]{{1, 0}});
        assertTrue(isValidOrder(order, 2, new int[][]{{1, 0}}));
    }

    @Test
    void example2_fourCourses() {
        int[][] prereqs = {{1,0},{2,0},{3,1},{3,2}};
        int[] order = sol.findOrder(4, prereqs);
        assertTrue(isValidOrder(order, 4, prereqs));
    }

    @Test
    void cycleDetected_returnsEmpty() {
        int[] order = sol.findOrder(2, new int[][]{{1,0},{0,1}});
        assertEquals(0, order.length);
    }

    @Test
    void noPrerequisites() {
        int[] order = sol.findOrder(3, new int[0][]);
        // Any permutation of [0,1,2] is valid
        assertEquals(3, order.length);
        Set<Integer> vals = new HashSet<>();
        for (int v : order) vals.add(v);
        assertEquals(new HashSet<>(Arrays.asList(0, 1, 2)), vals);
    }

    @Test
    void singleCourse() {
        int[] order = sol.findOrder(1, new int[0][]);
        assertArrayEquals(new int[]{0}, order);
    }

    @Test
    void threeCycleLong() {
        int[] order = sol.findOrder(3, new int[][]{{1,0},{2,1},{0,2}});
        assertEquals(0, order.length);
    }
}
