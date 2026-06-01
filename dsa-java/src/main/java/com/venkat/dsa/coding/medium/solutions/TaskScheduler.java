package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Task Scheduler (LeetCode #621)
 *
 * <p>Approach: Greedy simulation with a max-heap (by remaining count) and a
 * cooldown queue. At each CPU time unit, pop the most frequent available task
 * from the heap and execute it. After execution the task is placed in a cooldown
 * queue with its "available again" timestamp (current_time + n + 1). Before
 * each tick, release any task from the front of the cooldown queue that is ready.
 * When no task is available, jump time directly to the next ready task to avoid
 * iterating idle ticks one-by-one.
 *
 * <p>Key insight: Always scheduling the most frequent remaining task minimises
 * total idle time, because it reduces the dominant constraint as fast as possible.
 * The time-jump optimisation keeps the loop proportional to the number of
 * executions rather than the number of CPU ticks.
 *
 * <p>Time complexity:  O(N log 26) = O(N) where N = tasks.length.
 * Space complexity: O(26) = O(1) — at most 26 distinct task types.
 */
public class TaskScheduler {

    public int leastInterval(char[] tasks, int n) {
        // Count frequencies
        Map<Character, Integer> freq = new HashMap<>();
        for (char t : tasks) {
            freq.merge(t, 1, Integer::sum);
        }

        // Max-heap by remaining count
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        maxHeap.addAll(freq.values());

        // Cooldown queue stores [remaining_count, time_when_available_again]
        Deque<int[]> cooldownQueue = new ArrayDeque<>();

        int time = 0;

        while (!maxHeap.isEmpty() || !cooldownQueue.isEmpty()) {
            // Release from cooldown any task ready at this moment
            if (!cooldownQueue.isEmpty() && cooldownQueue.peek()[1] <= time) {
                maxHeap.offer(cooldownQueue.poll()[0]);
            }

            if (!maxHeap.isEmpty()) {
                int count = maxHeap.poll() - 1; // execute one unit of this task
                time++;
                if (count > 0) {
                    // Schedule re-availability after n idle slots
                    cooldownQueue.offer(new int[]{count, time + n});
                }
            } else {
                // No task available; jump directly to next release time
                time = cooldownQueue.peek()[1];
            }
        }

        return time;
    }
}
