package com.ictools.algorithms.graph.maxflow;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.ictools.algorithms.graph.structures.DefaultingTable;

public final class FlowConsolidationUtils {

    private FlowConsolidationUtils() {
        // utility
    }

    public static MaxFlowResult resolveAndConsolidateFlows(FlowNetwork flowNetwork, DefaultingTable<Long, Long, Long> flow) {
        Table<Long, Long, Long> finalFlow = new DefaultingTable<>(HashBasedTable.create(), 0l);
        for (Table.Cell<Long, Long, Long> cell : flow.cellSet()) {
            if (flowNetwork.getCapacity(cell.getRowKey(), cell.getColumnKey()) > 0) {
                finalFlow.put(cell.getRowKey(), cell.getColumnKey(),
                        finalFlow.get(cell.getRowKey(), cell.getColumnKey()) + cell.getValue());
            } else {
                finalFlow.put(cell.getColumnKey(), cell.getRowKey(),
                        finalFlow.get(cell.getColumnKey(), cell.getRowKey()) - cell.getValue());
            }
        }
        return new MaxFlowResult(finalFlow,
                finalFlow.row(flowNetwork.getSource()).values().stream().mapToLong(Long::longValue).sum());
    }
}
