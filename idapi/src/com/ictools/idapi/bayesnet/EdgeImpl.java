package com.ictools.idapi.bayesnet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EdgeImpl implements Edge {
    private final List<Node> sources;
    private final Node sink;
    private final Table<Integer, List<Integer>, Double> conditionalProbabilityMatrix;
    private final Map<String, List<Double>> piMessageStore;

    public EdgeImpl(List<Node> sources, Node sink, Table<Integer, List<Integer>, Double> conditionalProbabilityMatrix) {
        this.sources = sources;
        this.sink = sink;
        this.conditionalProbabilityMatrix = conditionalProbabilityMatrix;
        piMessageStore = Maps.newHashMap();
    }

    public List<Node> getSources() {
        return sources;
    }

    public Node getSink() {
        return sink;
    }

    public Table<Integer, List<Integer>, Double> getConditionalProbabilityMatrix() {
        return conditionalProbabilityMatrix;
    }

    @Override
    public void propagateLambdaEvidence(Node source, List<Double> lambdaEvidence) {
        propagateLambdaEvidenceSelective(source, lambdaEvidence, null);
    }

    @Override
    public void propagateLambdaEvidenceSelective(Node source, List<Double> lambdaEvidence, Node blockedParent) {
        Map<List<Integer>, Double> jointLambdaMessage = Maps.newHashMap();
        for (int i = 0; i < lambdaEvidence.size(); i++) {
            Map<List<Integer>, Double> row = conditionalProbabilityMatrix.row(i);
            for (Map.Entry<List<Integer>, Double> entry : row.entrySet()) {
                jointLambdaMessage.put(entry.getKey(), lambdaEvidence.get(i) * entry.getValue() + jointLambdaMessage.getOrDefault(entry.getKey(), 0.0));
            }
        }
        // Now for each source
        for (int i = 0; i < sources.size(); i++) {
            if (!sources.get(i).equals(blockedParent)) {
                List<Double> specificLambdaMessage = Lists.newArrayList();
                for (int j = 0; j < sources.get(i).getDimensionality(); j++) {
                    specificLambdaMessage.add(0.0);
                }
                for (Map.Entry<List<Integer>, Double> entry : jointLambdaMessage.entrySet()) {
                    int valueForSpecificSource = entry.getKey().get(i);
                    specificLambdaMessage.set(valueForSpecificSource, specificLambdaMessage.get(valueForSpecificSource) + entry.getValue());
                }
                sources.get(i).receiveLambdaMessage(new LambdaMessage(specificLambdaMessage, source));
            }
        }
    }

    @Override
    public void propagatePiEvidence(Node source, List<Double> piEvidence) {
        piMessageStore.put(source.getIdentifier(), VectorUtils.normalize(piEvidence));

        List<Double> piMessage = Lists.newArrayList();
        for (int i = 0; i < sink.getDimensionality(); i++) {
            piMessage.add(0.0);
        }
        for (Table.Cell<Integer, List<Integer>, Double> cell : conditionalProbabilityMatrix.cellSet()) {
            double jointProbability = 1.0;
            for (int j = 0; j < cell.getColumnKey().size(); j++) {
                if (piMessageStore.containsKey(sources.get(j).getIdentifier())) {
                    List<Double> existingPiMessage = piMessageStore.get(sources.get(j).getIdentifier());
                    jointProbability *= existingPiMessage.get(cell.getColumnKey().get(j));
                } else {
                    jointProbability *= 1. / (sources.get(j).getDimensionality());
                }
            }
            piMessage.set(cell.getRowKey(), piMessage.get(cell.getRowKey()) + jointProbability * cell.getValue());
        }

        sink.receivePiMessage(new PiMessage(piMessage, sources));
    }

    @Override
    public String toString() {
        return "EdgeImpl{" +
                "sources=" + sources.stream().map(Node::getIdentifier).collect(Collectors.toList()) +
                ", sink=" + sink.getIdentifier() +
                ", conditionalProbabilityMatrix=" + conditionalProbabilityMatrix +
                '}';
    }
}
