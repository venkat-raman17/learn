package com.venkat.dsa.coding.medium.solutions;

/**
 * Search a 2D Matrix (LeetCode #74)
 *
 * Approach: Treat the m×n matrix as a virtual sorted array of length m*n.
 * A single binary search on index [0, m*n-1] maps each virtual index i to
 * matrix[i/n][i%n], letting us binary-search without flattening the matrix.
 *
 * Key insight: Because each row is sorted and the first element of each row
 * is greater than the last element of the previous row, the entire matrix
 * is one contiguous sorted sequence when read left-to-right, top-to-bottom.
 *
 * Time complexity:  O(log(m*n)) — one binary search over m*n elements.
 * Space complexity: O(1) — only pointer variables.
 */
public class SearchA2DMatrix {

    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;

        int lo = 0, hi = m * n - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int val = matrix[mid / n][mid % n]; // map flat index to 2D coords

            if (val == target) {
                return true;
            } else if (val < target) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }

        return false;
    }
}
