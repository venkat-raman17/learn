package com.venkat.dsa.coding.medium.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Encode and Decode Strings (LeetCode 271) — Medium
 *
 * <p>Approach: Use a length-prefixed encoding: each string is stored as
 * {@code "<length>#<string>"}. The decoder reads the integer before the {@code #}
 * delimiter to know exactly how many characters to consume, making the scheme
 * unambiguous even for strings that contain {@code #} themselves.
 *
 * <p><b>Time complexity:</b> O(n) for both encode and decode, where n is total characters
 * across all strings.<br>
 * <b>Space complexity:</b> O(n) for the encoded/decoded output.
 *
 * <p><b>Key insight:</b> A delimiter-only scheme breaks when the delimiter appears inside
 * a string; length prefixing sidesteps that by making boundaries self-describing.
 */
public class EncodeAndDecodeStrings {

    /**
     * Encodes a list of strings to a single string.
     * Format: {@code len1#str1len2#str2...}
     */
    public String encode(List<String> strs) {
        StringBuilder sb = new StringBuilder();
        for (String s : strs) {
            sb.append(s.length()).append('#').append(s);
        }
        return sb.toString();
    }

    /**
     * Decodes a single string back to the original list.
     */
    public List<String> decode(String s) {
        List<String> result = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            // find the '#' separator after the length digits
            int j = i;
            while (s.charAt(j) != '#') j++;
            int len = Integer.parseInt(s.substring(i, j));
            // extract exactly 'len' characters after '#'
            result.add(s.substring(j + 1, j + 1 + len));
            i = j + 1 + len;
        }
        return result;
    }
}
