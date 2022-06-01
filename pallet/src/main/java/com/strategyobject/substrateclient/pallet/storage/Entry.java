package com.strategyobject.substrateclient.pallet.storage;

/**
 * Entry of keys and value of storage.
 * @param <V> the type of the value.
 */
public interface Entry<V> {
    /**
     * @param consumer that consumes value and keys of the entry.
     */
    void consume(KeyValueConsumer<V> consumer);
}
