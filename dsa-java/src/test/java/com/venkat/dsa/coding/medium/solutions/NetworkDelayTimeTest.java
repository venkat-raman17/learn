package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NetworkDelayTimeTest {

    private final NetworkDelayTime sol = new NetworkDelayTime();

    @Test
    void testExample1() {
        // LeetCode example: 4 nodes, source=2
        int[][] times = {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
        assertEquals(2, sol.networkDelayTime(times, 4, 2));
    }

    @Test
    void testSingleNode() {
        // Only one node, trivially 0
        int[][] times = {};
        assertEquals(0, sol.networkDelayTime(times, 1, 1));
    }

    @Test
    void testUnreachableNode() {
        // Node 2 cannot be reached from node 1
        int[][] times = {{1, 3, 1}};
        assertEquals(-1, sol.networkDelayTime(times, 3, 1));
    }

    @Test
    void testDirectPaths() {
        // All nodes directly reachable from k=1; answer is max weight = 3
        int[][] times = {{1, 2, 1}, {1, 3, 2}, {1, 4, 3}};
        assertEquals(3, sol.networkDelayTime(times, 4, 1));
    }

    @Test
    void testShortestPathChosen() {
        // Two paths to node 3: direct 1->3 cost 10, indirect 1->2->3 cost 3
        int[][] times = {{1, 2, 1}, {2, 3, 2}, {1, 3, 10}};
        // dist[2]=1, dist[3]=3 => answer = 3
        assertEquals(3, sol.networkDelayTime(times, 3, 1));
    }

    @Test
    void testTwoNodes() {
        int[][] times = {{1, 2, 5}};
        assertEquals(5, sol.networkDelayTime(times, 2, 1));
    }
}
