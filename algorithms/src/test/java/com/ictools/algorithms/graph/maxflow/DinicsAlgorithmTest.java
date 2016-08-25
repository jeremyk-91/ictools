package com.ictools.algorithms.graph.maxflow;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DinicsAlgorithmTest {

    @Test
    public void testDinicsAlgorithmSimple() {
        FlowNetwork flowNetwork = TestFlowNetworks.getTestFlowNetworkSimple();
        DinicsAlgorithm dinicsAlgorithm = new DinicsAlgorithm(flowNetwork);
        MaxFlowResult result = dinicsAlgorithm.computeMaxFlow();

        assertThat(result.getMaximumFlowValue(), is(5l));
        assertThat(result.getMaximumFlow().get(0l, 1l), is(5l));
        assertThat(result.getMaximumFlow().get(1l, 2l), is(5l));
    }

    @Test
    public void testDinicsAlgorithmFork() {
        FlowNetwork flowNetwork = TestFlowNetworks.getTestFlowNetworkFork();
        DinicsAlgorithm dinicsAlgorithm = new DinicsAlgorithm(flowNetwork);
        MaxFlowResult result = dinicsAlgorithm.computeMaxFlow();

        assertThat(result.getMaximumFlowValue(), is(14l));
        assertThat(result.getMaximumFlow().get(0l, 1l), is(5l));
        assertThat(result.getMaximumFlow().get(1l, 2l), is(5l));
        assertThat(result.getMaximumFlow().get(0l, 2l), is(9l));
    }
}
