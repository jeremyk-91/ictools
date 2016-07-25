package com.ictools.idapi.bayesnet;

import com.google.common.collect.Table;

import java.util.List;

public class EdgeImpl implements Edge {
    private final Node source;
    private final Node sink;
    private final Table<Integer, Integer, Double> conditionalProbabilityMatrix;

    public EdgeImpl(Node source, Node sink, Table<Integer, Integer, Double> conditionalProbabilityMatrix) {
        this.source = source;
        this.sink = sink;
        this.conditionalProbabilityMatrix = conditionalProbabilityMatrix;
    }

    public Node getSource() {
        return source;
    }

    public Node getSink() {
        return sink;
    }

    public Table<Integer, Integer, Double> getConditionalProbabilityMatrix() {
        return conditionalProbabilityMatrix;
    }

    @Override
    public void propagateLambdaEvidence(List<Double> lambdaEvidence) {
        // TODO
    }

    @Override
    public void propagatePiEvidence(List<Double> piEvidence) {
        // TODO
    }

    @Override
    public String toString() {
        return "Edge{" +
                "source=" + source +
                ", sink=" + sink +
                ", conditionalProbabilityMatrix=" + conditionalProbabilityMatrix +
                '}';
    }
}
