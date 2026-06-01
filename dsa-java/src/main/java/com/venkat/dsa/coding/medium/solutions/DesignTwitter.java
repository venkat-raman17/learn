package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Design Twitter (LeetCode #355)
 *
 * <p>Approach: Each user owns a list of (timestamp, tweetId) pairs stored in
 * insertion order. {@code getNewsFeed} merges at most 10 tweets across the
 * user's own list and all followees' lists using a max-heap seeded with the
 * latest tweet of each participant. This is the classic "merge k sorted lists"
 * pattern applied to tweet streams.
 *
 * <p>Key insight: By storing tweets in chronological order and seeding the heap
 * with only the latest tweet per user, we lazily traverse each stream and avoid
 * loading all tweets into memory.
 *
 * <p>Time complexity:  postTweet O(1); follow/unfollow O(1); getNewsFeed O(k log k)
 *                      where k = number of followees + 1 (≤ total users).
 * Space complexity: O(total tweets + follow relationships).
 */
public class DesignTwitter {

    private int timestamp;
    private final Map<Integer, List<int[]>> tweets;   // userId -> list of [time, tweetId]
    private final Map<Integer, Set<Integer>> follows;  // userId -> set of followeeIds

    public DesignTwitter() {
        timestamp = 0;
        tweets = new HashMap<>();
        follows = new HashMap<>();
    }

    public void postTweet(int userId, int tweetId) {
        tweets.computeIfAbsent(userId, k -> new ArrayList<>())
              .add(new int[]{timestamp++, tweetId});
    }

    public List<Integer> getNewsFeed(int userId) {
        // Max-heap: [timestamp, tweetId, userId, indexInTweetList]
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> b[0] - a[0]);

        // Seed the heap with the most recent tweet from each relevant user
        Set<Integer> candidates = new HashSet<>();
        candidates.add(userId);
        if (follows.containsKey(userId)) {
            candidates.addAll(follows.get(userId));
        }

        for (int uid : candidates) {
            List<int[]> userTweets = tweets.get(uid);
            if (userTweets != null && !userTweets.isEmpty()) {
                int idx = userTweets.size() - 1;
                int[] latest = userTweets.get(idx);
                // [time, tweetId, ownerId, current_index_in_list]
                maxHeap.offer(new int[]{latest[0], latest[1], uid, idx});
            }
        }

        List<Integer> feed = new ArrayList<>();
        while (!maxHeap.isEmpty() && feed.size() < 10) {
            int[] top = maxHeap.poll();
            feed.add(top[1]); // tweetId

            // Advance pointer to the previous (older) tweet for this user
            int nextIdx = top[3] - 1;
            if (nextIdx >= 0) {
                int uid = top[2];
                int[] prev = tweets.get(uid).get(nextIdx);
                maxHeap.offer(new int[]{prev[0], prev[1], uid, nextIdx});
            }
        }

        return feed;
    }

    public void follow(int followerId, int followeeId) {
        follows.computeIfAbsent(followerId, k -> new HashSet<>()).add(followeeId);
    }

    public void unfollow(int followerId, int followeeId) {
        if (follows.containsKey(followerId)) {
            follows.get(followerId).remove(followeeId);
        }
    }
}
