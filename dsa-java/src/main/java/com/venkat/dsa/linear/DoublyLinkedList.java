package com.venkat.dsa.linear;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Doubly Linked List — a linear data structure where each node holds a value
 * and pointers to both the previous and next nodes in the sequence.
 *
 * <p><b>Backing representation:</b> A chain of {@code Node<T>} objects linked
 * via {@code prev} and {@code next} references, anchored by {@code head} (first
 * node) and {@code tail} (last node). An integer {@code size} tracks the count.</p>
 *
 * <p><b>Invariants:</b>
 * <ul>
 *   <li>{@code size == 0} iff {@code head == null} and {@code tail == null}.</li>
 *   <li>When {@code size == 1}, {@code head == tail} and both {@code prev}/{@code next} are {@code null}.</li>
 *   <li>{@code head.prev == null} and {@code tail.next == null} at all times.</li>
 *   <li>For every interior node {@code n}: {@code n.prev.next == n} and {@code n.next.prev == n}.</li>
 * </ul>
 * </p>
 *
 * <p><b>Operations:</b></p>
 * <pre>
 * Operation      Time        Space (extra)
 * -----------------------------------------
 * addFirst       O(1)        O(1)
 * addLast        O(1)        O(1)
 * removeFirst    O(1)        O(1)
 * removeLast     O(1)        O(1)
 * peekFirst      O(1)        O(1)
 * peekLast       O(1)        O(1)
 * size           O(1)        O(1)
 * isEmpty        O(1)        O(1)
 * toList         O(n)        O(n)
 * </pre>
 *
 * <p><b>When to use:</b> Prefer a doubly linked list when you need O(1)
 * insertion/removal at both ends without the amortised cost of array shifting,
 * and when you do not need random access by index. It is the natural backing
 * structure for a deque, LRU cache, or browser history.</p>
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Draw the node pointers before coding; many bugs come from forgetting
 *       the {@code prev} link or mishandling the single-element edge case.</li>
 *   <li>Compared to a singly linked list, the extra {@code prev} pointer enables
 *       O(1) {@code removeLast} without traversal.</li>
 *   <li>{@link java.util.LinkedList} in the JDK is a doubly linked list and
 *       implements both {@code Deque} and {@code List}.</li>
 * </ul>
 * </p>
 *
 * @param <T> the type of elements held in this list
 */
public class DoublyLinkedList<T> {

    // -------------------------------------------------------------------------
    // Internal node
    // -------------------------------------------------------------------------

    private static final class Node<T> {
        T data;
        Node<T> prev;
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

    /** Constructs an empty doubly linked list. */
    public DoublyLinkedList() {
        // head, tail default to null; size defaults to 0
    }

    // -------------------------------------------------------------------------
    // Mutating operations
    // -------------------------------------------------------------------------

    /**
     * Inserts {@code value} at the front of the list.
     *
     * <p>Time: O(1) &nbsp;|&nbsp; Space: O(1)</p>
     *
     * @param value the element to insert; may be {@code null}
     */
    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
    }

    /**
     * Inserts {@code value} at the back of the list.
     *
     * <p>Time: O(1) &nbsp;|&nbsp; Space: O(1)</p>
     *
     * @param value the element to insert; may be {@code null}
     */
    public void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (tail == null) {
            head = tail = node;
        } else {
            node.prev = tail;
            tail.next = node;
            tail = node;
        }
        size++;
    }

    /**
     * Removes and returns the first element.
     *
     * <p>Time: O(1) &nbsp;|&nbsp; Space: O(1)</p>
     *
     * @return the removed element
     * @throws NoSuchElementException if the list is empty
     */
    public T removeFirst() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        T data = head.data;
        if (head == tail) {
            // single element
            head = tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size--;
        return data;
    }

    /**
     * Removes and returns the last element.
     *
     * <p>Time: O(1) &nbsp;|&nbsp; Space: O(1)</p>
     *
     * @return the removed element
     * @throws NoSuchElementException if the list is empty
     */
    public T removeLast() {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");
        }
        T data = tail.data;
        if (head == tail) {
            // single element
            head = tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return data;
    }

    // -------------------------------------------------------------------------
    // Query operations
    // -------------------------------------------------------------------------

    /**
     * Returns (without removing) the first element.
     *
     * <p>Time: O(1) &nbsp;|&nbsp; Space: O(1)</p>
     *
     * @return the first element
     * @throws NoSuchElementException if the list is empty
     */
    public T peekFirst() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        return head.data;
    }

    /**
     * Returns (without removing) the last element.
     *
     * <p>Time: O(1) &nbsp;|&nbsp; Space: O(1)</p>
     *
     * @return the last element
     * @throws NoSuchElementException if the list is empty
     */
    public T peekLast() {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");
        }
        return tail.data;
    }

    /**
     * Returns the number of elements in the list.
     *
     * <p>Time: O(1) &nbsp;|&nbsp; Space: O(1)</p>
     *
     * @return element count, always &ge; 0
     */
    public int size() {
        return size;
    }

    /**
     * Returns {@code true} if the list contains no elements.
     *
     * <p>Time: O(1) &nbsp;|&nbsp; Space: O(1)</p>
     *
     * @return {@code true} if size is zero
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns a snapshot {@link List} of all elements in front-to-back order.
     *
     * <p>Time: O(n) &nbsp;|&nbsp; Space: O(n)</p>
     *
     * @return a new {@link ArrayList} containing every element in order
     */
    public List<T> toList() {
        List<T> result = new ArrayList<>(size);
        Node<T> current = head;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }
}
