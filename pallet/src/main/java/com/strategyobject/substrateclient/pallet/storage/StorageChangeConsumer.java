package com.strategyobject.substrateclient.pallet.storage;

import com.strategyobject.substrateclient.rpc.api.BlockHash;

import java.util.List;

/**
 * Represents an operation that accepts
 * exception, hash of the block where change happened,
 * the value and list of the keys of the entry.
 * Doesn't return result.
 *
 * @param <V> the type of the value.
 */
@FunctionalInterface
public interface StorageChangeConsumer<V> {
    /**
     * Performs this operation on the given arguments.
     *
     * @param exception happened on event.
     * @param block     hash of the block where the change happened.
     * @param value     new value of the storage's entry.
     * @param keys      list of the storage entry's keys.
     */
    void accept(Exception exception, BlockHash block, V value, List<Object> keys);
}
