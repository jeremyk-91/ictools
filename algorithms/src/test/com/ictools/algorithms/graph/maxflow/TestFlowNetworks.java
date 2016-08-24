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
