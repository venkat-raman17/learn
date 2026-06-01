package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Time Based Key-Value Store (LeetCode #981)
 *
 * Approach: Store each key mapped to a list of (timestamp, value) pairs, always
 * appended in increasing timestamp order (guaranteed by the problem). On get,
 * binary search the list for the largest timestamp <= the requested timestamp,
 * returning the associated value or "" if none exists.
 *
 * Key insight: Because set() calls arrive in non-decreasing timestamp order,
 * the list per key is already sorted, enabling O(log n) binary search on get
 * rather than a linear scan.
 *
 * Time complexity:  set — O(1) amortised (list append).
 *                  get  — O(log n) binary search over n entries for the key.
 * Space complexity: O(n) total entries stored across all keys.
 */
public class TimeBasedKeyValueStore {

    /** Simple pair holding a timestamp and its value. */
    private static class Entry {
        final int timestamp;
        final String value;
        Entry(int timestamp, String value) {
            this.timestamp = timestamp;
            this.value = value;
        }
    }

    private final Map<String, List<Entry>> store;

    public TimeBasedKeyValueStore() {
        store = new HashMap<>();
    }

    public void set(String key, String value, int timestamp) {
        store.computeIfAbsent(key, k -> new ArrayList<>())
             .add(new Entry(timestamp, value));
    }

    public String get(String key, int timestamp) {
        List<Entry> entries = store.get(key);
        if (entries == null) return "";

        // Binary search for the rightmost entry with entry.timestamp <= timestamp
        int lo = 0, hi = entries.size() - 1;
        String result = "";

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (entries.get(mid).timestamp <= timestamp) {
                result = entries.get(mid).value; // valid candidate; try later timestamps
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }

        return result;
    }
}
