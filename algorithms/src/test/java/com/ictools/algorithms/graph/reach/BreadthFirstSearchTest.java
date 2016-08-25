package com.ictools.algorithms.graph.reach;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.ictools.algorithms.graph.structures.Graph;
import com.ictools.algorithms.graph.structures.TableGraph;
import com.ictools.algorithms.graph.util.GraphTestUtils;
import javafx.util.Pair;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class BreadthFirstSearchTest {

    @Test
    public void testBreadthFirstSearch_Reachable() {
        Table<Long, Long, Long> adjacencyMatrix = HashBasedTable.create();
        adjacencyMatrix.put(0l, 1l, 75l);
        adjacencyMatrix.put(1l, 2l, 43l);
        adjacencyMatrix.put(2l, 3l, 99l);
        Graph graph = new TableGraph(adjacencyMatrix);

        List<Pair<Long, Long>> result = BreadthFirstSearch.findPath(graph, 0l, 3l);
        assertThat(result, not(nullValue()));
        assertThat(result, is(GraphTestUtils.buildPath(0l, 1l, 2l, 3l)));
    }

    @Test
    public void testBreadthFirstSearch_Unreachable() {
        Table<Long, Long, Long> adjacencyMatrix = HashBasedTable.create();
        adjacencyMatrix.put(0l, 1l, 75l);
        adjacencyMatrix.put(1l, 2l, 43l);
        adjacencyMatrix.put(1l, 3l, 99l);
        Graph graph = new TableGraph(adjacencyMatrix);

        assertThat(BreadthFirstSearch.findPath(graph, 2l, 3l), is(nullValue()));
    }

    @Test
    public void testBreadthFirstSearch_ShortestPathByNumEdges() {
        Table<Long, Long, Long> adjacencyMatrix = HashBasedTable.create();
        adjacencyMatrix.put(0l, 1l, 75l);
        adjacencyMatrix.put(1l, 2l, 43l);
        adjacencyMatrix.put(2l, 3l, 99l);
        adjacencyMatrix.put(0l, 3l, 66l);
        Graph graph = new TableGraph(adjacencyMatrix);

        List<Pair<Long, Long>> result = BreadthFirstSearch.findPath(graph, 0l, 3l);
        assertThat(result, not(nullValue()));
        assertThat(result, is(GraphTestUtils.buildPath(0l, 3l)));
    }

    @Test
    public void testBreadthFirstSearch_CycleReachable() {
        Table<Long, Long, Long> adjacencyMatrix = HashBasedTable.create();
        adjacencyMatrix.put(0l, 1l, 75l);
        adjacencyMatrix.put(1l, 2l, 43l);
        adjacencyMatrix.put(2l, 3l, 99l);
        adjacencyMatrix.put(3l, 0l, 66l);
        adjacencyMatrix.put(1l, 4l, 11l);
        Graph graph = new TableGraph(adjacencyMatrix);

        List<Pair<Long, Long>> result = BreadthFirstSearch.findPath(graph, 0l, 4l);
        assertThat(result, not(nullValue()));
        assertThat(result, is(GraphTestUtils.buildPath(0l, 1l, 4l)));
    }

    @Test
    public void testBreadthFirstSearch_CycleUnreachable() {
        Table<Long, Long, Long> adjacencyMatrix = HashBasedTable.create();
        adjacencyMatrix.put(0l, 1l, 75l);
        adjacencyMatrix.put(1l, 2l, 43l);
        adjacencyMatrix.put(2l, 3l, 99l);
        adjacencyMatrix.put(3l, 0l, 66l);
        adjacencyMatrix.put(1l, 4l, 11l);
        adjacencyMatrix.put(5l, 4l, 11l);
        Graph graph = new TableGraph(adjacencyMatrix);

        List<Pair<Long, Long>> result = BreadthFirstSearch.findPath(graph, 0l, 5l);
        assertThat(result, is(nullValue()));
    }

    @Test
    public void testBreadthFirstSearch_Distances() {
        Table<Long, Long, Long> adjacencyMatrix = HashBasedTable.create();
        adjacencyMatrix.put(0l, 1l, 75l);
        adjacencyMatrix.put(1l, 2l, 43l);
        adjacencyMatrix.put(2l, 3l, 99l);
        adjacencyMatrix.put(3l, 0l, 66l);
        adjacencyMatrix.put(1l, 4l, 11l);
        adjacencyMatrix.put(5l, 4l, 11l);
        Graph graph = new TableGraph(adjacencyMatrix);

        Map<Long, Long> result = BreadthFirstSearch.findDistances(graph, 0l);
        assertThat(result.get(0l), is(0l));
        assertThat(result.get(1l), is(1l));
        assertThat(result.get(2l), is(2l));
        assertThat(result.get(3l), is(3l));
        assertThat(result.get(4l), is(2l));
        assertThat(result.get(5l), is(nullValue()));
    }

    @Test
    public void testBreadthFirstSearch_LevelGraph() {
        Table<Long, Long, Long> adjacencyMatrix = HashBasedTable.create();
        adjacencyMatrix.put(0l, 1l, 75l);
        adjacencyMatrix.put(1l, 2l, 43l);
        adjacencyMatrix.put(2l, 3l, 99l);
        adjacencyMatrix.put(3l, 0l, 66l);
        adjacencyMatrix.put(1l, 4l, 11l);
        adjacencyMatrix.put(5l, 4l, 11l);
        Graph graph = new TableGraph(adjacencyMatrix);

        Map<Long, List<Long>> result = BreadthFirstSearch.findLevelGraph(graph, 0l);
        assertThat(result.get(0l), containsInAnyOrder(1l));
        assertThat(result.get(1l), containsInAnyOrder(2l, 4l));
        assertThat(result.get(2l), containsInAnyOrder(3l));
        assertThat(result.get(3l), empty());
        assertThat(result.get(4l), empty());
        assertThat(result.get(5l), is(nullValue()));

    }

}
