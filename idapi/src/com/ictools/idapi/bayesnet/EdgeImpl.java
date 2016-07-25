package com.ictools.idapi.bayesnet;

import com.google.common.collect.Table;

import java.util.List;
import java.util.Map;

public class EdgeImpl implements Edge {
    private final List<Node> sources;
    private final Node sink;
    private final Table<Integer, Integer, Double> conditionalProbabilityMatrix;

    public EdgeImpl(List<Node> sources, Node sink, Table<Integer, Integer, Double> conditionalProbabilityMatrix) {
        this.sources = sources;
        this.sink = sink;
        this.conditionalProbabilityMatrix = conditionalProbabilityMatrix;
    }

    public List<Node> getSources() {
        return sources;
    }

    public Node getSink() {
        return sink;
    }

    public Table<Integer, Integer, Double> getConditionalProbabilityMatrix() {
        return conditionalProbabilityMatrix;
    }

    @Override
    public void propagateLambdaEvidence(Node source, List<Double> lambdaEvidence) {
        // TODO
    }

    @Override
    public void propagatePiEvidence(Node source, List<Double> piEvidence) {
        // TODO
    }

    @Override
    public String toString() {
        return "EdgeImpl{" +
                "sources=" + sources +
                ", sink=" + sink +
                ", conditionalProbabilityMatrix=" + conditionalProbabilityMatrix +
                '}';
    }
}
