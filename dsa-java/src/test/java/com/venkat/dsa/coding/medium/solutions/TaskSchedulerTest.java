package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskSchedulerTest {

    private final TaskScheduler sol = new TaskScheduler();

    // Official example 1: tasks=["A","A","A","B","B","B"], n=2 -> 8
    // Optimal: A->B->idle->A->B->idle->A->B
    @Test
    void testOfficialExample1() {
        char[] tasks = {'A','A','A','B','B','B'};
        assertEquals(8, sol.leastInterval(tasks, 2));
    }

    // Official example 2: tasks=["A","A","A","B","B","B"], n=0 -> 6
    // No cooldown needed: AAABBB (or any order)
    @Test
    void testNoCooldown() {
        char[] tasks = {'A','A','A','B','B','B'};
        assertEquals(6, sol.leastInterval(tasks, 0));
    }

    // Official example 3: tasks=["A","A","A","A","A","A","B","C","D","E","F","G"], n=2 -> 16
    // A has count=6; formula: (6-1)*(2+1)+1 = 16; other tasks fill idle slots exactly
    @Test
    void testOfficialExample3() {
        char[] tasks = {'A','A','A','A','A','A','B','C','D','E','F','G'};
        assertEquals(16, sol.leastInterval(tasks, 2));
    }

    // Single task type, n=2: ["A","A","A"], n=2
    // A -> idle -> idle -> A -> idle -> idle -> A  = 7
    @Test
    void testSingleTaskType() {
        char[] tasks = {'A','A','A'};
        assertEquals(7, sol.leastInterval(tasks, 2));
    }

    // Single task: ["A"], n=5 -> 1 (no cooldown needed with one task)
    @Test
    void testSingleTask() {
        assertEquals(1, sol.leastInterval(new char[]{'A'}, 5));
    }

    // All distinct tasks, n=2: each only once -> no cooldown ever needed
    // ["A","B","C","D"], n=2 -> 4
    @Test
    void testAllDistinct() {
        char[] tasks = {'A','B','C','D'};
        assertEquals(4, sol.leastInterval(tasks, 2));
    }

    // Large n but enough variety to fill it: ["A","A","B","B"], n=1
    // A->B->A->B = 4 (no idle needed)
    @Test
    void testNoIdleNeededDueToVariety() {
        char[] tasks = {'A','A','B','B'};
        assertEquals(4, sol.leastInterval(tasks, 1));
    }

    // Large n forcing idle: ["A","A"], n=3
    // A -> idle -> idle -> idle -> A = 5
    @Test
    void testLargeCooldown() {
        char[] tasks = {'A','A'};
        assertEquals(5, sol.leastInterval(tasks, 3));
    }
}
