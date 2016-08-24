package com.ictools.algorithms.graph.maxflow;

import com.google.common.collect.Table;

public class MaxFlowResult {
    private final Table<Long, Long, Long> maximumFlow;
    private final long maximumFlowValue;

    public MaxFlowResult(Table<Long, Long, Long> maximumFlow, long maximumFlowValue) {
        this.maximumFlow = maximumFlow;
        this.maximumFlowValue = maximumFlowValue;
    }

    public Table<Long, Long, Long> getMaximumFlow() {
        return maximumFlow;
    }

    public long getMaximumFlowValue() {
        return maximumFlowValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MaxFlowResult that = (MaxFlowResult) o;

        if (maximumFlowValue != that.maximumFlowValue) return false;
        if (maximumFlow != null ? !maximumFlow.equals(that.maximumFlow) : that.maximumFlow != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = maximumFlow != null ? maximumFlow.hashCode() : 0;
        result = 31 * result + (int) (maximumFlowValue ^ (maximumFlowValue >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "MaxFlowResult{" +
                "maximumFlow=" + maximumFlow +
                ", maximumFlowValue=" + maximumFlowValue +
                '}';
    }
}
