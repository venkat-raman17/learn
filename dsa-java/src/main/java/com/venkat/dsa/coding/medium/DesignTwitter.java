package com.venkat.dsa.coding.medium;

import java.util.List;

/**
 * NeetCode / LeetCode — Design Twitter
 *
 * <p>Difficulty: MEDIUM
 * <p>Pattern: Heap / Priority Queue
 * <p>URL: https://leetcode.com/problems/design-twitter/
 *
 * <p>Design a simplified version of Twitter where users can post tweets, follow/unfollow other
 * users, and retrieve the 10 most recent tweets in their news feed. The news feed includes
 * tweets from the user and all users they follow, ordered from most recent to least recent.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 &lt;= userId, followerId, followeeId &lt;= 500</li>
 *   <li>0 &lt;= tweetId &lt;= 10^4</li>
 *   <li>All twitter IDs are unique.</li>
 *   <li>At most 3 * 10^4 calls will be made.</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>
 * Example 1:
 *   twitter = new Twitter()
 *   twitter.postTweet(1, 5)
 *   twitter.getNewsFeed(1)   -> [5]
 *   twitter.follow(1, 2)
 *   twitter.postTweet(2, 6)
 *   twitter.getNewsFeed(1)   -> [6, 5]
 *   twitter.unfollow(1, 2)
 *   twitter.getNewsFeed(1)   -> [5]
 * </pre>
 *
 * <p>Target: Time O(n + k log n) for getNewsFeed (n = followees, k = 10), Space O(users + tweets)
 *
 * <p>Hint 1: Store each user's tweet list in insertion order with a global timestamp; a max-heap merges the heads of each followee's list.
 * <p>Hint 2: Keep each followee's list as a pointer so you can push the next tweet from that list when you pop the top.
 */
public class DesignTwitter {

    public DesignTwitter() {
        throw new UnsupportedOperationException("implement me");
    }

    public void postTweet(int userId, int tweetId) {
        throw new UnsupportedOperationException("implement me");
    }

    public List<Integer> getNewsFeed(int userId) {
        throw new UnsupportedOperationException("implement me");
    }

    public void follow(int followerId, int followeeId) {
        throw new UnsupportedOperationException("implement me");
    }

    public void unfollow(int followerId, int followeeId) {
        throw new UnsupportedOperationException("implement me");
    }
}
