package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CourseScheduleTest {

    private final CourseSchedule sol = new CourseSchedule();

    @Test
    void example1_canFinish() {
        // 2 courses, [1,0] means 0 before 1 -> linear, can finish
        assertTrue(sol.canFinish(2, new int[][]{{1, 0}}));
    }

    @Test
    void example2_cannotFinish() {
        // [1,0] and [0,1] form a cycle
        assertFalse(sol.canFinish(2, new int[][]{{1, 0}, {0, 1}}));
    }

    @Test
    void noCourses_triviallyTrue() {
        assertTrue(sol.canFinish(1, new int[0][]));
    }

    @Test
    void noPrerequisites_allIndependent() {
        assertTrue(sol.canFinish(5, new int[0][]));
    }

    @Test
    void longChain_canFinish() {
        // 0->1->2->3->4, linear dependency
        int[][] prereqs = {{1,0},{2,1},{3,2},{4,3}};
        assertTrue(sol.canFinish(5, prereqs));
    }

    @Test
    void cycleLengthThree() {
        // 0->1->2->0
        int[][] prereqs = {{1,0},{2,1},{0,2}};
        assertFalse(sol.canFinish(3, prereqs));
    }

    @Test
    void disconnectedGraph_noLoop() {
        // Components {0,1} and {2,3} with no cycle
        int[][] prereqs = {{1,0},{3,2}};
        assertTrue(sol.canFinish(4, prereqs));
    }

    @Test
    void selfLoop() {
        // Course 0 requires itself
        assertFalse(sol.canFinish(2, new int[][]{{0, 0}}));
    }
}
