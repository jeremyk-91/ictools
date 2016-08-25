package com.ictools.algorithms.graph.maxflow;

import com.google.common.collect.HashBasedTable;
import com.ictools.algorithms.graph.structures.TableGraph;

public final class TestFlowNetworks {

    private TestFlowNetworks() {
        // Utility
    }

    public static FlowNetwork getTestFlowNetworkSimple() {
        HashBasedTable<Long, Long, Long> table = HashBasedTable.create(3, 3);
        table.put(0l, 1l, 5l);
        table.put(1l, 2l, 7l);
        return new FlowNetwork(new TableGraph(table), 0l, 2l);
    }

    public static FlowNetwork getTestFlowNetworkMedium() {
        HashBasedTable<Long, Long, Long> table = HashBasedTable.create(10, 10);
        table.put(0l, 1l, 3l);
        table.put(1l, 2l, 2l);
        table.put(2l, 3l, 4l);
        table.put(3l, 6l, 4l);
        table.put(0l, 4l, 3l);
        table.put(1l, 4l, 6l);
        table.put(4l, 5l, 3l);
        table.put(5l, 6l, 2l);
        table.put(1l, 6l, 3l);
        table.put(4l, 7l, 2l);
        table.put(5l, 7l, 1l);
        table.put(9l, 0l, 6l);
        table.put(9l, 4l, 6l);
        table.put(9l, 7l, 2l);
        table.put(3l, 10l, 7l);
        table.put(6l, 10l, 3l);
        table.put(7l, 10l, 6l);
        return new FlowNetwork(new TableGraph(table), 9l, 10l);
    }

    public static FlowNetwork getTestFlowNetworkPathological() {
        HashBasedTable<Long, Long, Long> table = HashBasedTable.create(4, 4);
        table.put(0l, 1l, 1000000000l);
        table.put(0l, 2l, 1000000000l);
        table.put(1l, 3l, 1000000000l);
        table.put(2l, 3l, 1000000000l);
        table.put(1l, 2l, 1l);
        return new FlowNetwork(new TableGraph(table), 0l, 3l);
    }

}
