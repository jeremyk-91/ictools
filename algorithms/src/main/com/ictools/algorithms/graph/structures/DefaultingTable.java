package com.ictools.algorithms.graph.structures;

import com.google.common.collect.Table;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A DefaultingTable is like a standard Guava Table.
 * However, it also returns the specified default value in the event that get() is tried on a result which doesn't seem to exist.
 */
public class DefaultingTable<R, C, V> implements Table<R, C, V> {

    private final Table<R, C, V> delegate;
    private final V defaultValue;

    public DefaultingTable(Table<R, C, V> delegate, V defaultValue) {
        this.delegate = delegate;
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean contains(Object rowKey, Object columnKey) {
        return delegate.contains(rowKey, columnKey);
    }

    @Override
    public boolean containsRow(Object rowKey) {
        return delegate.containsRow(rowKey);
    }

    @Override
    public boolean containsColumn(Object columnKey) {
        return delegate.containsColumn(columnKey);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public V get(Object rowKey, Object columnKey) {
        V result = delegate.get(rowKey, columnKey);
        return result == null ? defaultValue : result;
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public V put(R rowKey, C columnKey, V value) {
        return delegate.put(rowKey, columnKey, value);
    }

    @Override
    public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
        delegate.putAll(table);
    }

    @Override
    public V remove(Object rowKey, Object columnKey) {
        return delegate.remove(rowKey, columnKey);
    }

    @Override
    public Map<C, V> row(R rowKey) {
        return delegate.row(rowKey);
    }

    @Override
    public Map<R, V> column(C columnKey) {
        return delegate.column(columnKey);
    }

    @Override
    public Set<Cell<R, C, V>> cellSet() {
        return delegate.cellSet();
    }

    @Override
    public Set<R> rowKeySet() {
        return delegate.rowKeySet();
    }

    @Override
    public Set<C> columnKeySet() {
        return delegate.columnKeySet();
    }

    @Override
    public Collection<V> values() {
        return delegate.values();
    }

    @Override
    public Map<R, Map<C, V>> rowMap() {
        return delegate.rowMap();
    }

    @Override
    public Map<C, Map<R, V>> columnMap() {
        return delegate.columnMap();
    }
}
