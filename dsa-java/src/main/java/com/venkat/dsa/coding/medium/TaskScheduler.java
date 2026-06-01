package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — Task Scheduler
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Heap / Priority Queue
 * <p>URL: https://leetcode.com/problems/task-scheduler/
 *
 * <p>Given a list of CPU tasks (each represented by a character A-Z) and a non-negative integer n
 * representing the cooldown period between two same tasks, return the minimum number of CPU
 * intervals required to finish all tasks. The CPU may be idle during a cooldown.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= tasks.length &lt;= 10^4</li>
 *   <li>tasks[i] is an uppercase English letter</li>
 *   <li>0 &lt;= n &lt;= 100</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Example 1:
 *   Input:  tasks = ["A","A","A","B","B","B"], n = 2
 *   Output: 8
 *   Explanation: A -> B -> idle -> A -> B -> idle -> A -> B
 *
 * Example 2:
 *   Input:  tasks = ["A","A","A","A","A","A","B","C","D","E","F","G"], n = 2
 *   Output: 16
 * </pre>
 *
 * <p>Target: Time O(m) where m = tasks.length, Space O(1) (26 task types)
 *
 * <p>Hint 1: Use a max-heap for task frequencies and a queue to track when cooled-down tasks become available again.
 * <p>Hint 2: Alternatively, the math formula: result = max(tasks.length, (maxFreq - 1) * (n + 1) + countOfMaxFreq).
 */
public class TaskScheduler {

    public int leastInterval(char[] tasks, int n) {
        throw new UnsupportedOperationException("implement me");
    }
}
