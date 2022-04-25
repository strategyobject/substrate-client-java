package com.strategyobject.substrateclient.storage;

import java.util.Iterator;

/**
 * Collection of storage's entries. Each entry contains keys and a value.
 * @param <V> the type of the value.
 */
public interface KeyValueCollection<V> {
    /**
     * @return iterator of entries.
     */
    Iterator<Entry<V>> iterator();
}
