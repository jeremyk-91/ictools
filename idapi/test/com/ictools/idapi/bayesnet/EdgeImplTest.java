package com.ictools.idapi.bayesnet;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.junit.Test;

import java.util.List;

public class EdgeImplTest {

    @Test
    public void testInstantiateChild_Definite() throws Exception {
        List<Edge> edgeList = Lists.newArrayList();
        Node source = new RootNode("foo", Lists.newArrayList(0.5, 0.5), edgeList);
        Node sink = new InternalNode("bar", 2, edgeList, Lists.newArrayList());

        Table<Integer, List<Integer>, Double> probMatrix = HashBasedTable.create();
        probMatrix.put(0, Lists.newArrayList(0), 0.0);
        probMatrix.put(1, Lists.newArrayList(0), 1.0);
        probMatrix.put(0, Lists.newArrayList(1), 1.0);
        probMatrix.put(1, Lists.newArrayList(1), 0.0);

        Edge e = new EdgeImpl(Lists.newArrayList(source), sink, probMatrix);
        edgeList.add(e);

        sink.instantiate(1);
        List<Double> result = source.getPosteriorDistribution();
        TestUtils.checkVectorEquality(result, Lists.newArrayList(1.0, 0.0));
    }

    @Test
    public void testInstantiateChild_NonDefinite() throws Exception {
        List<Edge> edgeList = Lists.newArrayList();
        Node source = new RootNode("foo", Lists.newArrayList(0.4, 0.6), edgeList);
        Node sink = new InternalNode("bar", 2, edgeList, Lists.newArrayList());

        Table<Integer, List<Integer>, Double> probMatrix = HashBasedTable.create();
        probMatrix.put(0, Lists.newArrayList(0), 0.4);
        probMatrix.put(1, Lists.newArrayList(0), 0.6);
        probMatrix.put(0, Lists.newArrayList(1), 0.2);
        probMatrix.put(1, Lists.newArrayList(1), 0.8);

        Edge e = new EdgeImpl(Lists.newArrayList(source), sink, probMatrix);
        edgeList.add(e);

        sink.instantiate(1);
        List<Double> result = source.getPosteriorDistribution();
        TestUtils.checkVectorEquality(result, Lists.newArrayList(1./3, 2./3));
    }

    @Test
    public void testMeaninglessVirtualEvidenceForChild() throws Exception {
        List<Edge> edgeList = Lists.newArrayList();
        Node source = new RootNode("foo", Lists.newArrayList(0.4, 0.6), edgeList);
        Node sink = new InternalNode("bar", 2, edgeList, Lists.newArrayList());

        Table<Integer, List<Integer>, Double> probMatrix = HashBasedTable.create();
        probMatrix.put(0, Lists.newArrayList(0), 0.4);
        probMatrix.put(1, Lists.newArrayList(0), 0.6);
        probMatrix.put(0, Lists.newArrayList(1), 0.2);
        probMatrix.put(1, Lists.newArrayList(1), 0.8);

        Edge e = new EdgeImpl(Lists.newArrayList(source), sink, probMatrix);
        edgeList.add(e);

        sink.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(0.5, 0.5), sink));
        List<Double> result = source.getPosteriorDistribution();
        TestUtils.checkVectorEquality(result, Lists.newArrayList(0.4, 0.6));
    }

    @Test
    public void testVirtualEvidenceForChild() throws Exception {
        List<Edge> edgeList = Lists.newArrayList();
        Node source = new RootNode("foo", Lists.newArrayList(0.4, 0.6), edgeList);
        Node sink = new InternalNode("bar", 2, edgeList, Lists.newArrayList());

        Table<Integer, List<Integer>, Double> probMatrix = HashBasedTable.create();
        probMatrix.put(0, Lists.newArrayList(0), 0.4);
        probMatrix.put(1, Lists.newArrayList(0), 0.6);
        probMatrix.put(0, Lists.newArrayList(1), 0.2);
        probMatrix.put(1, Lists.newArrayList(1), 0.8);

        Edge e = new EdgeImpl(Lists.newArrayList(source), sink, probMatrix);
        edgeList.add(e);

        sink.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(0.2, 0.8), sink));
        List<Double> result = source.getPosteriorDistribution();
        TestUtils.checkVectorRatios(result, Lists.newArrayList(0.4 * 56. / (56 + 68), 0.6 * 68. / (56 + 68)));
    }
}
