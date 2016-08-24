package com.ictools.algorithms.graph.maxflow;

import com.ictools.algorithms.graph.structures.Graph;
import com.ictools.algorithms.graph.structures.TableGraph;
import com.ictools.algorithms.graph.structures.WeightedGraph;

import java.util.Optional;
import java.util.Set;

public class FlowNetwork {
    private final WeightedGraph graph;
    private final WeightedGraph flow;
    private final long source;
    private final long sink;

    public FlowNetwork(WeightedGraph graph, long source, long sink) {
        graphSanityCheck(graph);
        this.graph = graph;
        // Initially the flow graph is empty.
        this.flow = new TableGraph();
        this.source = source;
        this.sink = sink;
    }

    private void graphSanityCheck(WeightedGraph graph) {
    }

    public long getCapacity(long a, long b) {
        Optional<Long> result = graph.getWeight(a, b);
        // Note
    }
}
