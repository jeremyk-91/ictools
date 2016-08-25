package com.ictools.algorithms.graph.maxflow;

import com.google.common.collect.HashBasedTable;
import com.ictools.algorithms.graph.structures.TableGraph;
import org.junit.Test;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RandomDifferentialFlowTest {

    public static final int FLOW_NETWORK_SIZE = 25;
    public static final int NUM_TEST_ITERATIONS = 50;

    public static FlowNetwork generateRandomFlowNetwork() {
        Random random = new Random();
        HashBasedTable<Long, Long, Long> table = HashBasedTable.create(FLOW_NETWORK_SIZE, FLOW_NETWORK_SIZE);
        for (long i = 0; i < FLOW_NETWORK_SIZE; i++) {
            for (long j = i + 1; j < FLOW_NETWORK_SIZE; j++) {
                table.put(i, j, (long) Math.abs(random.nextInt(500)));
            }
        }
        return new FlowNetwork(new TableGraph(table), 0l, FLOW_NETWORK_SIZE - 1);
    }

    @Test
    public void randomDifferentialTest() {
        for (int i = 0; i < NUM_TEST_ITERATIONS; i++) {
            FlowNetwork flowNetwork = generateRandomFlowNetwork();
            Logger.getLogger(EdmondsKarpAlgorithm.class.getName()).setLevel(Level.OFF); // way too noisy

            MaxFlowResult edmondsKarpResult = new EdmondsKarpAlgorithm(flowNetwork).computeMaxFlow();
            MaxFlowResult pushRelabelResult = new PushRelabelAlgorithm(flowNetwork).computeMaxFlow();
            MaxFlowResult dinicsResult = new DinicsAlgorithm(flowNetwork).computeMaxFlow();

            // There can be multiple solutions. So we only check the flow values which must be equal
            // e.g. (1) --> (2) branches to (3), (4), both go in to (5) which is the sink
            // It's OK to use 1235 or 1245, so the edges on which flow actually travels differ.
            assertThat(edmondsKarpResult.getMaximumFlowValue(), is(pushRelabelResult.getMaximumFlowValue()));
            assertThat(edmondsKarpResult.getMaximumFlowValue(), is(dinicsResult.getMaximumFlowValue()));
        }
    }
}
