package com.strategyobject.substrateclient.pallet.storage;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a query to storage(s).
 * It allows to request multiple data within a single query.
 * @param <V> the type of the value.
 */
public interface MultiQuery<V> {
    /**
     * @return key-value collection of entries.
     */
    CompletableFuture<KeyValueCollection<V>> execute();
}
