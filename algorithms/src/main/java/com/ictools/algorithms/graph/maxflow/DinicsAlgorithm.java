package com.ictools.algorithms.graph.maxflow;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.ictools.algorithms.graph.reach.BreadthFirstSearch;
import com.ictools.algorithms.graph.structures.DefaultingTable;
import com.ictools.algorithms.graph.structures.TableGraph;
import com.ictools.algorithms.graph.structures.WeightedGraph;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DinicsAlgorithm implements MaxFlowAlgorithm {
    private final FlowNetwork flowNetwork;
    private final DefaultingTable<Long, Long, Long> residualGraph;
    private final DefaultingTable<Long, Long, Long> flow;


    public DinicsAlgorithm(FlowNetwork flowNetwork) {
        this.flowNetwork = flowNetwork;
        this.residualGraph = new DefaultingTable<>(HashBasedTable.create(), 0l);
        this.flow = new DefaultingTable<>(HashBasedTable.create(), 0l);
    }

    @Override
    public MaxFlowResult computeMaxFlow() {
        initializeResidualGraph();
        while (BreadthFirstSearch.findPath(residualGraph, flowNetwork.getSource(), flowNetwork.getSink()) != null) {
            Table<Long, Long, Long> blockingFlow = findBlockingFlow();
            augmentFlow(blockingFlow);
        }
        return FlowConsolidationUtils.resolveAndConsolidateFlows(flowNetwork, flow);
    }

    private Table<Long, Long, Long> findBlockingFlow() {
        Table<Long, Long, Long> blockingFlow = new DefaultingTable<>(HashBasedTable.create(), 0l);
        // Compute level graph for the residual graph
        Table<Long, Long, Long> residualGraphCopy = HashBasedTable.create(residualGraph);
        Map<Long, List<Long>> levelGraph = BreadthFirstSearch.findLevelGraph(new TableGraph(residualGraphCopy), flowNetwork.getSource());
        long[] limits = new long[levelGraph.size()];
        List<Long> path = Lists.newArrayList(flowNetwork.getSource());
        while (dfsInLevelGraph(levelGraph, limits, flowNetwork.getSource(), path)) {
            augmentFlowAlong(blockingFlow, residualGraphCopy, path);
        }
        return blockingFlow;
    }

    private void augmentFlowAlong(Table<Long, Long, Long> flow, Table<Long, Long, Long> graph, List<Long> path) {
        long augmentationAmount = Long.MAX_VALUE;
        for (int i = 0; i < path.size() - 1; i++) {
            // Each edge is between i and (i + 1).
            // Find the minimum amount first.
            augmentationAmount = Math.min(augmentationAmount, graph.get(path.get(i), path.get(i + 1)));
        }

        // Then...
        for (int i = 0; i < path.size() - 1; i++) {
            flow.put(path.get(i), path.get(i + 1), flow.get(path.get(i), path.get(i + 1)) + augmentationAmount);
            long remainder = flow.get(path.get(i), path.get(i + 1)) - augmentationAmount;
            if (remainder > 0) {
                graph.put(path.get(i), path.get(i + 1), remainder);
            } else {
                graph.remove(path.get(i), path.get(i + 1));
            }
            graph.put(path.get(i + 1), path.get(i), flow.get(path.get(i + 1), path.get(i)) + augmentationAmount);
        }
    }

    private boolean dfsInLevelGraph(Map<Long, List<Long>> levelGraph, long[] limits, long node, List<Long> path) {
        if (node == flowNetwork.getSink()) {
            return true;
        }
        if (limits[(int) node] >= levelGraph.get(node).size()) {
            // No more edges
            return false;
        }
        long nextTarget = levelGraph.get(node).get((int) limits[(int) node]);
        path.add(nextTarget);
        if (dfsInLevelGraph(levelGraph, limits, nextTarget, path)) {
            limits[(int) node]++;
            return true;
        } else {
            path.remove(nextTarget);
            limits[(int) node]++;
            return dfsInLevelGraph(levelGraph, limits, node, path);
        }
    }

    @SuppressWarnings("unchecked")
    private void augmentFlow(Table<Long, Long, Long> blockingFlow) {
        for (Table.Cell<Long, Long, Long> cell : blockingFlow.cellSet()) {
            long from = cell.getRowKey();
            long to = cell.getColumnKey();
            long value = cell.getValue();
            flow.put(from, to, value + flow.get(from, to));
            long remainder = residualGraph.get(from, to) - value;
            if (remainder > 0) {
                residualGraph.put(from, to, residualGraph.get(from, to) - value);
            } else {
                residualGraph.remove(from, to);
            }
            residualGraph.put(to, from, residualGraph.get(to, from) + value);
        }
    }

    private void initializeResidualGraph() {
        WeightedGraph capacities = flowNetwork.getGraph();
        for (Pair<Long, Long> edge : capacities.getEdges()) {
            residualGraph.put(edge.getKey(), edge.getValue(), flowNetwork.getCapacity(edge.getKey(), edge.getValue()));
        }
    }
}
