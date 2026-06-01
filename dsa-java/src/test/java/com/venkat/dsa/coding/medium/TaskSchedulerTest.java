package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class TaskSchedulerTest {

    private final TaskScheduler solution = new TaskScheduler();

    @Test
    void testExample1() {
        assertEquals(8, solution.leastInterval(
                new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 2));
    }

    @Test
    void testExample2() {
        assertEquals(16, solution.leastInterval(
                new char[]{'A', 'A', 'A', 'A', 'A', 'A', 'B', 'C', 'D', 'E', 'F', 'G'}, 2));
    }

    @Test
    void testNoCooldown() {
        // n=0 means tasks can run back-to-back
        assertEquals(6, solution.leastInterval(
                new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 0));
    }
}
