package com.ictools.algorithms.graph.structures;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleNode that = (SimpleNode) o;

        if (identifier != that.identifier) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (identifier ^ (identifier >>> 32));
    }

    @Override
    public String toString() {
        return "SimpleNode{" +
                "identifier=" + identifier +
                ", value=" + value +
                '}';
    }
}
