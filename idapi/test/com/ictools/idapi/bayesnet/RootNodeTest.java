package com.ictools.idapi.bayesnet;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

public class RootNodeTest {

    private static final String NAME = "testRootNode";

    @Test
    public void testInstantiation() {
        RootNode rootNode = new RootNode(NAME, Lists.newArrayList(0.4, 0.6), Lists.newArrayList());
        rootNode.instantiate(1);
        assertThat(rootNode.getPosteriorDistribution(), is(Lists.newArrayList(0.0, 1.0)));
    }

    @Test
    public void testReceiveLambdaMessage() {
        RootNode rootNode = new RootNode(NAME, Lists.newArrayList(0.4, 0.6), Lists.newArrayList());
        rootNode.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(0.2, 0.8)));
        TestUtils.checkVectorEquality(rootNode.getPosteriorDistribution(), Lists.newArrayList(0.08 / 0.56, 0.48 / 0.56));
    }

    @Test
    public void testReceiveMultipleLambdaMessages() {
        RootNode rootNode = new RootNode(NAME, Lists.newArrayList(0.4, 0.6), Lists.newArrayList());
        rootNode.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(0.2, 0.8)));
        rootNode.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(0.2, 0.8)));

        double totalPreNormalisation = 0.4 * 0.2 * 0.2 + 0.6 * 0.8 * 0.8;
        TestUtils.checkVectorEquality(rootNode.getPosteriorDistribution(), Lists.newArrayList((0.4 * 0.2 * 0.2) / totalPreNormalisation, (0.6 * 0.8 * 0.8) / totalPreNormalisation));
    }

    @Test
    public void testReceiveDefinitiveLambdaMessage() {
        RootNode rootNode = new RootNode(NAME, Lists.newArrayList(0.4, 0.6), Lists.newArrayList());
        rootNode.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(0.0, 1.0)));
        TestUtils.checkVectorEquality(rootNode.getPosteriorDistribution(), Lists.newArrayList(0.0, 1.0));
    }

    @Test
    public void testReceiveUnnormalisedLambdaMessage() {
        RootNode rootNode = new RootNode(NAME, Lists.newArrayList(0.4, 0.6), Lists.newArrayList());
        rootNode.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(0.2, 0.2)));
        TestUtils.checkVectorEquality(rootNode.getPosteriorDistribution(), Lists.newArrayList(0.4, 0.6));
    }

}
