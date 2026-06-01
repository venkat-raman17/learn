package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GasStationTest {

    private final GasStation solution = new GasStation();

    @Test
    void example1() {
        // gas=[1,2,3,4,5] cost=[3,4,5,1,2]
        // Starting at index 3: surplus 3, then 4+5-5=4, then 4+1-3=2, then 2+2-4=0, then 0+3-5=-2...
        // Actually: start=3, tank=4-1=3, +5-2=6, +1-3=4, +2-4=2, +3-5=0 -> 0 ok. Answer=3.
        assertEquals(3, solution.canCompleteCircuit(
                new int[]{1, 2, 3, 4, 5},
                new int[]{3, 4, 5, 1, 2}));
    }

    @Test
    void example2_impossible() {
        // gas=[2,3,4] cost=[3,4,3] total gas=9 total cost=10 -> impossible
        assertEquals(-1, solution.canCompleteCircuit(
                new int[]{2, 3, 4},
                new int[]{3, 4, 3}));
    }

    @Test
    void singleStation_possible() {
        // gas[0] >= cost[0]
        assertEquals(0, solution.canCompleteCircuit(new int[]{5}, new int[]{4}));
    }

    @Test
    void singleStation_impossible() {
        assertEquals(-1, solution.canCompleteCircuit(new int[]{1}, new int[]{2}));
    }

    @Test
    void startAtZero() {
        // gas=[3,1,1] cost=[1,2,2]: start=0, tank=2, 2+1-2=1, 1+1-2=0 -> ok
        assertEquals(0, solution.canCompleteCircuit(
                new int[]{3, 1, 1},
                new int[]{1, 2, 2}));
    }

    @Test
    void startAtLast() {
        // gas=[1,1,1,1,5] cost=[2,2,2,2,1]: only index 4 has surplus
        // total gas=9 total cost=9, start must be 4
        assertEquals(4, solution.canCompleteCircuit(
                new int[]{1, 1, 1, 1, 5},
                new int[]{2, 2, 2, 2, 1}));
    }

    @Test
    void allZeroNet() {
        // Every station has gas == cost, any start works — expect 0 (first valid)
        assertEquals(0, solution.canCompleteCircuit(
                new int[]{2, 2, 2},
                new int[]{2, 2, 2}));
    }
}
