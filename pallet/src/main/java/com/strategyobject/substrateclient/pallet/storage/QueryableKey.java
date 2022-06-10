package com.strategyobject.substrateclient.pallet.storage;

import com.strategyobject.substrateclient.rpc.api.StorageKey;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a query to the storage.
 * Can be executed or joined to other queries.
 *
 * @param <V> the type of the value.
 */
public interface QueryableKey<V> {
    /**
     * @return value.
     */
    CompletableFuture<V> execute();

    /**
     * @return StorageKey produced from given key.
     */
    StorageKey getKey();

    /**
     * @return reader for the value of the storage's entry.
     */
    ScaleReader<V> getValueReader();

    /**
     * @param consumer that consumes keys of the entry.
     */
    void consume(@NonNull KeyConsumer consumer);

    /**
     * @param others keys to be joined.
     * @return the multi query that allows to make single request to storages with given keys.
     */
    MultiQuery<Object> join(@NonNull QueryableKey<?>... others);

    /**
     * @param fullKey StorageKey that contains pallet and storage names and all keys
     *                of the storage's entry.
     * @return all keys of the entry.
     */
    List<Object> extractKeys(@NonNull StorageKey fullKey);
}
