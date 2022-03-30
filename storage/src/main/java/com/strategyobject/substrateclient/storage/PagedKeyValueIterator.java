package com.strategyobject.substrateclient.storage;

import java.util.concurrent.CompletableFuture;

/**
 * An iterator of key-value collection.
 * Each element in the sequence contains a key-value collection with size
 * equal to the page's size or smaller in case of the last page and the total number
 * isn't multiple of tbe page's size.
 * @param <V> the type of the value.
 */
public interface PagedKeyValueIterator<V> {
    /**
     * @return number of the current page.
     */
    int number();

    /**
     * Requests the next page and returns the flag of existence of such page.
     * @return true if the next page exists.
     */
    CompletableFuture<Boolean> moveNext();

    /**
     * @return the current page of key-values.
     */
    KeyValueCollection<V> current();
}
