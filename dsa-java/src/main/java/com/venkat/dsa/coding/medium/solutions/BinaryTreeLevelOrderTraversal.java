package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Binary Tree Level Order Traversal (LeetCode 102)
 *
 * <p>Standard BFS with a queue. We process the queue level by level: before
 * processing each level we record the current queue size (= number of nodes at
 * that level), dequeue exactly that many nodes, collect their values, and enqueue
 * their children. Each iteration of the outer loop corresponds to one level.
 *
 * <p><b>Time complexity:</b> O(n) — each node is enqueued and dequeued once.
 * <b>Space complexity:</b> O(n) — the queue holds at most one full level (~n/2
 * nodes in the last level of a complete tree).
 *
 * <p><b>Key insight:</b> Snapshotting the queue size before the inner loop lets
 * us cleanly separate levels without needing a sentinel marker.
 */
public class BinaryTreeLevelOrderTraversal {

    public static class TreeNode {
        public int val;
        public TreeNode left, right;
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();           // nodes in the current level
            List<Integer> level = new ArrayList<>(levelSize);

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left  != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            result.add(level);
        }
        return result;
    }
}
