package com.ictools.idapi.bayesnet;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class RootNodeTest {

    private static final String NAME = "testRootNode";

    private final Node RANDOM_NODE_1 = Mockito.mock(Node.class);
    private final Node RANDOM_NODE_2 = Mockito.mock(Node.class);
    private final Node RANDOM_NODE_3 = Mockito.mock(Node.class);
    private final Edge RANDOM_EDGE_1 = Mockito.mock(Edge.class);
    private final Edge RANDOM_EDGE_2 = Mockito.mock(Edge.class);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(RANDOM_EDGE_1.getSink()).thenReturn(RANDOM_NODE_1);
        when(RANDOM_EDGE_2.getSink()).thenReturn(RANDOM_NODE_2);
    }

    @Test
    public void testInstantiation() {
        RootNode rootNode = new RootNode(NAME, Lists.newArrayList(0.4, 0.6), Lists.newArrayList());
        rootNode.instantiate(1);
        assertThat(rootNode.getPosteriorDistribution(), is(Lists.newArrayList(0.0, 1.0)));
    }

    @Test
    public void testPropagatesPiEvidenceOnInstantiation() {
        RootNode rootNode = new RootNode(NAME, Lists.newArrayList(0.4, 0.6), Lists.newArrayList(RANDOM_EDGE_1, RANDOM_EDGE_2));
        rootNode.instantiate(1);
        verify(RANDOM_EDGE_1, times(1)).propagatePiEvidence(eq(Lists.newArrayList(0.0, 1.0)));
        verify(RANDOM_EDGE_2, times(1)).propagatePiEvidence(eq(Lists.newArrayList(0.0, 1.0)));
    }

    @Test
    public void testReceiveLambdaMessage() {
        RootNode rootNode = new RootNode(NAME, Lists.newArrayList(0.4, 0.6), Lists.newArrayList());
        rootNode.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(0.2, 0.8), RANDOM_NODE_1));
        TestUtils.checkVectorEquality(rootNode.getPosteriorDistribution(), Lists.newArrayList(0.08 / 0.56, 0.48 / 0.56));
    }

    @Test
    public void testReceiveMultipleLambdaMessages() {
        RootNode rootNode = new RootNode(NAME, Lists.newArrayList(0.4, 0.6), Lists.newArrayList());
        rootNode.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(0.2, 0.8), RANDOM_NODE_1));
        rootNode.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(0.2, 0.8), RANDOM_NODE_1));

        double totalPreNormalisation = 0.4 * 0.2 * 0.2 + 0.6 * 0.8 * 0.8;
        TestUtils.checkVectorEquality(rootNode.getPosteriorDistribution(), Lists.newArrayList((0.4 * 0.2 * 0.2) / totalPreNormalisation, (0.6 * 0.8 * 0.8) / totalPreNormalisation));
    }

    @Test
    public void testReceiveDefinitiveLambdaMessage() {
        RootNode rootNode = new RootNode(NAME, Lists.newArrayList(0.4, 0.6), Lists.newArrayList());
        rootNode.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(0.0, 1.0), RANDOM_NODE_1));
        TestUtils.checkVectorEquality(rootNode.getPosteriorDistribution(), Lists.newArrayList(0.0, 1.0));
    }

    @Test
    public void testReceiveUnnormalisedLambdaMessage() {
        RootNode rootNode = new RootNode(NAME, Lists.newArrayList(0.4, 0.6), Lists.newArrayList());
        rootNode.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(0.2, 0.2), RANDOM_NODE_1));
        TestUtils.checkVectorEquality(rootNode.getPosteriorDistribution(), Lists.newArrayList(0.4, 0.6));
    }

    @Test
    public void testPropagatesPiEvidenceOnReceivingLambdaMessage_1() {
        RootNode rootNode = new RootNode(NAME, Lists.newArrayList(0.4, 0.6), Lists.newArrayList(RANDOM_EDGE_1, RANDOM_EDGE_2));
        rootNode.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(1.0, 1.0), RANDOM_NODE_1));
        verify(RANDOM_EDGE_1, never()).propagatePiEvidence(any());
        verify(RANDOM_EDGE_2, times(1)).propagatePiEvidence(eq(Lists.newArrayList(0.4, 0.6)));
    }

    @Test
    public void testPropagatesPiEvidenceOnReceivingLambdaMessage_2() {
        RootNode rootNode = new RootNode(NAME, Lists.newArrayList(0.4, 0.6), Lists.newArrayList(RANDOM_EDGE_1, RANDOM_EDGE_2));
        rootNode.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(0.2, 0.2), RANDOM_NODE_3));
        verify(RANDOM_EDGE_1, times(1)).propagatePiEvidence(any());
        verify(RANDOM_EDGE_2, times(1)).propagatePiEvidence(any());
    }
}
