package com.strategyobject.substrateclient.pallet.storage;

import java.util.concurrent.CompletableFuture;


/**
 * An iterator of key collection.
 * Each element in the sequence contains a key collection with size
 * equal to the page's size or smaller in case of the last page and the total number
 * isn't multiple of tbe page's size.
 * @param <V> the type of the value.
 */
public interface PagedKeyIterator<V> {
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
     * @return the current page of keys.
     */
    KeyCollection<V> current();
}
