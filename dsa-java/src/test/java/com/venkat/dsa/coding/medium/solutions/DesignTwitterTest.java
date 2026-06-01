package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DesignTwitterTest {

    // Official example (LeetCode #355):
    // postTweet(1,5), getNewsFeed(1)->[5],
    // follow(1,2), postTweet(2,6), getNewsFeed(1)->[6,5],
    // unfollow(1,2), getNewsFeed(1)->[5]
    @Test
    void testOfficialExample() {
        DesignTwitter twitter = new DesignTwitter();
        twitter.postTweet(1, 5);
        List<Integer> feed1 = twitter.getNewsFeed(1);
        assertEquals(List.of(5), feed1);

        twitter.follow(1, 2);
        twitter.postTweet(2, 6);
        List<Integer> feed2 = twitter.getNewsFeed(1);
        // Most recent first: tweet 6 was posted after tweet 5
        assertEquals(List.of(6, 5), feed2);

        twitter.unfollow(1, 2);
        List<Integer> feed3 = twitter.getNewsFeed(1);
        assertEquals(List.of(5), feed3);
    }

    // User with no tweets and no followees gets empty feed
    @Test
    void testEmptyFeed() {
        DesignTwitter twitter = new DesignTwitter();
        List<Integer> feed = twitter.getNewsFeed(42);
        assertTrue(feed.isEmpty());
    }

    // Feed is capped at 10 most recent tweets
    @Test
    void testFeedCappedAtTen() {
        DesignTwitter twitter = new DesignTwitter();
        // Post 12 tweets as user 1
        for (int i = 1; i <= 12; i++) {
            twitter.postTweet(1, i);
        }
        List<Integer> feed = twitter.getNewsFeed(1);
        assertEquals(10, feed.size());
        // Most recent tweets: ids 12,11,...,3 (reverse chronological)
        assertEquals(12, feed.get(0));
        assertEquals(3,  feed.get(9));
    }

    // Interleaved tweets from multiple followees appear in recency order
    @Test
    void testInterleavedFollowees() {
        DesignTwitter twitter = new DesignTwitter();
        twitter.postTweet(1, 100); // t=0
        twitter.postTweet(2, 200); // t=1
        twitter.postTweet(1, 101); // t=2
        twitter.follow(3, 1);
        twitter.follow(3, 2);

        List<Integer> feed = twitter.getNewsFeed(3);
        // Recency order: 101(t=2), 200(t=1), 100(t=0)
        assertEquals(List.of(101, 200, 100), feed);
    }

    // Unfollowing a user not followed is a no-op
    @Test
    void testUnfollowNotFollowed() {
        DesignTwitter twitter = new DesignTwitter();
        twitter.postTweet(1, 10);
        assertDoesNotThrow(() -> twitter.unfollow(1, 99));
        assertEquals(List.of(10), twitter.getNewsFeed(1));
    }

    // User sees their own tweets even without explicit self-follow
    @Test
    void testSelfTweetsVisible() {
        DesignTwitter twitter = new DesignTwitter();
        twitter.postTweet(5, 77);
        twitter.postTweet(5, 88);
        List<Integer> feed = twitter.getNewsFeed(5);
        assertEquals(List.of(88, 77), feed);
    }

    // Following then re-following should not duplicate tweets
    @Test
    void testFollowFollowee_NoDuplicates() {
        DesignTwitter twitter = new DesignTwitter();
        twitter.postTweet(2, 42);
        twitter.follow(1, 2);
        twitter.follow(1, 2); // second follow is a no-op
        List<Integer> feed = twitter.getNewsFeed(1);
        assertEquals(List.of(42), feed);
    }
}
