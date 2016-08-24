package com.ictools.algorithms.graph;

import java.util.Optional;

public interface WeightedGraph extends Graph {
    // Returns the weight between the node identified by a and that identified by b, provided it exists.
    Optional<Long> getWeight(long a, long b);
}
