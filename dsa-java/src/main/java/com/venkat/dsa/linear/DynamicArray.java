package com.venkat.dsa.linear;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * DynamicArray — a generic resizable array backed by a raw {@code Object[]}.
 *
 * <p><b>Backing representation:</b> A single {@code Object[]} whose allocated length
 * ({@code capacity}) is at least as large as the logical size ({@code size}).
 * When {@code size == capacity}, the array is grown by doubling before the next add.
 *
 * <p><b>Invariant(s):</b>
 * <ul>
 *   <li>{@code 0 <= size <= capacity}</li>
 *   <li>{@code data[0..size-1]} hold the logical elements; {@code data[size..capacity-1]} are {@code null}.</li>
 *   <li>{@code capacity >= 1} at all times.</li>
 * </ul>
 *
 * <p><b>Operations table:</b>
 * <pre>
 * Operation          Time (avg)   Time (worst)  Space
 * ─────────────────────────────────────────────────────
 * get(i)             O(1)         O(1)          O(1)
 * set(i, v)          O(1)         O(1)          O(1)
 * add(v)             O(1)*        O(n) resize   O(1)*
 * add(i, v)          O(n)         O(n)          O(1)*
 * remove(i)          O(n)         O(n)          O(1)
 * indexOf(v)         O(n)         O(n)          O(1)
 * contains(v)        O(n)         O(n)          O(1)
 * size()             O(1)         O(1)          O(1)
 * isEmpty()          O(1)         O(1)          O(1)
 * clear()            O(n)         O(n)          O(1)
 *
 * * amortized; overall O(n) for n appends (geometric growth).
 * </pre>
 *
 * <p><b>When to use:</b> When you need O(1) random access and amortized O(1) appends
 * with a familiar array feel, and do not need O(1) prepend or arbitrary-position insert.
 * Prefer over {@code LinkedList} when random access dominates; prefer over a fixed array
 * when the size is unknown up front.
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Doubling strategy keeps the amortized cost of {@code add} at O(1): total work for
 *       n appends is O(n) (geometric series 1+2+4+...+n = 2n).</li>
 *   <li>Shrinking (halving at 1/4 full) avoids thrashing on repeated add/remove at boundary.</li>
 *   <li>The raw {@code Object[]} + unchecked cast is the same technique used by
 *       {@code java.util.ArrayList} internally.</li>
 * </ul>
 *
 * @param <T> the type of elements stored in this array
 */
public class DynamicArray<T> {

    private static final int DEFAULT_CAPACITY = 8;
    private static final int MIN_CAPACITY = 1;

    private Object[] data;
    private int size;

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    /**
     * Creates an empty DynamicArray with the default initial capacity (8).
     *
     * <p>Big-O: O(1)
     */
    public DynamicArray() {
        data = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /**
     * Creates an empty DynamicArray with the given initial capacity.
     *
     * <p>Big-O: O(1)
     *
     * @param initialCapacity the initial backing-array size; must be &gt;= 1
     * @throws IllegalArgumentException if {@code initialCapacity < 1}
     */
    public DynamicArray(int initialCapacity) {
        if (initialCapacity < MIN_CAPACITY) {
            throw new IllegalArgumentException(
                    "initialCapacity must be >= 1, got: " + initialCapacity);
        }
        data = new Object[initialCapacity];
        size = 0;
    }

    // -----------------------------------------------------------------------
    // Core operations
    // -----------------------------------------------------------------------

    /**
     * Returns the number of elements logically stored.
     *
     * <p>Big-O: O(1)
     *
     * @return logical size
     */
    public int size() {
        return size;
    }

    /**
     * Returns {@code true} if this array contains no elements.
     *
     * <p>Big-O: O(1)
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the element at the given index.
     *
     * <p>Big-O: O(1)
     *
     * @param index position in [0, size)
     * @return the element at {@code index}
     * @throws IndexOutOfBoundsException if {@code index < 0 || index >= size}
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) data[index];
    }

    /**
     * Replaces the element at the given index with {@code value}.
     *
     * <p>Big-O: O(1)
     *
     * @param index position in [0, size)
     * @param value the new value (may be {@code null})
     * @return the old value that was replaced
     * @throws IndexOutOfBoundsException if {@code index < 0 || index >= size}
     */
    @SuppressWarnings("unchecked")
    public T set(int index, T value) {
        checkIndex(index);
        T old = (T) data[index];
        data[index] = value;
        return old;
    }

    /**
     * Appends {@code value} to the end of the array.
     * Grows the backing array (doubling) if necessary.
     *
     * <p>Big-O: amortized O(1), worst-case O(n) when resize triggers
     *
     * @param value the element to append (may be {@code null})
     */
    public void add(T value) {
        ensureCapacity(size + 1);
        data[size++] = value;
    }

    /**
     * Inserts {@code value} at position {@code index}, shifting subsequent elements right.
     * Appending at {@code index == size} is allowed (equivalent to {@link #add(Object)}).
     *
     * <p>Big-O: O(n)
     *
     * @param index position in [0, size]
     * @param value the element to insert (may be {@code null})
     * @throws IndexOutOfBoundsException if {@code index < 0 || index > size}
     */
    public void add(int index, T value) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        // Shift elements from index..size-1 one position to the right
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = value;
        size++;
    }

    /**
     * Removes and returns the element at {@code index}, shifting subsequent elements left.
     *
     * <p>Big-O: O(n)
     *
     * @param index position in [0, size)
     * @return the element that was removed
     * @throws IndexOutOfBoundsException if {@code index < 0 || index >= size}
     */
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        checkIndex(index);
        T removed = (T) data[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(data, index + 1, data, index, numMoved);
        }
        data[--size] = null; // allow GC
        shrinkIfNeeded();
        return removed;
    }

    /**
     * Returns the index of the first occurrence of {@code value} using
     * {@code Objects.equals} semantics, or -1 if not found.
     *
     * <p>Big-O: O(n)
     *
     * @param value the element to search for (may be {@code null})
     * @return first index where element equals {@code value}, or -1
     */
    public int indexOf(T value) {
        for (int i = 0; i < size; i++) {
            if (java.util.Objects.equals(data[i], value)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns {@code true} if {@code value} is present in the array.
     *
     * <p>Big-O: O(n)
     *
     * @param value the element to test (may be {@code null})
     */
    public boolean contains(T value) {
        return indexOf(value) >= 0;
    }

    /**
     * Removes all elements. The backing array is reset to the default capacity.
     *
     * <p>Big-O: O(n) — nulls out old references for GC
     */
    public void clear() {
        Arrays.fill(data, 0, size, null);
        size = 0;
    }

    // -----------------------------------------------------------------------
    // Internal helpers
    // -----------------------------------------------------------------------

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " out of bounds for size " + size);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " out of bounds for add with size " + size);
        }
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > data.length) {
            int newCapacity = Math.max(data.length * 2, minCapacity);
            data = Arrays.copyOf(data, newCapacity);
        }
    }

    /**
     * Halve the backing array when it is only 1/4 full, down to MIN_CAPACITY.
     * This prevents thrashing on repeated add/remove at the boundary.
     */
    private void shrinkIfNeeded() {
        if (size > 0 && size <= data.length / 4 && data.length / 2 >= MIN_CAPACITY) {
            data = Arrays.copyOf(data, data.length / 2);
        }
    }

    // -----------------------------------------------------------------------
    // Object overrides
    // -----------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }
}
