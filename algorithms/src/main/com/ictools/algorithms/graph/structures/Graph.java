package com.ictools.algorithms.graph.structures;

import javafx.util.Pair;

import java.util.Set;

public interface Graph {
    // Returns true if there is an edge from a to b.
    // Note that this relationship is not necessarily symmetric (e.g. directed graphs)
    boolean isAdjacent(long a, long b);

    // Returns every identifier that is mapped to a node in this graph.
    Set<Long> getNodes();

    // Returns a set of every edge in this graph.
    Set<Pair<Long, Long>> getEdges();
}
