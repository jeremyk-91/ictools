package com.ictools.algorithms.graph.maxflow;

import com.google.common.collect.HashBasedTable;
import com.ictools.algorithms.graph.reach.BreadthFirstSearch;
import com.ictools.algorithms.graph.structures.DefaultingTable;
import com.ictools.algorithms.graph.structures.WeightedGraph;
import javafx.util.Pair;

import java.util.List;
import java.util.logging.Logger;

/**
 * This class implements a specific case of the Ford-Fulkerson algorithm.
 * To select an augmenting path, the algorithm selects the shortest path (as measured by a BFS) from source to sink.
 */

public class EdmondsKarpAlgorithm implements MaxFlowAlgorithm {
    private static final Logger log = Logger.getLogger(EdmondsKarpAlgorithm.class.getName());

    private final FlowNetwork flowNetwork;
    private final DefaultingTable<Long, Long, Long> residualGraph;
    private final DefaultingTable<Long, Long, Long> flow;
    private long flowValue;

    public EdmondsKarpAlgorithm(FlowNetwork flowNetwork) {
        this.flowNetwork = flowNetwork;
        this.residualGraph = new DefaultingTable<>(HashBasedTable.<Long, Long, Long>create(), 0l);
        this.flow = new DefaultingTable<>(HashBasedTable.<Long, Long, Long>create(), 0l);
        this.flowValue = 0l;
    }

    @Override
    public MaxFlowResult computeMaxFlow() {
        initializeResidualGraph();

        List<Pair<Long, Long>> augmentingPath = BreadthFirstSearch.findPath(residualGraph, flowNetwork.getSource(), flowNetwork.getSink());
        while (augmentingPath != null) {
            // Augment the flow along the augmenting path!
            augmentPath(augmentingPath);
            augmentingPath = BreadthFirstSearch.findPath(residualGraph, flowNetwork.getSource(), flowNetwork.getSink());
        }
        log.info("Final flow from the max flow computation is " + flow);
        return new MaxFlowResult(flow, flowValue);
    }

    private void augmentPath(List<Pair<Long, Long>> augmentingPath) {
        // Augmenting paths cannot have a length of 0 so the get below is safe.
        long minCapacity = augmentingPath.stream()
                .map(edge -> residualGraph.get(edge.getKey(), edge.getValue()))
                .min(Long::compareTo)
                .get();
        flowValue += minCapacity;

        // For every edge along the path, push the flow down.
        augmentingPath.stream()
                .forEach(edge -> propagateFlow(minCapacity, edge));
        log.info("Augment along the path " + augmentingPath + "; you should push " + minCapacity + " units");
        log.info("The new residual network is " + residualGraph);
    }

    private void propagateFlow(long minCapacity, Pair<Long, Long> edge) {
        long newCapacity = residualGraph.get(edge.getKey(), edge.getValue()) - minCapacity;
        if (newCapacity > 0) {
            residualGraph.put(edge.getKey(), edge.getValue(), newCapacity);
        } else {
            residualGraph.remove(edge.getKey(), edge.getValue());
        }
        residualGraph.put(edge.getValue(), edge.getKey(),
                residualGraph.get(edge.getValue(), edge.getKey() + minCapacity));
        flow.put(edge.getKey(), edge.getValue(),
                flow.get(edge.getKey(), edge.getValue()) + minCapacity);
    }

    private void initializeResidualGraph() {
        WeightedGraph capacities = flowNetwork.getGraph();
        for (Pair<Long, Long> edge : capacities.getEdges()) {
            residualGraph.put(edge.getKey(), edge.getValue(), flowNetwork.getCapacity(edge.getKey(), edge.getValue()));
        }
    }
}
