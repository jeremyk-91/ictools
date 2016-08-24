package com.ictools.algorithms.graph.maxflow;

import com.google.common.collect.HashBasedTable;
import com.ictools.algorithms.graph.structures.TableGraph;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FlowNetworkTest {

    private static FlowNetwork flowNetwork;

    @BeforeClass
    public static void setupFlowNetwork() {
        HashBasedTable<Long, Long, Long> table = HashBasedTable.create(3, 3);
        table.put(0l, 1l, 5l);
        table.put(1l, 2l, 7l);
        flowNetwork = new FlowNetwork(new TableGraph(table), 0l, 2l);
    }

    @Test
    public void testGetCapacity() {
        assertThat(flowNetwork.getCapacity(0l, 1l), is(5l));
    }

    @Test
    public void testGetCapacityOfAbsentEdgeReturnsZero() {
        assertThat(flowNetwork.getCapacity(0l, 2l), is(0l));
    }
}
