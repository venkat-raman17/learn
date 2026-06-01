package com.venkat.dsa.trees;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Generic Binary Tree — an acyclic, hierarchical data structure in which each node has
 * at most two children, referred to as the left child and the right child.
 *
 * <p><b>Backing representation:</b> A linked structure of {@link Node} objects. Each node
 * holds a value and nullable references to its left and right children. The tree is accessed
 * via a single {@code root} reference; a {@code null} root represents an empty tree.
 *
 * <p><b>Invariant(s):</b>
 * <ul>
 *   <li>Every node has at most two children (left, right).</li>
 *   <li>There are no cycles — the structure is a proper rooted tree.</li>
 *   <li>Node values may be {@code null} if {@code T} permits it (no null-check is enforced
 *       by this class; callers are responsible for consistent null handling).</li>
 * </ul>
 *
 * <p><b>Operations table:</b>
 * <pre>
 * Operation        Time (average)   Time (worst)   Space (aux)
 * ─────────────────────────────────────────────────────────────
 * preorder()       O(n)             O(n)           O(h) stack
 * inorder()        O(n)             O(n)           O(h) stack
 * postorder()      O(n)             O(n)           O(h) stack
 * levelOrder()     O(n)             O(n)           O(w) queue  (w = max width)
 * height()         O(n)             O(n)           O(h) stack
 * size()           O(n)             O(n)           O(h) stack
 *
 * n = number of nodes, h = height of tree, w = maximum width (nodes on widest level)
 * </pre>
 *
 * <p><b>When to use:</b> Use a plain binary tree when you need to model hierarchical
 * relationships (e.g., expression trees, parse trees, decision trees) without the ordering
 * constraint of a BST or the balance guarantee of AVL/Red-Black trees.
 *
 * <p><b>Interview notes:</b>
 * <ul>
 *   <li>Know all four traversal strategies (pre/in/post/level) and their iterative forms.</li>
 *   <li>Inorder of a <em>BST</em> yields sorted output — this tree makes no such guarantee.</li>
 *   <li>Height of an empty tree is conventionally -1; height of a single node is 0.</li>
 *   <li>Recursive DFS uses O(h) implicit stack space; in the worst case (skewed tree) h = n.</li>
 *   <li>BFS / level-order requires an explicit queue; space is O(w) where w can be n/2 at the
 *       last level of a complete tree — effectively O(n) worst-case.</li>
 * </ul>
 *
 * @param <T> the type of value stored in each node
 */
public class BinaryTree<T> {

    // -----------------------------------------------------------------------
    // Nested Node class
    // -----------------------------------------------------------------------

    /**
     * A single node in the binary tree.
     *
     * @param <T> the type of value stored in this node
     */
    public static final class Node<T> {
        /** The value held by this node. */
        public T value;
        /** Reference to the left child, or {@code null} if none. */
        public Node<T> left;
        /** Reference to the right child, or {@code null} if none. */
        public Node<T> right;

        /**
         * Constructs a leaf node with the given value and no children.
         *
         * @param value the value to store
         */
        public Node(T value) {
            this.value = value;
        }

        /**
         * Constructs a node with the given value and explicit children.
         *
         * @param value the value to store
         * @param left  the left child (may be {@code null})
         * @param right the right child (may be {@code null})
         */
        public Node(T value, Node<T> left, Node<T> right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    // -----------------------------------------------------------------------
    // Fields
    // -----------------------------------------------------------------------

    /** The root of the tree; {@code null} represents an empty tree. */
    private final Node<T> root;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    /**
     * Constructs a binary tree with the given root node.
     *
     * @param root the root node, or {@code null} for an empty tree
     */
    public BinaryTree(Node<T> root) {
        this.root = root;
    }

    // -----------------------------------------------------------------------
    // Traversals
    // -----------------------------------------------------------------------

    /**
     * Returns the nodes' values in pre-order (root, left subtree, right subtree).
     *
     * <p>Time: O(n) — every node is visited exactly once.
     * Space: O(h) auxiliary stack space for recursion.
     *
     * @return a new {@link List} containing values in pre-order; empty if the tree is empty
     */
    public List<T> preorder() {
        List<T> result = new ArrayList<>();
        preorderHelper(root, result);
        return result;
    }

    private void preorderHelper(Node<T> node, List<T> result) {
        if (node == null) return;
        result.add(node.value);
        preorderHelper(node.left, result);
        preorderHelper(node.right, result);
    }

    /**
     * Returns the nodes' values in in-order (left subtree, root, right subtree).
     *
     * <p>Time: O(n) — every node is visited exactly once.
     * Space: O(h) auxiliary stack space for recursion.
     *
     * @return a new {@link List} containing values in in-order; empty if the tree is empty
     */
    public List<T> inorder() {
        List<T> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private void inorderHelper(Node<T> node, List<T> result) {
        if (node == null) return;
        inorderHelper(node.left, result);
        result.add(node.value);
        inorderHelper(node.right, result);
    }

    /**
     * Returns the nodes' values in post-order (left subtree, right subtree, root).
     *
     * <p>Time: O(n) — every node is visited exactly once.
     * Space: O(h) auxiliary stack space for recursion.
     *
     * @return a new {@link List} containing values in post-order; empty if the tree is empty
     */
    public List<T> postorder() {
        List<T> result = new ArrayList<>();
        postorderHelper(root, result);
        return result;
    }

    private void postorderHelper(Node<T> node, List<T> result) {
        if (node == null) return;
        postorderHelper(node.left, result);
        postorderHelper(node.right, result);
        result.add(node.value);
    }

    /**
     * Returns the nodes' values level by level, left to right (breadth-first order).
     *
     * <p>Time: O(n) — every node is enqueued and dequeued exactly once.
     * Space: O(w) auxiliary queue space, where w is the maximum width of the tree
     * (O(n) worst case for a complete tree).
     *
     * @return a new {@link List} containing values in level-order; empty if the tree is empty
     */
    public List<T> levelOrder() {
        List<T> result = new ArrayList<>();
        if (root == null) return result;

        Queue<Node<T>> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node<T> current = queue.poll();
            result.add(current.value);
            if (current.left != null) queue.offer(current.left);
            if (current.right != null) queue.offer(current.right);
        }

        return result;
    }

    // -----------------------------------------------------------------------
    // Structural queries
    // -----------------------------------------------------------------------

    /**
     * Returns the height of the tree.
     *
     * <p>Height is defined as the number of edges on the longest path from the root to a leaf.
     * An empty tree has height {@code -1}; a single-node tree has height {@code 0}.
     *
     * <p>Time: O(n) — every node is visited exactly once.
     * Space: O(h) auxiliary stack space for recursion.
     *
     * @return the height of the tree, or {@code -1} if the tree is empty
     */
    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(Node<T> node) {
        if (node == null) return -1;
        int leftHeight = heightHelper(node.left);
        int rightHeight = heightHelper(node.right);
        return 1 + Math.max(leftHeight, rightHeight);
    }

    /**
     * Returns the total number of nodes in the tree.
     *
     * <p>Time: O(n) — every node is visited exactly once.
     * Space: O(h) auxiliary stack space for recursion.
     *
     * @return the number of nodes, or {@code 0} if the tree is empty
     */
    public int size() {
        return sizeHelper(root);
    }

    private int sizeHelper(Node<T> node) {
        if (node == null) return 0;
        return 1 + sizeHelper(node.left) + sizeHelper(node.right);
    }
}
