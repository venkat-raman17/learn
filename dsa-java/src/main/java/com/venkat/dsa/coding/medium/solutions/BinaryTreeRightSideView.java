package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Binary Tree Right Side View (LeetCode 199)
 *
 * <p>BFS level-order traversal: the last node dequeued in each level is the
 * rightmost node visible from the right side. We use the same "snapshot the
 * queue size" trick as level-order traversal and add the value of the last
 * node in each level to the result.
 *
 * <p><b>Time complexity:</b> O(n) — every node processed once.
 * <b>Space complexity:</b> O(n) — queue width at most n/2.
 *
 * <p><b>Key insight:</b> The right-side view value at each depth is simply the
 * last element of that depth's BFS batch.
 */
public class BinaryTreeRightSideView {

    public static class TreeNode {
        public int val;
        public TreeNode left, right;
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                // Only the last node in this level is visible from the right
                if (i == levelSize - 1) result.add(node.val);
                if (node.left  != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        return result;
    }
}
