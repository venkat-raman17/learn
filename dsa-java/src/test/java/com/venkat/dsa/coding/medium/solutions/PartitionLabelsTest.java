package com.venkat.dsa.coding.medium.solutions;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PartitionLabelsTest {

    private final PartitionLabels solution = new PartitionLabels();

    @Test
    void example1() {
        // "ababcbacadefegdehijhklij" -> [9,7,8]
        // Partition 1: a..a ends at 8 (index 8), size=9
        // Partition 2: d..e ends at 15, size=7
        // Partition 3: i..j ends at 23, size=8
        List<Integer> result = solution.partitionLabels("ababcbacadefegdehijhklij");
        assertEquals(List.of(9, 7, 8), result);
    }

    @Test
    void example2() {
        // "eccbbbbdec" -> [10]
        // 'e' last at 9, 'c' last at 8, 'd' last at 7 -> entire string one partition
        List<Integer> result = solution.partitionLabels("eccbbbbdec");
        assertEquals(List.of(10), result);
    }

    @Test
    void singleChar() {
        assertEquals(List.of(1), solution.partitionLabels("a"));
    }

    @Test
    void allDistinct() {
        // "abcde" — each character appears once, so each is its own partition
        assertEquals(List.of(1, 1, 1, 1, 1), solution.partitionLabels("abcde"));
    }

    @Test
    void allSameChar() {
        // "aaaa" — one partition of size 4
        assertEquals(List.of(4), solution.partitionLabels("aaaa"));
    }

    @Test
    void twoPartitions() {
        // "aabb" -> 'a' last at 1, 'b' last at 3 -> [2, 2]
        assertEquals(List.of(2, 2), solution.partitionLabels("aabb"));
    }

    @Test
    void overlappingExtensions() {
        // "abacbc" -> 'a' last=4, 'b' last=5, 'c' last=5; all in one partition of size 6
        assertEquals(List.of(6), solution.partitionLabels("abacbc"));
    }

    @Test
    void threePartitions() {
        // "caedbdedda" -> manual trace:
        // indices: c=0,a=1,e=2,d=3,b=4,d=5,e=6,d=7,d=8,a=9
        // last: c=0, a=9, e=6, d=8, b=4
        // i=0 char=c, end=max(0,0)=0, i==end -> partition size=1, start=1
        // i=1 char=a, end=max(0,9)=9, i!=end
        // i=2..8: end stays 9
        // i=9 char=a, end=max(9,9)=9, i==end -> partition size=9-1+1=9, start=10
        // Result: [1, 9]
        assertEquals(List.of(1, 9), solution.partitionLabels("caedbdedda"));
    }
}
