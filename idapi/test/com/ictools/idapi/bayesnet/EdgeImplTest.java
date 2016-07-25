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

        Table<Integer, List<Integer>, Double> probMatrix = setupOneParentTestMatrix();

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

        Table<Integer, List<Integer>, Double> probMatrix = setupOneParentTestMatrix();

        Edge e = new EdgeImpl(Lists.newArrayList(source), sink, probMatrix);
        edgeList.add(e);

        sink.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(0.5, 0.5), sink));
        List<Double> result = source.getPosteriorDistribution();
        TestUtils.checkVectorEquality(result, Lists.newArrayList(0.4, 0.6));
    }

    private Table<Integer, List<Integer>, Double> setupOneParentTestMatrix() {
        Table<Integer, List<Integer>, Double> probMatrix = HashBasedTable.create();
        probMatrix.put(0, Lists.newArrayList(0), 0.4);
        probMatrix.put(1, Lists.newArrayList(0), 0.6);
        probMatrix.put(0, Lists.newArrayList(1), 0.2);
        probMatrix.put(1, Lists.newArrayList(1), 0.8);
        return probMatrix;
    }

    @Test
    public void testVirtualEvidenceForChild() throws Exception {
        List<Edge> edgeList = Lists.newArrayList();
        Node source = new RootNode("foo", Lists.newArrayList(0.4, 0.6), edgeList);
        Node sink = new InternalNode("bar", 2, edgeList, Lists.newArrayList());

        Table<Integer, List<Integer>, Double> probMatrix = setupOneParentTestMatrix();

        Edge e = new EdgeImpl(Lists.newArrayList(source), sink, probMatrix);
        edgeList.add(e);

        sink.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(0.2, 0.8), sink));
        List<Double> result = source.getPosteriorDistribution();
        TestUtils.checkVectorRatios(result, Lists.newArrayList(0.4 * 56. / (56 + 68), 0.6 * 68. / (56 + 68)));
    }

    @Test
    public void testMultipleParents() throws Exception {
        List<Edge> edgeList = Lists.newArrayList();
        Node source1 = new RootNode("foo1", Lists.newArrayList(0.1, 0.9), edgeList);
        Node source2 = new RootNode("foo2", Lists.newArrayList(0.7, 0.3), edgeList);
        Node sink = new InternalNode("bar", 2, edgeList, Lists.newArrayList());

        Table<Integer, List<Integer>, Double> probMatrix = setupTwoParentTestMatrix();

        Edge e = new EdgeImpl(Lists.newArrayList(source1, source2), sink, probMatrix);
        edgeList.add(e);

        sink.instantiate(0);
        List<Double> result1 = source1.getPosteriorDistribution();
        TestUtils.checkVectorRatios(result1, Lists.newArrayList(0.1 * (0.6), 0.9 * (0.4)));
        List<Double> result2 = source2.getPosteriorDistribution();
        TestUtils.checkVectorRatios(result2, Lists.newArrayList(0.7 * (0.7), 0.3 * (0.3)));
    }

    @Test
    public void testMultipleParentsVirtualEvidence() throws Exception {
        List<Edge> edgeList = Lists.newArrayList();
        Node source1 = new RootNode("foo1", Lists.newArrayList(0.1, 0.9), edgeList);
        Node source2 = new RootNode("foo2", Lists.newArrayList(0.7, 0.3), edgeList);
        Node sink = new InternalNode("bar", 2, edgeList, Lists.newArrayList());

        Table<Integer, List<Integer>, Double> probMatrix = setupTwoParentTestMatrix();

        Edge e = new EdgeImpl(Lists.newArrayList(source1, source2), sink, probMatrix);
        edgeList.add(e);

        sink.receiveLambdaMessage(new LambdaMessage(Lists.newArrayList(0.2, 0.8), sink));
        List<Double> result1 = source1.getPosteriorDistribution();
        TestUtils.checkVectorRatios(result1, Lists.newArrayList(0.1 * (1.24 / 2.6), 0.9 * (1.36 / 2.6)));
        List<Double> result2 = source2.getPosteriorDistribution();
        TestUtils.checkVectorRatios(result2, Lists.newArrayList(0.7 * (1.18 / 2.6), 0.3 * (1.42 / 2.6)));
    }

    private Table<Integer, List<Integer>, Double> setupTwoParentTestMatrix() {
        Table<Integer, List<Integer>, Double> probMatrix = HashBasedTable.create();
        probMatrix.put(0, Lists.newArrayList(0, 0), 0.4);
        probMatrix.put(1, Lists.newArrayList(0, 0), 0.6);
        probMatrix.put(0, Lists.newArrayList(0, 1), 0.2);
        probMatrix.put(1, Lists.newArrayList(0, 1), 0.8);
        probMatrix.put(0, Lists.newArrayList(1, 0), 0.3);
        probMatrix.put(1, Lists.newArrayList(1, 0), 0.7);
        probMatrix.put(0, Lists.newArrayList(1, 1), 0.1);
        probMatrix.put(1, Lists.newArrayList(1, 1), 0.9);
        return probMatrix;
    }

    @Test
    public void testDownPropagation() {
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

        source.instantiate(1);
        List<Double> result = sink.getPosteriorDistribution();
        TestUtils.checkVectorEquality(result, Lists.newArrayList(1.0, 0.0));
    }

    @Test
    public void testDownPropagationTwoChildren() {
        List<Edge> edgeListSource = Lists.newArrayList();
        List<Edge> edgeListSink1 = Lists.newArrayList();
        List<Edge> edgeListSink2 = Lists.newArrayList();
        Node source = new RootNode("foo", Lists.newArrayList(0.5, 0.5), edgeListSource);
        Node sink1 = new InternalNode("bar1", 2, edgeListSink1, Lists.newArrayList());
        Node sink2 = new InternalNode("bar2", 2, edgeListSink2, Lists.newArrayList());

        Table<Integer, List<Integer>, Double> probMatrix = setupOneParentTestMatrix();

        Edge e1 = new EdgeImpl(Lists.newArrayList(source), sink1, probMatrix);
        Edge e2 = new EdgeImpl(Lists.newArrayList(source), sink2, probMatrix);
        edgeListSource.add(e1);
        edgeListSource.add(e2);
        edgeListSink1.add(e1);
        edgeListSink2.add(e2);

        sink1.instantiate(1);
        List<Double> result = sink2.getPosteriorDistribution();
        TestUtils.checkVectorEquality(result, Lists.newArrayList(2./7, 5./7));
    }
}
