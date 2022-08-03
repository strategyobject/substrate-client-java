package com.strategyobject.substrateclient.pallet.storage;

import com.strategyobject.substrateclient.common.types.tuple.Pair;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockHash;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Represents a storage of data in blockchain
 * that persists between the blocks.
 *
 * @param <V> the type of the value.
 */
public interface StorageNMap<V> {
    /**
     * Returns the value by given key(s).
     *
     * @param keys the keys of the storage's entry.
     * @return the value.
     */
    CompletableFuture<V> get(@NonNull Object... keys);

    /**
     * Returns the value by given key(s)
     * starting at block hash given as the first parameter.
     *
     * @param block hash of the block
     *              used as a starting point for the value request.
     * @param keys  the keys of the storage's entry.
     * @return the value.
     */
    CompletableFuture<V> at(@NonNull BlockHash block, @NonNull Object... keys);

    /**
     * Retrieves the storage hash.
     *
     * @param keys the keys of the storage's entry.
     * @return the hash.
     */
    CompletableFuture<Hash> hash(@NonNull Object... keys);

    /**
     * Retrieves the storage hash
     * starting at block hash given as the first parameter.
     *
     * @param block hash of the block
     *              used as a starting point for the hash request.
     * @param keys  the keys of the storage's entry.
     * @return the hash.
     */
    CompletableFuture<Hash> hashAt(@NonNull BlockHash block, @NonNull Object... keys);

    /**
     * Retrieves the storage size.
     *
     * @param keys the keys of the storage's entry.
     * @return the size of the entry.
     */
    CompletableFuture<Long> size(@NonNull Object... keys);

    /**
     * Retrieves the storage size
     * starting at block hash given as the first parameter.
     *
     * @param block hash of the block
     *              used as a starting point for the size request.
     * @param keys  the keys of the storage's entry.
     * @return the size of the entry.
     */
    CompletableFuture<Long> sizeAt(@NonNull BlockHash block, @NonNull Object... keys);

    /**
     * Returns a collection of keys, each item of which
     * contains the remaining keys of the entry.
     *
     * @param keys some keys of the storage's entry.
     *             Note: all the keys must follow the same strict order as
     *             they appear in the storage.
     * @return collection of keys.
     */
    CompletableFuture<KeyCollection<V>> keys(@NonNull Object... keys);


    /**
     * Returns a collection of keys, each item of which
     * contains the remaining keys of the entry.
     *
     * @param block hash of the block
     *              since that the keys are requested.
     * @param keys  some keys of the storage's entry.
     *              Note: all the keys must follow the same strict order as
     *              their sequence in the storage.
     * @return collection of keys.
     */
    CompletableFuture<KeyCollection<V>> keysAt(@NonNull BlockHash block, @NonNull Object... keys);

    /**
     * Returns an iterator of a key collection, each item of which
     * contains the remaining keys of the entry.
     * Each item of the iterator contains a key collection with size equal to the page's size
     * or smaller in case of the last page and the total number isn't multiple of the page's size.
     *
     * @param pageSize number of items in collection per page.
     * @param keys     some keys of the storage's entry.
     *                 Note: all the keys must follow the same strict order as
     *                 their sequence in the storage.
     * @return iterator of key collections.
     */
    PagedKeyIterator<V> keysPaged(int pageSize, @NonNull Object... keys);

    /**
     * @param keys the keys of the storage's entry.
     * @return the queryable key that allows to request value.
     * The queryable key can also be joined to other keys for forming a multi query.
     */
    QueryableKey<V> query(@NonNull Object... keys);

    /**
     * @param keys the keys of the storage's entry.
     * @return list of pairs. Each pair contains a hash of the block
     * where the change(s) happened and list of values.
     */
    CompletableFuture<List<Pair<BlockHash, List<V>>>> history(@NonNull Object... keys);

    /**
     * Returns a key-value collection, each item of which
     * contains the remaining keys of the entry and its value.
     *
     * @param keys some keys of the storage's entry.
     *             Note: all the keys must follow the same strict order as
     *             they appear in the storage.
     * @return collection of key-values.
     */
    CompletableFuture<KeyValueCollection<V>> entries(@NonNull Object... keys);

    /**
     * Returns an iterator of a key-value collection, each item of which
     * contains the remaining keys and the value.
     * Each item of the iterator contains a key-value collection with size equal to the page's size
     * or smaller in case of the last page and the total number isn't multiple of tbe page's size.
     *
     * @param pageSize number of items in collection per page.
     * @param keys     some keys of the storage's entry.
     *                 Note: all the keys must follow the same strict order as
     *                 their sequence in the storage.
     * @return collection of key-values.
     */
    PagedKeyValueIterator<V> entriesPaged(int pageSize, @NonNull Object... keys);

    /**
     * Returns a collection of key-values, each item of which
     * contains all the keys of the entry and its value.
     *
     * @param args any number of arguments to request
     *             multiple entries.
     * @return collection of key-value.
     */
    CompletableFuture<KeyValueCollection<V>> multi(@NonNull Arg... args);

    /**
     * Subscribes for changes on storage's entries with given args.
     *
     * @param consumer that consumes changes.
     * @param args     any number of arguments.
     * @return an operation that allows to unsubscribe.
     */
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> subscribe(@NonNull StorageChangeConsumer<V> consumer,
                                                                      @NonNull Arg... args);
}
