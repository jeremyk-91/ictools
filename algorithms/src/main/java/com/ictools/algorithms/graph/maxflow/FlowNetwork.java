package com.ictools.algorithms.graph.maxflow;

import com.ictools.algorithms.graph.structures.WeightedGraph;

public class FlowNetwork {
    private final WeightedGraph graph;
    private final long source;
    private final long sink;

    public FlowNetwork(WeightedGraph graph, long source, long sink) {
        graphSanityCheck(graph);
        this.graph = graph;
        this.source = source;
        this.sink = sink;
    }

    public WeightedGraph getGraph() {
        return graph;
    }

    private void graphSanityCheck(WeightedGraph graph) {
        if (graph.getEdges().stream().anyMatch(edge -> graph.getWeight(edge.getKey(), edge.getValue()).get() < 0)) {
            throw new IllegalArgumentException("This library assumes that capacities in a flow network must be nonnegative.");
        }
    }

    public long getCapacity(long a, long b) {
        // An absent edge can't have any flow sent down it, so it has a capacity of zero.
        return graph.getWeight(a, b).orElse(0l);
    }

    public long getSource() {
        return source;
    }

    public long getSink() {
        return sink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlowNetwork that = (FlowNetwork) o;

        if (sink != that.sink) return false;
        if (source != that.source) return false;
        if (graph != null ? !graph.equals(that.graph) : that.graph != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = graph != null ? graph.hashCode() : 0;
        result = 31 * result + (int) (source ^ (source >>> 32));
        result = 31 * result + (int) (sink ^ (sink >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "FlowNetwork{" +
                "graph=" + graph +
                ", source=" + source +
                ", sink=" + sink +
                '}';
    }
}
