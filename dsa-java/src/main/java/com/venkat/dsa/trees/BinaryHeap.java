package com.venkat.dsa.trees;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Binary Min-Heap — a complete binary tree where every node is less than or
 * equal to its children, giving O(1) access to the minimum element.
 *
 * <p><b>Backing representation:</b> A dynamic {@link ArrayList} used as an
 * implicit array-based heap. For a node at index {@code i}:
 * <ul>
 *   <li>Parent: {@code (i - 1) / 2}</li>
 *   <li>Left child: {@code 2*i + 1}</li>
 *   <li>Right child: {@code 2*i + 2}</li>
 * </ul>
 *
 * <p><b>Invariant:</b> {@code heap[i] <= heap[2*i+1]} and
 * {@code heap[i] <= heap[2*i+2]} for all valid indices (heap-order property).
 * The tree is always a <em>complete</em> binary tree (all levels full except
 * possibly the last, filled left to right).
 *
 * <p><b>Operations:</b>
 * <pre>
 * Operation   Time        Space (auxiliary)
 * ---------   ----------  -----------------
 * add         O(log n)    O(1)
 * peek        O(1)        O(1)
 * poll        O(log n)    O(1)
 * size        O(1)        O(1)
 * isEmpty     O(1)        O(1)
 * Space (total)           O(n)
 * </pre>
 *
 * <p><b>When to use:</b> Priority queues, scheduling, graph algorithms such as
 * Dijkstra / Prim, median-maintenance (paired with a max-heap), and any
 * scenario that repeatedly needs the minimum (or maximum, for a max-heap
 * variant) element with efficient insertion.
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Heap vs BST: heap gives O(1) min/max but no ordered traversal; BST
 *       gives O(log n) min/max but ordered traversal in O(n).</li>
 *   <li>Heapify (build heap from array) is O(n), not O(n log n).</li>
 *   <li>{@link java.util.PriorityQueue} in the JDK is a min-heap; use
 *       {@code Collections.reverseOrder()} for a max-heap.</li>
 *   <li>Sift-up fixes a violation between a node and its parent;
 *       sift-down fixes a violation between a node and its children.</li>
 * </ul>
 *
 * @param <T> element type; must be {@link Comparable} so natural ordering
 *            determines heap priority (smallest value = highest priority).
 */
public class BinaryHeap<T extends Comparable<T>> {

    private final ArrayList<T> data;

    /** Constructs an empty binary min-heap. */
    public BinaryHeap() {
        this.data = new ArrayList<>();
    }

    // -----------------------------------------------------------------------
    // Public API
    // -----------------------------------------------------------------------

    /**
     * Inserts {@code value} into the heap and restores the heap-order property
     * by sifting the new element up toward the root.
     *
     * <p><b>Time:</b> O(log n) &nbsp;|&nbsp; <b>Space:</b> O(1) auxiliary.
     *
     * @param value the element to insert; must not be {@code null}
     * @throws IllegalArgumentException if {@code value} is {@code null}
     */
    public void add(T value) {
        if (value == null) {
            throw new IllegalArgumentException("null elements are not permitted");
        }
        data.add(value);
        siftUp(data.size() - 1);
    }

    /**
     * Returns, but does not remove, the minimum element (the root).
     *
     * <p><b>Time:</b> O(1) &nbsp;|&nbsp; <b>Space:</b> O(1).
     *
     * @return the minimum element
     * @throws NoSuchElementException if the heap is empty
     */
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        return data.get(0);
    }

    /**
     * Removes and returns the minimum element (the root). The last element is
     * moved to the root position and sifted down to restore heap order.
     *
     * <p><b>Time:</b> O(log n) &nbsp;|&nbsp; <b>Space:</b> O(1) auxiliary.
     *
     * @return the minimum element
     * @throws NoSuchElementException if the heap is empty
     */
    public T poll() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        T min = data.get(0);
        int last = data.size() - 1;
        if (last == 0) {
            data.remove(0);
            return min;
        }
        // Move last element to root, then sift down
        data.set(0, data.remove(last));
        siftDown(0);
        return min;
    }

    /**
     * Returns the number of elements currently in the heap.
     *
     * <p><b>Time:</b> O(1).
     *
     * @return element count
     */
    public int size() {
        return data.size();
    }

    /**
     * Returns {@code true} if the heap contains no elements.
     *
     * <p><b>Time:</b> O(1).
     *
     * @return {@code true} if empty
     */
    public boolean isEmpty() {
        return data.isEmpty();
    }

    // -----------------------------------------------------------------------
    // Internal helpers
    // -----------------------------------------------------------------------

    /**
     * Sifts the element at {@code index} up the tree until the heap-order
     * property is restored (element >= its parent).
     */
    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (data.get(index).compareTo(data.get(parent)) < 0) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    /**
     * Sifts the element at {@code index} down the tree until the heap-order
     * property is restored (element <= both children).
     */
    private void siftDown(int index) {
        int size = data.size();
        while (true) {
            int left  = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && data.get(left).compareTo(data.get(smallest)) < 0) {
                smallest = left;
            }
            if (right < size && data.get(right).compareTo(data.get(smallest)) < 0) {
                smallest = right;
            }
            if (smallest == index) {
                break;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    /** Swaps elements at positions {@code i} and {@code j}. */
    private void swap(int i, int j) {
        T tmp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, tmp);
    }
}
