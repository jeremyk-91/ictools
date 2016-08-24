package com.ictools.algorithms.graph.maxflow;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class EdmondsKarpAlgorithmTest {

    @Test
    public void testEdmondsKarpAlgorithmSimple() {
        FlowNetwork flowNetwork = TestFlowNetworks.getTestFlowNetworkSimple();
        EdmondsKarpAlgorithm edmondsKarpAlgorithm = new EdmondsKarpAlgorithm(flowNetwork);
        MaxFlowResult result = edmondsKarpAlgorithm.computeMaxFlow();

        assertThat(result.getMaximumFlowValue(), is(5l));
        assertThat(result.getMaximumFlow().get(0l, 1l), is(5l));
        assertThat(result.getMaximumFlow().get(1l, 2l), is(5l));
    }

    @Test
    public void testEdmondsKarpAlgorithmMedium() {
        FlowNetwork flowNetwork = TestFlowNetworks.getTestFlowNetworkMedium();
        EdmondsKarpAlgorithm edmondsKarpAlgorithm = new EdmondsKarpAlgorithm(flowNetwork);
        MaxFlowResult result = edmondsKarpAlgorithm.computeMaxFlow();

        assertThat(result.getMaximumFlowValue(), is(10l));
    }

    @Test
    public void testEdmondsKarpAlgorithmPathological() {
        FlowNetwork pathologicalNetwork = TestFlowNetworks.getTestFlowNetworkPathological();
        EdmondsKarpAlgorithm edmondsKarpAlgorithm = new EdmondsKarpAlgorithm(pathologicalNetwork);
        MaxFlowResult result = edmondsKarpAlgorithm.computeMaxFlow();

        assertThat(result.getMaximumFlowValue(), is(2000000000l));
        assertThat(result.getMaximumFlow().get(0l, 1l), is(1000000000l));
        assertThat(result.getMaximumFlow().get(0l, 2l), is(1000000000l));
        assertThat(result.getMaximumFlow().get(1l, 3l), is(1000000000l));
        assertThat(result.getMaximumFlow().get(2l, 3l), is(1000000000l));
        assertThat(result.getMaximumFlow().get(1l, 2l), is(nullValue()));
    }
}
