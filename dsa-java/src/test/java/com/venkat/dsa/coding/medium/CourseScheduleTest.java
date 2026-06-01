package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class CourseScheduleTest {

    private final CourseSchedule solution = new CourseSchedule();

    @Test
    public void testCanFinish() {
        assertTrue(solution.canFinish(2, new int[][]{{1,0}}));
    }

    @Test
    public void testCycleDetected() {
        assertFalse(solution.canFinish(2, new int[][]{{1,0},{0,1}}));
    }

    @Test
    public void testNoPrerequistes() {
        assertTrue(solution.canFinish(5, new int[][]{}));
    }
}
