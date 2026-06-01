package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarFleetTest {

    private final CarFleet solution = new CarFleet();

    // Official LeetCode examples
    @Test
    void example1() {
        // target=12, positions=[10,8,0,5,3], speeds=[2,4,1,1,3]
        // Car at 10 arrives in 1.0
        // Car at 8  arrives in 1.0 -> catches car at 10, same fleet
        // Car at 5  arrives in 3.5
        // Car at 3  arrives in 3.0 -> catches car at 5, same fleet
        // Car at 0  arrives in 12.0 -> new fleet
        // Total: 3 fleets
        assertEquals(3, solution.carFleet(12,
            new int[]{10, 8, 0, 5, 3},
            new int[]{2, 4, 1, 1, 3}));
    }

    @Test
    void example2_singleCar() {
        assertEquals(1, solution.carFleet(10, new int[]{3}, new int[]{3}));
    }

    @Test
    void example3_twoCarsNoFleet() {
        // target=100, positions=[0,2], speeds=[4,2]
        // Car at 2: 98/2 = 49.0; Car at 0: 100/4 = 25.0 -> 25 < 49, catches up -> 1 fleet? No:
        // Sorted by pos desc: car(2,2) time=49, car(0,4) time=25 -> 25 < 49 so car(0) merges -> 1 fleet
        assertEquals(1, solution.carFleet(100, new int[]{0, 2}, new int[]{4, 2}));
    }

    // Edge cases
    @Test
    void noCars() {
        assertEquals(0, solution.carFleet(10, new int[]{}, new int[]{}));
    }

    @Test
    void allCarsFormSeparateFleets() {
        // Each car is slower than the one behind but none can catch up
        // positions=[1,2,3] speeds=[3,2,1] target=10
        // Car at 3: 7/1=7.0; Car at 2: 8/2=4.0 -> 4 < 7 merges
        // Car at 1: 9/3=3.0 -> 3 < 4 (current stack top after merge is 7.0) -> 3 < 7 merges
        // All merge -> 1 fleet
        assertEquals(1, solution.carFleet(10, new int[]{1, 2, 3}, new int[]{3, 2, 1}));
    }

    @Test
    void allCarsSamePosAndSpeed() {
        // 3 cars all at different positions, same speed — none can catch up to leading car
        // positions=[0,3,6] speeds=[2,2,2] target=12
        // times: 6, 4.5, 3 — each later car arrives earlier, so merges
        // Car at 6: 6/2=3; Car at 3: 9/2=4.5 -> 4.5>3 new fleet; Car at 0: 12/2=6 -> 6>4.5 new fleet
        // Total 3 fleets
        assertEquals(3, solution.carFleet(12, new int[]{0, 3, 6}, new int[]{2, 2, 2}));
    }

    @Test
    void carAtTarget() {
        // A car already at the target
        assertEquals(1, solution.carFleet(10, new int[]{10}, new int[]{5}));
    }

    @Test
    void twoFleets() {
        // positions=[4,1] speeds=[2,1] target=10
        // Car at 4: 6/2=3.0; Car at 1: 9/1=9.0 -> 9>3 new fleet -> 2 fleets
        assertEquals(2, solution.carFleet(10, new int[]{4, 1}, new int[]{2, 1}));
    }
}
