package com.ictools.algorithms.graph.maxflow;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.ictools.algorithms.graph.structures.DefaultingTable;
import com.ictools.algorithms.graph.structures.WeightedGraph;
import javafx.util.Pair;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PushRelabelAlgorithm implements MaxFlowAlgorithm {

    private final FlowNetwork flowNetwork;
    private final DefaultingTable<Long, Long, Long> residualGraph;
    private final DefaultingTable<Long, Long, Long> flow;
    private final Map<Long, Long> nodeHeights;
    private final Map<Long, Long> nodeExcessFlows;

    private final Deque<Long> outstandingNodes;

    public PushRelabelAlgorithm(FlowNetwork flowNetwork) {
        this.flowNetwork = flowNetwork;
        residualGraph = new DefaultingTable<>(HashBasedTable.create(), 0l);
        flow = new DefaultingTable<>(HashBasedTable.create(), 0l);
        nodeHeights = Maps.newHashMap();
        nodeExcessFlows = Maps.newHashMap();
        outstandingNodes = new ArrayDeque<>();
    }

    @Override
    public MaxFlowResult computeMaxFlow() {
        initializeDataStructures();
        fireSaturatingPushes();

        Set<Pair<Long, Long>> edges = flowNetwork.getGraph().getEdges();

        while (!outstandingNodes.isEmpty()) {
            long source = outstandingNodes.pollFirst();
            if (source == flowNetwork.getSink()) {
                continue;
            }

            Set<Pair<Long, Long>> pushableEdges = edges.stream()
                    .filter(edge -> edge.getKey() == source)
                    .filter(edge -> residualGraph.get(source, edge.getValue()) > 0)
                    .filter(edge -> nodeHeights.get(source) > nodeHeights.get(edge.getValue()))
                    .collect(Collectors.toSet());
            // Pick one
            if (!pushableEdges.isEmpty()) {
                Pair<Long, Long> targetEdge = pushableEdges.iterator().next();
                push(source, targetEdge.getValue(),
                        Math.min(nodeExcessFlows.get(source),
                                residualGraph.get(source, targetEdge.getValue())));
            } else {
                relabel(source);
                outstandingNodes.addLast(source);
            }

        }
        return new MaxFlowResult(flow, nodeExcessFlows.get(flowNetwork.getSink()));
    }

    private void initializeDataStructures() {
        initializeResidualGraph();
        initializeNodeMaps();
    }

    private void initializeResidualGraph() {
        WeightedGraph capacities = flowNetwork.getGraph();
        for (Pair<Long, Long> edge : capacities.getEdges()) {
            residualGraph.put(edge.getKey(), edge.getValue(), flowNetwork.getCapacity(edge.getKey(), edge.getValue()));
        }
    }

    private void initializeNodeMaps() {
        Set<Long> nodes = flowNetwork.getGraph().getNodes();
        nodes.forEach(node -> nodeHeights.put(node, 0l));
        nodes.forEach(node -> nodeExcessFlows.put(node, 0l));
        nodeHeights.put(flowNetwork.getSource(), (long) nodes.size());
    }

    private void fireSaturatingPushes() {
        flowNetwork.getGraph().getEdges().stream().filter(edge -> edge.getKey() == flowNetwork.getSource())
                .forEach(edge -> {
                    push(edge.getKey(), edge.getValue(), flowNetwork.getCapacity(edge.getKey(), edge.getValue()));
                    outstandingNodes.add(edge.getValue());
                });
    }

    private void push(long from, long to, long amount) {
        if (from != flowNetwork.getSource()) {
            nodeExcessFlows.put(from, nodeExcessFlows.get(from) - amount);
        }
        nodeExcessFlows.put(to, nodeExcessFlows.get(to) + amount);

        long newCapacity = residualGraph.get(from, to) - amount;
        if (newCapacity > 0) {
            residualGraph.put(from, to, newCapacity);
        } else {
            residualGraph.remove(from, to);
        }
        residualGraph.put(from, to, residualGraph.get(from, to) + amount);
        flow.put(from, to, flow.get(from, to) + amount);
    }

    private void relabel(long node) {
        nodeHeights.put(node, nodeHeights.get(node) + 1);
    }
}
