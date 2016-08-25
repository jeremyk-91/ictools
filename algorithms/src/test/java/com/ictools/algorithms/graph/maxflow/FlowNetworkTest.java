package com.ictools.algorithms.graph.maxflow;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FlowNetworkTest {

    private static FlowNetwork flowNetwork;

    @BeforeClass
    public static void setupFlowNetwork() {
        flowNetwork = TestFlowNetworks.getTestFlowNetworkSimple();
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
