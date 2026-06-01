package com.venkat.dsa.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Binary Search Tree (BST) — a node-based binary tree where every node's key is
 * greater than all keys in its left subtree and less than all keys in its right
 * subtree.
 *
 * <p><b>Backing representation:</b> Linked nodes ({@code Node<T>}), each holding
 * a value and references to left/right children. No sentinel nodes; an empty tree
 * is represented by a {@code null} root.
 *
 * <p><b>Invariant:</b> For every node {@code n}:
 * {@code left.value < n.value < right.value} (strict inequality; duplicates are
 * silently ignored on insert).
 *
 * <h2>Operations — Time &amp; Space Complexity</h2>
 * <pre>
 * Operation   Average       Worst (skewed)   Extra space
 * ─────────── ───────────── ──────────────── ───────────
 * insert      O(log n)      O(n)             O(h) stack
 * contains    O(log n)      O(n)             O(h) stack
 * delete      O(log n)      O(n)             O(h) stack
 * min / max   O(log n)      O(n)             O(1)
 * inorder     O(n)          O(n)             O(n) list
 * size        O(1)          O(1)             O(1)
 * isEmpty     O(1)          O(1)             O(1)
 * </pre>
 * where {@code h} is tree height (log n balanced, n worst-case).
 *
 * <p><b>When to use:</b> Dynamic sorted data with frequent search, insert, and
 * delete where average-case O(log n) is acceptable. Prefer a self-balancing
 * variant (AVL, Red-Black) when worst-case O(n) on skewed input is unacceptable.
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Inorder traversal of a BST yields elements in ascending sorted order —
 *       essential for "kth smallest" and "validate BST" problems.</li>
 *   <li>Deletion of a node with two children replaces its value with the
 *       <em>inorder successor</em> (leftmost node in the right subtree) then
 *       removes that successor from the right subtree.</li>
 *   <li>A BST degrades to a linked list on already-sorted input — always mention
 *       balancing in interviews.</li>
 * </ul>
 *
 * @param <T> the element type; must implement {@link Comparable}
 */
public class BinarySearchTree<T extends Comparable<T>> {

    // ── Inner node ────────────────────────────────────────────────────────────

    private static class Node<T> {
        T value;
        Node<T> left;
        Node<T> right;

        Node(T value) {
            this.value = value;
        }
    }

    // ── Fields ────────────────────────────────────────────────────────────────

    private Node<T> root;
    private int size;

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Returns {@code true} if the tree contains no elements.
     *
     * <p><b>O(1)</b> time and space.
     *
     * @return {@code true} if empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements stored in the tree.
     *
     * <p><b>O(1)</b> time and space.
     *
     * @return element count
     */
    public int size() {
        return size;
    }

    /**
     * Inserts {@code value} into the tree. If the value already exists the tree
     * is left unchanged (duplicates are silently ignored).
     *
     * <p><b>O(h)</b> time where {@code h} is the tree height; O(h) stack space.
     *
     * @param value the value to insert; must not be {@code null}
     * @throws IllegalArgumentException if {@code value} is {@code null}
     */
    public void insert(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Null values are not permitted.");
        }
        root = insertRec(root, value);
    }

    /**
     * Returns {@code true} if {@code value} exists in the tree.
     *
     * <p><b>O(h)</b> time; O(h) stack space.
     *
     * @param value the value to search for; must not be {@code null}
     * @return {@code true} if found
     * @throws IllegalArgumentException if {@code value} is {@code null}
     */
    public boolean contains(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Null values are not permitted.");
        }
        return containsRec(root, value);
    }

    /**
     * Removes {@code value} from the tree if present; otherwise does nothing.
     * Handles all three cases:
     * <ol>
     *   <li>Leaf node — simply removed.</li>
     *   <li>One child — the child replaces the deleted node.</li>
     *   <li>Two children — replaced with the inorder successor (minimum of the
     *       right subtree), which is then deleted from the right subtree.</li>
     * </ol>
     *
     * <p><b>O(h)</b> time; O(h) stack space.
     *
     * @param value the value to remove; must not be {@code null}
     * @throws IllegalArgumentException if {@code value} is {@code null}
     */
    public void delete(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Null values are not permitted.");
        }
        root = deleteRec(root, value);
    }

    /**
     * Returns the minimum (leftmost) value in the tree.
     *
     * <p><b>O(h)</b> time; O(1) space.
     *
     * @return the minimum value
     * @throws NoSuchElementException if the tree is empty
     */
    public T min() {
        if (root == null) {
            throw new NoSuchElementException("Tree is empty.");
        }
        Node<T> cur = root;
        while (cur.left != null) {
            cur = cur.left;
        }
        return cur.value;
    }

    /**
     * Returns the maximum (rightmost) value in the tree.
     *
     * <p><b>O(h)</b> time; O(1) space.
     *
     * @return the maximum value
     * @throws NoSuchElementException if the tree is empty
     */
    public T max() {
        if (root == null) {
            throw new NoSuchElementException("Tree is empty.");
        }
        Node<T> cur = root;
        while (cur.right != null) {
            cur = cur.right;
        }
        return cur.value;
    }

    /**
     * Returns all elements in ascending sorted order via inorder traversal.
     *
     * <p><b>O(n)</b> time; O(n) space for the returned list plus O(h) call stack.
     *
     * @return a new {@link List} containing every element in ascending order
     */
    public List<T> inorder() {
        List<T> result = new ArrayList<>(size);
        inorderRec(root, result);
        return result;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private Node<T> insertRec(Node<T> node, T value) {
        if (node == null) {
            size++;
            return new Node<>(value);
        }
        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            node.left = insertRec(node.left, value);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, value);
        }
        // cmp == 0 → duplicate; ignore
        return node;
    }

    private boolean containsRec(Node<T> node, T value) {
        if (node == null) {
            return false;
        }
        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            return containsRec(node.left, value);
        } else if (cmp > 0) {
            return containsRec(node.right, value);
        }
        return true;
    }

    private Node<T> deleteRec(Node<T> node, T value) {
        if (node == null) {
            return null; // value not found
        }
        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            node.left = deleteRec(node.left, value);
        } else if (cmp > 0) {
            node.right = deleteRec(node.right, value);
        } else {
            // Found the node to delete
            if (node.left == null) {
                // Leaf (right is also null) or one right child
                size--;
                return node.right;
            }
            if (node.right == null) {
                // One left child
                size--;
                return node.left;
            }
            // Two children: find inorder successor (min of right subtree)
            T successorValue = findMinValue(node.right);
            // Replace current node's value with successor's value
            node.value = successorValue;
            // Delete the successor from the right subtree
            node.right = deleteRec(node.right, successorValue);
        }
        return node;
    }

    /**
     * Returns the value of the leftmost (minimum) node in the subtree rooted
     * at {@code node}. Caller must ensure {@code node} is non-null.
     */
    private T findMinValue(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.value;
    }

    private void inorderRec(Node<T> node, List<T> result) {
        if (node == null) {
            return;
        }
        inorderRec(node.left, result);
        result.add(node.value);
        inorderRec(node.right, result);
    }
}
