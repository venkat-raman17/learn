package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DetectSquaresTest {

    /**
     * Official LeetCode example:
     *   add [3,10], [11,2], [3,2]
     *   count [11,10] -> 1   (square with corners (3,10),(11,10),(3,2),(11,2))
     *   count [14,8]  -> 0   (no valid square)
     *   add [11,2]           (duplicate)
     *   count [11,10] -> 2   (two ways because (11,2) appears twice)
     */
    @Test
    void officialExample() {
        DetectSquares ds = new DetectSquares();
        ds.add(new int[]{3, 10});
        ds.add(new int[]{11, 2});
        ds.add(new int[]{3, 2});

        assertEquals(1, ds.count(new int[]{11, 10}));
        assertEquals(0, ds.count(new int[]{14, 8}));

        ds.add(new int[]{11, 2}); // second occurrence
        assertEquals(2, ds.count(new int[]{11, 10}));
    }

    @Test
    void noPointsAdded() {
        DetectSquares ds = new DetectSquares();
        assertEquals(0, ds.count(new int[]{0, 0}));
    }

    @Test
    void singlePointAdded_noSquare() {
        DetectSquares ds = new DetectSquares();
        ds.add(new int[]{1, 1});
        assertEquals(0, ds.count(new int[]{1, 1}));
    }

    @Test
    void twoSquares_sharedSide() {
        // Add points forming two squares sharing a vertical edge:
        // Square 1: (0,0),(2,0),(0,2),(2,2)
        // Square 2: (0,0),(4,0),(0,4),(4,4)  -- different size, not square 2
        // Just test one complete square
        DetectSquares ds = new DetectSquares();
        ds.add(new int[]{0, 0});
        ds.add(new int[]{2, 0});
        ds.add(new int[]{0, 2});
        // query corner (2,2) should find 1 square
        assertEquals(1, ds.count(new int[]{2, 2}));
    }

    @Test
    void squareAboveAndBelow() {
        // Points: (0,0),(2,0),(0,2),(2,2) and (0,-2),(2,-2)
        // Query (2,0): can form square above (corners (0,0),(2,0),(0,2),(2,2))
        //              and square below (corners (0,0),(2,0),(0,-2),(2,-2))
        DetectSquares ds = new DetectSquares();
        ds.add(new int[]{0, 0});
        ds.add(new int[]{0, 2});
        ds.add(new int[]{2, 2});
        ds.add(new int[]{0, -2});
        ds.add(new int[]{2, -2});
        // query (2, 0): side length 2
        // above: needs (0,2) and (2,2) => 1*1*1 = 1
        // below: needs (0,-2) and (2,-2) => 1*1*1 = 1
        assertEquals(2, ds.count(new int[]{2, 0}));
    }

    @Test
    void duplicatePointsMultiplyCount() {
        // Add (1,0) twice, (0,1) once, (1,1) once
        // query (0,0): side=1, above: needs (0,1)=1, (1,0)=2, (1,1)=1 => 2
        DetectSquares ds = new DetectSquares();
        ds.add(new int[]{1, 0});
        ds.add(new int[]{1, 0}); // duplicate
        ds.add(new int[]{0, 1});
        ds.add(new int[]{1, 1});
        // query (0,0):
        // dx=1 on row y=0: side=1
        //   qy=1: count(0,1)*count(1,0)*count(1,1) = 1*2*1 = 2
        //   qy=-1: count(0,-1)*... = 0
        assertEquals(2, ds.count(new int[]{0, 0}));
    }

    @Test
    void queryPointIsQueryOnly_notAdded() {
        // The query point itself was not added; that is fine — only the 3 others must be added.
        DetectSquares ds = new DetectSquares();
        ds.add(new int[]{0, 0});
        ds.add(new int[]{3, 0});
        ds.add(new int[]{3, 3});
        // query (0,3): should find square (0,0),(3,0),(3,3),(0,3)
        assertEquals(1, ds.count(new int[]{0, 3}));
    }
}
