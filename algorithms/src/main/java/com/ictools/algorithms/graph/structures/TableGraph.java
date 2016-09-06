package com.ictools.algorithms.graph.structures;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import javafx.util.Pair;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TableGraph implements WeightedGraph {

    private final Table<Long, Long, Long> graph;
    private final Set<Long> nodeCache;

    public TableGraph(Table<Long, Long, Long> graph) {
        this.graph = HashBasedTable.create(graph);
        this.nodeCache = Sets.newHashSet(graph.rowKeySet());
        nodeCache.addAll(graph.columnKeySet());
    }

    @Override
    public Optional<Long> getWeight(long a, long b) {
        Long result = graph.get(a, b);
        return result == null ? Optional.empty() : Optional.of(result);
    }

    @Override
    public boolean isAdjacent(long a, long b) {
        return graph.contains(a, b);
    }

    @Override
    public Set<Long> getNodes() {
        return nodeCache;
    }

    @Override
    public Set<Pair<Long, Long>> getEdges() {
        return graph.cellSet().stream().map(x -> new Pair<>(x.getRowKey(), x.getColumnKey())).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableGraph that = (TableGraph) o;

        if (graph != null ? !graph.equals(that.graph) : that.graph != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return graph != null ? graph.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TableGraph{" +
                "graph=" + graph +
                '}';
    }
}
