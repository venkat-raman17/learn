package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@Disabled("practice — delete when you start")
public class DesignTwitterTest {

    @Test
    void testExample1() {
        DesignTwitter twitter = new DesignTwitter();
        twitter.postTweet(1, 5);
        assertEquals(List.of(5), twitter.getNewsFeed(1));
        twitter.follow(1, 2);
        twitter.postTweet(2, 6);
        assertEquals(List.of(6, 5), twitter.getNewsFeed(1));
        twitter.unfollow(1, 2);
        assertEquals(List.of(5), twitter.getNewsFeed(1));
    }

    @Test
    void testFeedLimitedToTen() {
        DesignTwitter twitter = new DesignTwitter();
        for (int i = 1; i <= 11; i++) {
            twitter.postTweet(1, i);
        }
        List<Integer> feed = twitter.getNewsFeed(1);
        assertEquals(10, feed.size());
        assertEquals(11, feed.get(0)); // most recent first
    }

    @Test
    void testUserSeesOwnTweetsAndFolloweeTweets() {
        DesignTwitter twitter = new DesignTwitter();
        twitter.postTweet(1, 100);
        twitter.postTweet(2, 200);
        twitter.follow(1, 2);
        List<Integer> feed = twitter.getNewsFeed(1);
        assertTrue(feed.contains(100));
        assertTrue(feed.contains(200));
    }
}
