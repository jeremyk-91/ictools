package com.ictools.algorithms.graph.structures;

import com.google.common.collect.HashBasedTable;
import javafx.util.Pair;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

public class TableGraphTest {

    public TableGraph getTestTableGraph() {
        HashBasedTable<Long, Long, Long> table = HashBasedTable.create(3, 3);
        table.put(0l, 1l, 5l);
        table.put(1l, 2l, 7l);
        return new TableGraph(table);
    }

    @Test
    public void testGetNodes() {
        assertThat(getTestTableGraph().getNodes(), containsInAnyOrder(0l, 1l, 2l));
    }

    @Test
    public void testGetEdges() {
        assertThat(getTestTableGraph().getEdges(), containsInAnyOrder(new Pair<>(0l, 1l), new Pair<>(1l, 2l)));
    }

    @Test
    public void testGetOnPresentEdge() {
        assertThat(getTestTableGraph().getWeight(0l, 1l), is(Optional.of(5l)));
    }

    @Test
    public void testGetOnAbsentEdge() {
        assertThat(getTestTableGraph().getWeight(0l, 0l), is(Optional.empty()));
    }

    @Test
    public void testAdjacent() {
        assertThat(getTestTableGraph().isAdjacent(0l, 1l), is(true));
        assertThat(getTestTableGraph().isAdjacent(0l, 2l), is(false));
    }
}
