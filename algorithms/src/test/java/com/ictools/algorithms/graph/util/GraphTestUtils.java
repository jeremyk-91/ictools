package com.ictools.algorithms.graph.util;

import com.google.common.collect.Lists;
import javafx.util.Pair;

import java.util.List;

public final class GraphTestUtils {
    private GraphTestUtils() {
        // utility
    }

    public static List<Pair<Long, Long>> buildPath(long... longs) {
        List<Pair<Long, Long>> pairList = Lists.newArrayList();
        for (int i = 0; i < longs.length - 1; i++) {
            pairList.add(new Pair<>(longs[i], longs[i+1]));
        }
        return pairList;
    }
}
