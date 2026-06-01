package com.venkat.dsa.trees;

import java.util.ArrayList;
import java.util.List;

/**
 * AVL Tree — a self-balancing binary search tree.
 *
 * <p><b>Backing representation:</b> Linked {@code Node<T>} objects where each node
 * stores a value, left/right children, and the node's height. The tree is rooted
 * at a private {@code root} reference.</p>
 *
 * <p><b>Invariant:</b> For every node {@code n}, the balance factor
 * {@code height(n.left) - height(n.right)} is in {@code {-1, 0, 1}}. This
 * property is restored after every insert and delete via single or double
 * rotations (LL, RR, LR, RL).</p>
 *
 * <h2>Operations</h2>
 * <table border="1" cellpadding="4">
 *   <tr><th>Operation</th><th>Average Time</th><th>Worst Time</th><th>Space</th></tr>
 *   <tr><td>insert</td>    <td>O(log n)</td>   <td>O(log n)</td>  <td>O(1)*</td></tr>
 *   <tr><td>delete</td>    <td>O(log n)</td>   <td>O(log n)</td>  <td>O(1)*</td></tr>
 *   <tr><td>contains</td>  <td>O(log n)</td>   <td>O(log n)</td>  <td>O(1)*</td></tr>
 *   <tr><td>inorder</td>   <td>O(n)</td>        <td>O(n)</td>      <td>O(n)</td></tr>
 *   <tr><td>height</td>    <td>O(1)</td>        <td>O(1)</td>      <td>O(1)</td></tr>
 *   <tr><td>isBalanced</td><td>O(n)</td>        <td>O(n)</td>      <td>O(log n)**</td></tr>
 * </table>
 * <p>* Recursive call stack depth is O(log n); noted above is auxiliary heap space.<br>
 *    ** Stack space for recursion.</p>
 *
 * <h2>When to use</h2>
 * <p>Prefer an AVL tree over a plain BST when lookup-heavy workloads demand
 * guaranteed O(log n) worst-case search (e.g., in-memory symbol tables, sorted
 * dictionaries). Prefer a Red-Black tree when write-heavy workloads need fewer
 * rotations per mutation.</p>
 *
 * <h2>Interview notes</h2>
 * <ul>
 *   <li>Height is stored per node and updated bottom-up, making height queries O(1).</li>
 *   <li>Balance factor = height(left) - height(right). Value &gt; 1 → left-heavy (rotate right);
 *       value &lt; -1 → right-heavy (rotate left).</li>
 *   <li>Four rotation cases: LL (right rotation), RR (left rotation),
 *       LR (left-right double rotation), RL (right-left double rotation).</li>
 *   <li>Delete uses in-order successor (leftmost node of right subtree) for
 *       two-child removal, then rebalances on the way up.</li>
 *   <li>Duplicates are ignored (set semantics); adjust comparator if multiset
 *       semantics are needed.</li>
 * </ul>
 *
 * @param <T> the element type; must be {@link Comparable}
 */
public class AVLTree<T extends Comparable<T>> {

    // ------------------------------------------------------------------ Node

    private static final class Node<T> {
        T value;
        Node<T> left;
        Node<T> right;
        int height; // height of this node's subtree (leaf = 1)

        Node(T value) {
            this.value = value;
            this.height = 1;
        }
    }

    // ------------------------------------------------------------------ State

    private Node<T> root;
    private int size;

    // ------------------------------------------------------------------ Public API

    /**
     * Inserts {@code value} into the tree. Duplicate values are silently ignored
     * (set semantics).
     *
     * <p><b>Time:</b> O(log n) &nbsp; <b>Space:</b> O(log n) call stack</p>
     *
     * @param value the value to insert; must not be {@code null}
     * @throws IllegalArgumentException if {@code value} is {@code null}
     */
    public void insert(T value) {
        if (value == null) throw new IllegalArgumentException("Null values are not allowed");
        int before = size;
        root = insert(root, value);
        // size is incremented inside the recursive helper only for new nodes
    }

    /**
     * Deletes {@code value} from the tree. If the value is not present, the tree
     * is unchanged.
     *
     * <p><b>Time:</b> O(log n) &nbsp; <b>Space:</b> O(log n) call stack</p>
     *
     * @param value the value to delete; must not be {@code null}
     * @throws IllegalArgumentException if {@code value} is {@code null}
     */
    public void delete(T value) {
        if (value == null) throw new IllegalArgumentException("Null values are not allowed");
        root = delete(root, value);
    }

    /**
     * Returns {@code true} if {@code value} is present in the tree.
     *
     * <p><b>Time:</b> O(log n) &nbsp; <b>Space:</b> O(log n) call stack</p>
     *
     * @param value the value to search for; must not be {@code null}
     * @return {@code true} if found
     * @throws IllegalArgumentException if {@code value} is {@code null}
     */
    public boolean contains(T value) {
        if (value == null) throw new IllegalArgumentException("Null values are not allowed");
        return contains(root, value);
    }

