package com.ictools.algorithms.graph;

public class SimpleNode<T> implements Node {
    private final long identifier;
    private final T value;

    public SimpleNode(long identifier, T value) {
        this.identifier = identifier;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public long getIdentifier() {
        return identifier;
    }
}
