package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Detect Squares (LeetCode 2013)
 *
 * <p>Maintains a frequency map of added points and a set of distinct x-values for each y-value.
 * For a query point (px, py), iterates over every distinct x-coordinate (dx) that has a point
 * on row py; each such point (dx, py) forms one side of the square. The side length is |dx - px|,
 * so the two candidate y-values for the opposite corners are py + side and py - side. For each
 * candidate (qy), checks that both (px, qy) and (dx, qy) exist (using their counts) and multiplies
 * the three counts together to accumulate the total number of axis-aligned squares.
 *
 * <p><b>Key insight:</b> Fix one horizontal edge of the square; the opposite edge is uniquely
 * determined (up to reflection). All four corners must exist with their respective multiplicities.
 *
 * <p><b>Time complexity:</b> add O(1); count O(n) per query where n = distinct points on same row.
 * <b>Space complexity:</b> O(total points added).
 */
public class DetectSquares {

    /** frequency[x][y] = number of times point (x,y) was added */
    private final Map<Integer, Map<Integer, Integer>> freq = new HashMap<>();

    /** xsByY.get(y) = set of distinct x-values that have been added at row y */
    private final Map<Integer, Set<Integer>> xsByY = new HashMap<>();

    /** Adds point [x, y] to the data structure (duplicates allowed). */
    public void add(int[] point) {
        int x = point[0], y = point[1];
        freq.computeIfAbsent(x, k -> new HashMap<>())
            .merge(y, 1, Integer::sum);
        xsByY.computeIfAbsent(y, k -> new HashSet<>()).add(x);
    }

    /**
     * Returns the number of ways to choose three additional points from the added points
     * to form an axis-aligned square with the query point {@code point}.
     */
    public int count(int[] point) {
        int px = point[0], py = point[1];
        int total = 0;

        // No points share row py -> no square possible
        if (!xsByY.containsKey(py)) return 0;

        // Iterate over every distinct x on row py as the second horizontal corner
        for (int dx : xsByY.get(py)) {
            if (dx == px) continue; // need a non-degenerate side
            int side = Math.abs(dx - px);

            // Try both squares: above and below
            for (int qy : new int[]{py + side, py - side}) {
                // count(px, qy) * count(dx, py) * count(dx, qy)
                int cPxQy = freq.getOrDefault(px, new HashMap<>()).getOrDefault(qy, 0);
                int cDxPy = freq.getOrDefault(dx, new HashMap<>()).getOrDefault(py, 0);
                int cDxQy = freq.getOrDefault(dx, new HashMap<>()).getOrDefault(qy, 0);
                total += cPxQy * cDxPy * cDxQy;
            }
        }
        return total;
    }
}
