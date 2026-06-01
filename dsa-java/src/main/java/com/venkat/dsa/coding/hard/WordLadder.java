package com.venkat.dsa.coding.hard;

import java.util.List;

/**
 * NeetCode / LeetCode — Word Ladder
 *
 * <p>Difficulty: HARD
 * <p>Pattern: Graphs
 * <p>URL: https://leetcode.com/problems/word-ladder/
 *
 * <p>Given a beginWord, endWord, and wordList, return the number of words in the shortest transformation
 * sequence from beginWord to endWord, such that each adjacent pair differs by exactly one letter and
 * every intermediate word must exist in wordList. Return 0 if no such sequence exists.
 *
 * <p>Constraints:
 * <ul>
 *   <li>1 <= beginWord.length <= 10</li>
 *   <li>endWord.length == beginWord.length</li>
 *   <li>1 <= wordList.length <= 5000</li>
 *   <li>wordList[i].length == beginWord.length</li>
 *   <li>All words consist of lowercase English letters</li>
 *   <li>beginWord != endWord</li>
 * </ul>
 *
 * <p>Example 1:
 * <pre>
 * Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log","cog"]
 * Output: 5  ("hit" -> "hot" -> "dot" -> "dog" -> "cog")
 * </pre>
 *
 * <p>Example 2:
 * <pre>
 * Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log"]
 * Output: 0  (endWord not in list)
 * </pre>
 *
 * <p>Target: Time O(M^2 * N) where M=word length, N=wordList size; Space O(M^2 * N)
 *
 * <p>Hint 1: BFS level by level — each level represents one transformation step; find all words differing by one char.
 * <p>Hint 2: Use a Set for O(1) lookups; for each word in the queue, try changing each character to 'a'-'z' and check the set.
 */
public class WordLadder {

    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        throw new UnsupportedOperationException("implement me");
    }
}
