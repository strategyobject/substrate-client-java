package com.strategyobject.substrateclient.pallet.storage;

import java.util.Iterator;

/**
 * Collection of keys of storage(s).
 * @param <V> the type of the value.
 */
public interface KeyCollection<V> {
    /**
     * @return number of keys.
     */
    int size();

    /**
     * @return the multi query that allows to make single request to storages with given keys.
     */
    MultiQuery<V> multi();

    /**
     * @return the queryable key that allows to request value.
     * The queryable key can also be joined to other keys for forming a multi query.
     */
    Iterator<QueryableKey<V>> iterator();
}
