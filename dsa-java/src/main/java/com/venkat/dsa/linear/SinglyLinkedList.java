package com.venkat.dsa.linear;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Generic singly linked list backed by a chain of {@code Node} objects.
 *
 * <p><b>Definition:</b> A linear data structure in which each element (node) holds a value and a
 * single pointer to the next node; the last node points to {@code null}.
 *
 * <p><b>Backing representation:</b> {@code head} points to the first node, {@code tail} points to
 * the last node (or {@code null} when empty), and {@code size} tracks the element count. No array
 * is used; storage is purely heap-allocated nodes.
 *
 * <p><b>Invariants:</b>
 * <ul>
 *   <li>{@code size == 0} iff {@code head == null} iff {@code tail == null}.
 *   <li>When {@code size == 1}, {@code head == tail} and {@code head.next == null}.
 *   <li>{@code tail.next} is always {@code null}.
 *   <li>Traversing from {@code head} via {@code next} pointers yields exactly {@code size} nodes.
 * </ul>
 *
 * <p><b>Operations (n = number of elements):</b>
 * <pre>
 * +--------------+----------+------------+
 * | Operation    | Time     | Space      |
 * +--------------+----------+------------+
 * | addFirst     | O(1)     | O(1)       |
 * | addLast      | O(1)     | O(1)       |
 * | removeFirst  | O(1)     | O(1)       |
 * | get(i)       | O(n)     | O(1)       |
 * | indexOf      | O(n)     | O(1)       |
 * | contains     | O(n)     | O(1)       |
 * | reverse      | O(n)     | O(1)       |
 * | size         | O(1)     | O(1)       |
 * | isEmpty      | O(1)     | O(1)       |
 * | toList       | O(n)     | O(n)       |
 * +--------------+----------+------------+
 * </pre>
 *
 * <p><b>When to use:</b> Prefer a singly linked list when you need fast O(1) prepend and removal
 * from the front and do not require random access by index. Good as a stack or queue building
 * block; poor for random access or backward traversal.
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Classic pointer-manipulation questions: reverse, detect cycle (Floyd's), find middle
 *       (two pointers), merge two sorted lists, remove Nth from end.
 *   <li>Always clarify whether the list can be empty before writing removal code.
 *   <li>Maintaining a {@code tail} pointer makes {@code addLast} O(1) instead of O(n).
 *   <li>In-place reverse uses three pointers (prev, curr, next) in a single pass.
 * </ul>
 *
 * @param <T> the type of elements held in this list
 */
public class SinglyLinkedList<T> {

    // -------------------------------------------------------------------------
    // Internal node
    // -------------------------------------------------------------------------

    private static final class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private Node<T> head;
    private Node<T> tail;
    private int size;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** Creates an empty singly linked list. */
    public SinglyLinkedList() {
        // head, tail = null; size = 0 by default
    }

    // -------------------------------------------------------------------------
    // Insertion
    // -------------------------------------------------------------------------

    /**
     * Inserts {@code value} at the front of the list.
     *
     * <p><b>Time:</b> O(1) &nbsp; <b>Space:</b> O(1)
     *
     * @param value the element to insert
     */
    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head = node;
        }
        size++;
    }

    /**
     * Appends {@code value} at the end of the list.
     *
     * <p><b>Time:</b> O(1) &nbsp; <b>Space:</b> O(1)
     *
     * @param value the element to append
     */
    public void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    // -------------------------------------------------------------------------
    // Removal
    // -------------------------------------------------------------------------

    /**
     * Removes and returns the first element.
     *
     * <p><b>Time:</b> O(1) &nbsp; <b>Space:</b> O(1)
     *
     * @return the value that was at the head of the list
     * @throws NoSuchElementException if the list is empty
     */
    public T removeFirst() {
        if (head == null) {
            throw new NoSuchElementException("Cannot remove from an empty list.");
        }
        T value = head.data;
        head = head.next;
        if (head == null) {
            tail = null; // list is now empty
        }
        size--;
        return value;
    }

    // -------------------------------------------------------------------------
    // Access
    // -------------------------------------------------------------------------

    /**
     * Returns the element at position {@code index} (0-based).
     *
     * <p><b>Time:</b> O(n) &nbsp; <b>Space:</b> O(1)
     *
     * @param index zero-based position
     * @return the element at {@code index}
     * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index >= size}
     */
    public T get(int index) {
        checkIndex(index);
        Node<T> curr = head;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr.data;
    }

    /**
     * Returns the index of the first occurrence of {@code value}, or {@code -1} if absent.
     *
     * <p><b>Time:</b> O(n) &nbsp; <b>Space:</b> O(1)
     *
     * @param value the value to search for (may be {@code null})
     * @return first index of {@code value}, or {@code -1}
     */
    public int indexOf(T value) {
        int index = 0;
        for (Node<T> curr = head; curr != null; curr = curr.next) {
            if (equals(curr.data, value)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Returns {@code true} if the list contains at least one element equal to {@code value}.
     *
     * <p><b>Time:</b> O(n) &nbsp; <b>Space:</b> O(1)
     *
     * @param value the value to search for (may be {@code null})
     * @return {@code true} if found, {@code false} otherwise
     */
    public boolean contains(T value) {
        return indexOf(value) != -1;
    }

    // -------------------------------------------------------------------------
    // Mutation
    // -------------------------------------------------------------------------

    /**
     * Reverses the list in place using three-pointer iteration.
     *
     * <p><b>Time:</b> O(n) &nbsp; <b>Space:</b> O(1)
     */
    public void reverse() {
        if (size <= 1) {
            return;
        }
        tail = head; // old head will become new tail
        Node<T> prev = null;
        Node<T> curr = head;
        while (curr != null) {
            Node<T> next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        head = prev;
    }

    // -------------------------------------------------------------------------
    // Query helpers
    // -------------------------------------------------------------------------

    /**
     * Returns the number of elements in the list.
     *
     * <p><b>Time:</b> O(1) &nbsp; <b>Space:</b> O(1)
     *
     * @return element count
     */
    public int size() {
        return size;
    }

    /**
     * Returns {@code true} if the list contains no elements.
     *
     * <p><b>Time:</b> O(1) &nbsp; <b>Space:</b> O(1)
     *
     * @return {@code true} when size is 0
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns a new {@link java.util.List} containing all elements in list order.
     *
     * <p><b>Time:</b> O(n) &nbsp; <b>Space:</b> O(n)
     *
     * @return snapshot list of elements from head to tail
     */
    public List<T> toList() {
        List<T> result = new ArrayList<>(size);
        for (Node<T> curr = head; curr != null; curr = curr.next) {
            result.add(curr.data);
        }
        return result;
    }

    // -------------------------------------------------------------------------
    // Object overrides
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> curr = head;
        while (curr != null) {
            sb.append(curr.data);
            if (curr.next != null) {
                sb.append(" -> ");
            }
            curr = curr.next;
        }
        sb.append("]");
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " out of bounds for size " + size);
        }
    }

    /** Null-safe equality check. */
    private boolean equals(T a, T b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
}
