package com.strategyobject.substrateclient.storage;

import lombok.NonNull;

import java.util.List;

/**
 * Represents an operation that accepts the value and the list of keys and returns no result.
 * @param <V> the type of the value.
 */
@FunctionalInterface
public interface KeyValueConsumer<V> {
    /**
     * Performs this operation on the given arguments.
     * @param value the value of the storage's entry.
     * @param keys the list of keys of the storage's entry.
     */
    void accept(V value, @NonNull List<Object> keys);
}
