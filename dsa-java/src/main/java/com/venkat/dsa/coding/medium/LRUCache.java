package com.venkat.dsa.coding.medium;

/**
 * NeetCode / LeetCode — LRU Cache
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Linked List
 * <p>URL: https://leetcode.com/problems/lru-cache/
 *
 * <p>Design a data structure that follows the Least Recently Used (LRU) cache constraint.
 * Implement LRUCache with get(key) returning the value or -1, and put(key, value) inserting
 * or updating a key, evicting the least recently used key when capacity is exceeded.
 * Both operations must run in O(1) average time.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= capacity <= 3000</li>
 *   <li>0 <= key <= 10^4</li>
 *   <li>0 <= value <= 10^5</li>
 *   <li>At most 2 * 10^5 calls will be made to get and put.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 *   LRUCache cache = new LRUCache(2);
 *   cache.put(1, 1); cache.put(2, 2);
 *   cache.get(1);    // returns 1
 *   cache.put(3, 3); // evicts key 2
 *   cache.get(2);    // returns -1 (not found)
 * </pre>
 *
 * <p>Target: O(1) get and put
 *
 * <p>Hint 1: Combine a HashMap (for O(1) lookup) with a doubly linked list (for O(1) eviction/reorder).
 * <p>Hint 2: Use sentinel head and tail nodes so you never need null checks when inserting/removing.
 */
public class LRUCache {

    public LRUCache(int capacity) {
        throw new UnsupportedOperationException("implement me");
    }

    public int get(int key) {
        throw new UnsupportedOperationException("implement me");
    }

    public void put(int key, int value) {
        throw new UnsupportedOperationException("implement me");
    }
}
