package com.venkat.dsa.coding.medium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("practice — delete when you start")
public class PartitionLabelsTest {

    private final PartitionLabels solution = new PartitionLabels();

    @Test
    void testExample1() {
        assertEquals(List.of(9, 7, 8), solution.partitionLabels("ababcbacadefegdehijhklij"));
    }

    @Test
    void testExample2() {
        assertEquals(List.of(10), solution.partitionLabels("eccbbbbdec"));
    }

    @Test
    void testSingleChar() {
        assertEquals(List.of(1), solution.partitionLabels("a"));
    }
}