    /**
     * Returns the elements of the tree in sorted (in-order) sequence.
     *
     * <p><b>Time:</b> O(n) &nbsp; <b>Space:</b> O(n)</p>
     *
     * @return a new {@link List} with elements in ascending order
     */
    public List<T> inorder() {
        List<T> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    /**
     * Returns the height of the tree (number of edges on the longest root-to-leaf
     * path). An empty tree has height {@code 0}; a single-node tree has height
     * {@code 1}.
     *
     * <p><b>Time:</b> O(1) &nbsp; <b>Space:</b> O(1)</p>
     *
     * @return the height of the tree
     */
    public int height() {
        return nodeHeight(root);
    }

    /**
     * Returns the number of elements stored in the tree.
     *
     * <p><b>Time:</b> O(1) &nbsp; <b>Space:</b> O(1)</p>
     *
     * @return the element count
     */
    public int size() {
        return size;
    }

    /**
     * Returns {@code true} if the tree contains no elements.
     *
     * <p><b>Time:</b> O(1) &nbsp; <b>Space:</b> O(1)</p>
     *
     * @return {@code true} when empty
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Verifies that the AVL balance invariant holds for every node in the tree.
     * Useful for testing and debugging.
     *
     * <p><b>Time:</b> O(n) &nbsp; <b>Space:</b> O(log n) call stack</p>
     *
     * @return {@code true} if the tree satisfies the AVL invariant
     */
    public boolean isBalanced() {
        return checkBalanced(root) != -1;
    }

    // ------------------------------------------------------------------ Private helpers — insert

    private Node<T> insert(Node<T> node, T value) {
        if (node == null) {
            size++;
            return new Node<>(value);
        }
        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            node.left = insert(node.left, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, value);
        } else {
            // duplicate — no change
            return node;
        }
        updateHeight(node);
        return rebalance(node);
    }

    // ------------------------------------------------------------------ Private helpers — delete

    private Node<T> delete(Node<T> node, T value) {
        if (node == null) return null; // value not found

        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            node.left = delete(node.left, value);
        } else if (cmp > 0) {
            node.right = delete(node.right, value);
        } else {
            // Found the node to delete
            size--;
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            // Two children: replace value with in-order successor, then delete successor
            Node<T> successor = minimum(node.right);
            node.value = successor.value;
            size++; // will be decremented again in the recursive delete below
            node.right = delete(node.right, successor.value);
        }
        updateHeight(node);
        return rebalance(node);
    }

    private Node<T> minimum(Node<T> node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // ------------------------------------------------------------------ Private helpers — contains

    private boolean contains(Node<T> node, T value) {
        if (node == null) return false;
        int cmp = value.compareTo(node.value);
        if (cmp < 0) return contains(node.left, value);
        if (cmp > 0) return contains(node.right, value);
        return true;
    }

    // ------------------------------------------------------------------ Private helpers — inorder

    private void inorder(Node<T> node, List<T> result) {
        if (node == null) return;
        inorder(node.left, result);
        result.add(node.value);
        inorder(node.right, result);
    }

    // ------------------------------------------------------------------ Private helpers — balance

    private int nodeHeight(Node<T> node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node<T> node) {
        node.height = 1 + Math.max(nodeHeight(node.left), nodeHeight(node.right));
    }

    private int balanceFactor(Node<T> node) {
        return node == null ? 0 : nodeHeight(node.left) - nodeHeight(node.right);
    }

    /**
     * Rebalances {@code node} if its balance factor is out of range, then returns
     * the (possibly new) root of this subtree.
     */
    private Node<T> rebalance(Node<T> node) {
        int bf = balanceFactor(node);

        // Left-heavy
        if (bf > 1) {
            if (balanceFactor(node.left) >= 0) {
                // LL case: single right rotation
                return rotateRight(node);
            } else {
                // LR case: left rotate left child, then right rotate node
                node.left = rotateLeft(node.left);
                return rotateRight(node);
            }
        }

        // Right-heavy
        if (bf < -1) {
            if (balanceFactor(node.right) <= 0) {
                // RR case: single left rotation
                return rotateLeft(node);
            } else {
                // RL case: right rotate right child, then left rotate node
                node.right = rotateRight(node.right);
                return rotateLeft(node);
            }
        }

        return node; // already balanced
    }

    /**
     * Right rotation around {@code y}:
     * <pre>
     *     y              x
     *    / \            / \
     *   x   T3   →    T1   y
     *  / \                / \
     * T1  T2            T2   T3
     * </pre>
     */
    private Node<T> rotateRight(Node<T> y) {
        Node<T> x = y.left;
        Node<T> t2 = x.right;
        x.right = y;
        y.left = t2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    /**
     * Left rotation around {@code x}:
     * <pre>
     *   x                y
     *  / \              / \
     * T1   y    →      x   T3
     *     / \         / \
     *    T2  T3      T1  T2
     * </pre>
     */
    private Node<T> rotateLeft(Node<T> x) {
        Node<T> y = x.right;
        Node<T> t2 = y.left;
        y.left = x;
        x.right = t2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    // ------------------------------------------------------------------ Private helpers — isBalanced

    /**
     * Returns the actual height of {@code node}'s subtree if balanced, or -1 if
     * any sub-tree violates the AVL invariant.
     */
    private int checkBalanced(Node<T> node) {
        if (node == null) return 0;
        int leftH = checkBalanced(node.left);
        if (leftH == -1) return -1;
        int rightH = checkBalanced(node.right);
        if (rightH == -1) return -1;
        int diff = leftH - rightH;
        if (diff < -1 || diff > 1) return -1;
        return 1 + Math.max(leftH, rightH);
    }
}
