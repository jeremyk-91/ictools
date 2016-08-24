package com.ictools.algorithms.graph.maxflow;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class PushRelabelAlgorithmTest {

    @Test
    public void testPushRelabelAlgorithmSimple() {
        FlowNetwork flowNetwork = TestFlowNetworks.getTestFlowNetworkSimple();
        PushRelabelAlgorithm pushRelabelAlgorithm = new PushRelabelAlgorithm(flowNetwork);
        MaxFlowResult result = pushRelabelAlgorithm.computeMaxFlow();

        assertThat(result.getMaximumFlowValue(), is(5l));
        assertThat(result.getMaximumFlow().get(0l, 1l), is(5l));
        assertThat(result.getMaximumFlow().get(1l, 2l), is(5l));
    }

    @Test
    public void testPushRelabelAlgorithmPathological() {
        FlowNetwork pathologicalNetwork = TestFlowNetworks.getTestFlowNetworkPathological();
        PushRelabelAlgorithm pushRelabelAlgorithm = new PushRelabelAlgorithm(pathologicalNetwork);
        MaxFlowResult result = pushRelabelAlgorithm.computeMaxFlow();

        assertThat(result.getMaximumFlowValue(), is(2000000000l));
        assertThat(result.getMaximumFlow().get(0l, 1l), is(1000000000l));
        assertThat(result.getMaximumFlow().get(0l, 2l), is(1000000000l));
        assertThat(result.getMaximumFlow().get(1l, 3l), is(1000000000l));
        assertThat(result.getMaximumFlow().get(2l, 3l), is(1000000000l));
        assertThat(result.getMaximumFlow().get(1l, 2l), is(nullValue()));
    }
}
