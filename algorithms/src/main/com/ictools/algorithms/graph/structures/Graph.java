package com.ictools.algorithms.graph.structures;

public interface Graph {
    // Returns true if there is an edge from a to b.
    // Note that this relationship is not necessarily symmetric (e.g. directed graphs)
    boolean isConnected(long a, long b);
}
