package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class DetectSquaresTest {

    @Test
    public void testBasicSquare() {
        DetectSquares ds = new DetectSquares();
        ds.add(new int[]{3, 10});
        ds.add(new int[]{11, 2});
        ds.add(new int[]{3, 2});
        assertEquals(1, ds.count(new int[]{11, 10}));
    }

    @Test
    public void testNoSquare() {
        DetectSquares ds = new DetectSquares();
        ds.add(new int[]{3, 10});
        ds.add(new int[]{11, 2});
        ds.add(new int[]{3, 2});
        assertEquals(0, ds.count(new int[]{14, 8}));
    }

    @Test
    public void testDuplicatePointDoublesCount() {
        DetectSquares ds = new DetectSquares();
        ds.add(new int[]{3, 10});
        ds.add(new int[]{11, 2});
        ds.add(new int[]{3, 2});
        ds.add(new int[]{11, 2});
        assertEquals(2, ds.count(new int[]{11, 10}));
    }
}
