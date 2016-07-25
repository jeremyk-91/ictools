package com.ictools.idapi.bayesnet;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.junit.Test;

import java.util.List;

public class MultipleParentsTest {
    @Test
    public void testHandlesMultipleParents() throws Exception {
        // This test is from the Intelligent Data & Probabilistic Inference 2015 examination.

        // Set up the network
        List<Edge> edgeListA = Lists.newArrayList();
        Node nodeA = new RootNode("A", Lists.newArrayList(0.6, 0.4), edgeListA);

        List<Edge> edgeListBParent = Lists.newArrayList();
        List<Edge> edgeListBChild = Lists.newArrayList();
        Node nodeB = new InternalNode("B", 2, edgeListBParent, edgeListBChild);

        List<Edge> edgeListCParent = Lists.newArrayList();
        List<Edge> edgeListCChild = Lists.newArrayList();
        Node nodeC = new InternalNode("C", 2, edgeListCParent, edgeListCChild);

        List<Edge> edgeListDParent = Lists.newArrayList();
        List<Edge> edgeListDChild = Lists.newArrayList();
        Node nodeD = new InternalNode("D", 2, edgeListDParent, edgeListDChild);

        List<Edge> edgeListE = Lists.newArrayList();
        Node nodeE = new InternalNode("E", 2, edgeListDChild, Lists.newArrayList());

        // Set up the Edges
        Table<Integer, List<Integer>, Double> cpmBgivenA = HashBasedTable.create();
        cpmBgivenA.put(0, Lists.newArrayList(0), 0.4);
        cpmBgivenA.put(1, Lists.newArrayList(0), 0.6);
        cpmBgivenA.put(0, Lists.newArrayList(1), 0.0);
        cpmBgivenA.put(1, Lists.newArrayList(1), 1.0);
        Edge bGivenA = new EdgeImpl(Lists.newArrayList(nodeA), nodeB, cpmBgivenA);
        edgeListA.add(bGivenA);
        edgeListBParent.add(bGivenA);

        Table<Integer, List<Integer>, Double> cpmCgivenA = HashBasedTable.create();
        cpmCgivenA.put(0, Lists.newArrayList(0), 0.5);
        cpmCgivenA.put(1, Lists.newArrayList(0), 0.5);
        cpmCgivenA.put(0, Lists.newArrayList(1), 0.3);
        cpmCgivenA.put(1, Lists.newArrayList(1), 0.7);
        Edge cGivenA = new EdgeImpl(Lists.newArrayList(nodeA), nodeC, cpmCgivenA);
        edgeListA.add(cGivenA);
        edgeListCParent.add(cGivenA);

        Table<Integer, List<Integer>, Double> cpmDgivenBC = HashBasedTable.create();
        cpmDgivenBC.put(0, Lists.newArrayList(0, 0), 0.2);
        cpmDgivenBC.put(1, Lists.newArrayList(0, 0), 0.8);
        cpmDgivenBC.put(0, Lists.newArrayList(0, 1), 1.0);
        cpmDgivenBC.put(1, Lists.newArrayList(0, 1), 0.0);
        cpmDgivenBC.put(0, Lists.newArrayList(1, 0), 0.3);
        cpmDgivenBC.put(1, Lists.newArrayList(1, 0), 0.7);
        cpmDgivenBC.put(0, Lists.newArrayList(1, 1), 0.1);
        cpmDgivenBC.put(1, Lists.newArrayList(1, 1), 0.9);
        Edge dGivenBC = new EdgeImpl(Lists.newArrayList(nodeB, nodeC), nodeD, cpmDgivenBC);
        edgeListBChild.add(dGivenBC);
        edgeListCChild.add(dGivenBC);
        edgeListDParent.add(dGivenBC);

        Table<Integer, List<Integer>, Double> cpmEgivenD = HashBasedTable.create();
        cpmEgivenD.put(0, Lists.newArrayList(0), 1.0);
        cpmEgivenD.put(1, Lists.newArrayList(0), 0.0);
        cpmEgivenD.put(0, Lists.newArrayList(1), 0.3);
        cpmEgivenD.put(1, Lists.newArrayList(1), 0.7);
        Edge eGivenD = new EdgeImpl(Lists.newArrayList(nodeD), nodeE, cpmEgivenD);
        edgeListDChild.add(eGivenD);

        // Initialise
        nodeA.receivePiMessage(new PiMessage(Lists.newArrayList(0.6, 0.4), null, null));

        // PART 1(a)
        TestUtils.checkVectorEquality(nodeA.getPosteriorDistribution(), Lists.newArrayList(0.6, 0.4));
        TestUtils.checkVectorEquality(nodeB.getPosteriorDistribution(), Lists.newArrayList(0.24, 0.76));
        TestUtils.checkVectorEquality(nodeC.getPosteriorDistribution(), Lists.newArrayList(0.42, 0.58));
        TestUtils.checkVectorEquality(nodeD.getPosteriorDistribution(), Lists.newArrayList(0.2992, 0.7008));
        TestUtils.checkVectorEquality(nodeE.getPosteriorDistribution(), Lists.newArrayList(0.50944, 0.49056));

        // PART 1(d)
        nodeC.instantiate(1);
        nodeE.instantiate(0);

        TestUtils.checkVectorEquality(nodeA.getPosteriorDistribution(), Lists.newArrayList(0.6430048242, 0.3569951758));
        TestUtils.checkVectorEquality(nodeB.getPosteriorDistribution(), Lists.newArrayList(0.4135079256, 0.5864920744));
        TestUtils.checkVectorEquality(nodeC.getPosteriorDistribution(), Lists.newArrayList(0.0, 1.0));
        TestUtils.checkVectorEquality(nodeD.getPosteriorDistribution(), Lists.newArrayList(0.5720192970, 0.4279807030));
        TestUtils.checkVectorEquality(nodeE.getPosteriorDistribution(), Lists.newArrayList(1.0, 0.0));

    }
}
