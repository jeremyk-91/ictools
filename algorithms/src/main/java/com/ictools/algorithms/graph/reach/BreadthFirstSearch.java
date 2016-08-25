package com.ictools.algorithms.graph.reach;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.ictools.algorithms.graph.structures.Graph;
import com.ictools.algorithms.graph.structures.TableGraph;
import javafx.util.Pair;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class BreadthFirstSearch {
    private BreadthFirstSearch() {
        // utility class
    }

    public static Map<Long, Long> findDistances(Graph graph, long source) {
        Set<Pair<Long, Long>> graphEdges = graph.getEdges();

        Map<Long, Long> distanceMap = Maps.newHashMap();
        Set<Long> exploredNodes = Sets.newHashSet();
        Queue<Pair<Long, Long>> traversal = new ArrayDeque<>();
        traversal.add(new Pair<>(source, 0l));
        exploredNodes.add(source);

        while (!traversal.isEmpty()) {
            Pair<Long, Long> currentStep = traversal.poll();
            long currentNode = currentStep.getKey();
            distanceMap.put(currentNode, currentStep.getValue());
            graphEdges.stream().filter(edge -> edge.getKey().equals(currentNode))
                    .filter(edge -> !exploredNodes.contains(edge.getValue()))
                    .forEach(edge -> {
                        exploredNodes.add(edge.getValue());
                        traversal.add(new Pair<>(edge.getValue(), currentStep.getValue() + 1));
                    });
        }
        return distanceMap;
    }

    public static List<List<Long>> findLevels(Table<Long, Long, Long> table, long source) {
        return findLevels(new TableGraph(table), source);
    }

    public static List<List<Long>> findLevels(Graph graph, long source) {
        Map<Long, Long> distances = findDistances(graph, source);
        long maxDistance = distances.values().stream().max(Long::compareTo).get();
        List<List<Long>> levelGraph = new ArrayList<>((int) maxDistance + 1);
        for (int i = 0; i < maxDistance; i++) {
            levelGraph.set(i, Lists.newArrayList());
        }

        for (Map.Entry<Long, Long> distance : distances.entrySet()) {
            levelGraph.get(distance.getValue().intValue()).add(distance.getKey());
        }
        return levelGraph;
    }

    public static Map<Long, List<Long>> findLevelGraph(Graph graph, long source) {
        Set<Pair<Long, Long>> graphEdges = graph.getEdges();

        Map<Long, Long> distanceMap = Maps.newHashMap();
        Map<Long, List<Long>> shortestGraph = Maps.newHashMap();
        Set<Long> exploredNodes = Sets.newHashSet();
        Queue<Pair<Long, Long>> traversal = new ArrayDeque<>();
        traversal.add(new Pair<>(source, 0l));
        exploredNodes.add(source);

        while (!traversal.isEmpty()) {
            Pair<Long, Long> currentStep = traversal.poll();
            long currentNode = currentStep.getKey();
            List<Long> usableEdges = Lists.newArrayList();
            distanceMap.put(currentNode, currentStep.getValue());
            graphEdges.stream().filter(edge -> edge.getKey().equals(currentNode))
                    .filter(edge -> !exploredNodes.contains(edge.getValue()))
                    .forEach(edge -> {
                        exploredNodes.add(edge.getValue());
                        usableEdges.add(edge.getValue());
                        traversal.add(new Pair<>(edge.getValue(), currentStep.getValue() + 1));
                    });

            shortestGraph.put(currentNode, usableEdges);
        }
        return shortestGraph;
    }


    public static List<Pair<Long, Long>> findPath(Table<Long, Long, Long> table, long source, long sink) {
        return findPath(new TableGraph(table), source, sink);
    }

    public static List<Pair<Long, Long>> findPath(Graph graph, long source, long sink) {
        Set<Long> nodes = graph.getNodes();
        if (!nodes.contains(source)) {
            throw new IllegalArgumentException("The source " + source + " is not a node in the graph " + graph);
        }
        if (!nodes.contains(sink)) {
            throw new IllegalArgumentException("The sink " + sink + " is not a node in the graph " + graph);
        }
        Set<Pair<Long, Long>> graphEdges = graph.getEdges();

        Map<Long, Long> predecessors = Maps.newHashMap();
        Queue<Long> traversal = new ArrayDeque<>();

        // Ugh. This hack is done so that the lambda can work
        final boolean[] found = {false};

        traversal.add(source);
        predecessors.put(source, null);

        while (!traversal.isEmpty() && !predecessors.containsKey(sink)) {
            long currentNode = traversal.poll();
            graphEdges.stream().filter(edge -> edge.getKey().equals(currentNode))
                    .filter(edge -> !predecessors.keySet().contains(edge.getValue()))
                    .forEach(edge -> {
                        if (edge.getValue() == sink) {
                            found[0] = true;
                        }
                        predecessors.put(edge.getValue(), edge.getKey());
                        traversal.add(edge.getValue());
                    });
        }

        if (!found[0]) {
            return null; // No path
        }

        // So we've found the target
        List<Pair<Long, Long>> result = Lists.newArrayList();
        long backtracking = sink;
        while (backtracking != source) {
            result.add(new Pair<>(predecessors.get(backtracking), backtracking));
            backtracking = predecessors.get(backtracking);
        }
        Collections.reverse(result);
        return result;
    }
}
