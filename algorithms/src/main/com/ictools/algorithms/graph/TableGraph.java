package com.ictools.algorithms.graph;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.Optional;

public class TableGraph implements WeightedGraph {

    private final Table<Long, Long, Long> graph;

    public TableGraph() {
        this(HashBasedTable.create());
    }

    public TableGraph(Table<Long, Long, Long> graph) {
        this.graph = graph;
    }

    @Override
    public Optional<Long> getWeight(long a, long b) {
        Long result = graph.get(a, b);
        return result == null ? Optional.empty() : Optional.of(result);
    }

    @Override
    public boolean isConnected(long a, long b) {
        return graph.contains(a, b);
    }
}
