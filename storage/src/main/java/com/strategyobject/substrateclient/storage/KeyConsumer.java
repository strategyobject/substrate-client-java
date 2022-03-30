package com.strategyobject.substrateclient.storage;

import lombok.NonNull;

import java.util.List;

/**
 * Represents an operation that accepts list of keys and returns no result.
 */
@FunctionalInterface
public interface KeyConsumer {
    /**
     * Performs this operation on the given arguments.
     * @param keys the list of keys of the storage's entry.
     */
    void accept(@NonNull List<Object> keys);
}
