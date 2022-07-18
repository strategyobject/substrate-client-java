package com.strategyobject.substrateclient.rpc.api.section;

import com.strategyobject.substrateclient.rpc.annotation.RpcCall;
import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;
import com.strategyobject.substrateclient.rpc.annotation.RpcSubscription;
import com.strategyobject.substrateclient.rpc.api.*;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockHash;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash;
import com.strategyobject.substrateclient.rpc.api.storage.StorageChangeSet;
import com.strategyobject.substrateclient.rpc.api.storage.StorageData;
import com.strategyobject.substrateclient.rpc.api.storage.StorageKey;
import com.strategyobject.substrateclient.scale.annotation.Scale;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@RpcInterface("state")
public interface State {
    /**
     * Returns the keys with prefix, leave empty to get all the keys.
     *
     * @param key prefix
     * @return storage keys
     */
    @RpcCall("getKeys")
    CompletableFuture<List<StorageKey>> getKeys(StorageKey key);


    /**
     * Returns the keys with prefix, leave empty to get all the keys.
     *
     * @param key prefix
     * @param at  block hash
     * @return storage keys
     */
    @RpcCall("getKeys")
    CompletableFuture<List<StorageKey>> getKeys(StorageKey key, BlockHash at);

    /**
     * Returns the keys with prefix with pagination support.
     *
     * @param key   prefix
     * @param count up to count keys will be returned
     * @return storage keys
     */
    @RpcCall("getKeysPaged")
    CompletableFuture<List<StorageKey>> getKeysPaged(StorageKey key, int count);

    /**
     * Returns the keys with prefix with pagination support.
     *
     * @param key      prefix
     * @param count    up to count keys will be returned
     * @param startKey if `start_key` is passed, return next keys in storage in lexicographic order
     * @return storage keys
     */
    @RpcCall("getKeysPaged")
    CompletableFuture<List<StorageKey>> getKeysPaged(StorageKey key, int count, StorageKey startKey);

    /**
     * Returns the keys with prefix with pagination support.
     *
     * @param key      prefix
     * @param count    up to count keys will be returned
     * @param startKey if `start_key` is passed, return next keys in storage in lexicographic order
     * @param at       block hash
     * @return storage keys
     */
    @RpcCall("getKeysPaged")
    CompletableFuture<List<StorageKey>> getKeysPaged(StorageKey key,
                                                     int count,
                                                     StorageKey startKey,
                                                     BlockHash at);

    /**
     * Returns the runtime metadata as an opaque blob.
     *
     * @return runtime metadata
     */
    @RpcCall("getMetadata")
    @Scale
    CompletableFuture<Metadata> getMetadata();

    /**
     * Returns the runtime metadata as an opaque blob.
     *
     * @param at block hash
     * @return runtime metadata
     */
    @RpcCall("getMetadata")
    @Scale
    CompletableFuture<Metadata> getMetadata(BlockHash at);

    /**
     * Get the runtime version.
     *
     * @return runtime version
     */
    @RpcCall("getRuntimeVersion")
    CompletableFuture<RuntimeVersion> getRuntimeVersion();

    /**
     * Get the runtime version.
     *
     * @param at block hash
     * @return runtime version
     */
    @RpcCall("getRuntimeVersion")
    CompletableFuture<RuntimeVersion> getRuntimeVersion(BlockHash at);

    /**
     * Returns a storage entry at a specific block's state.
     *
     * @param key storage key
     * @return storage entry
     */
    @RpcCall("getStorage")
    CompletableFuture<StorageData> getStorage(StorageKey key);

    /**
     * Returns a storage entry at a specific block's state.
     *
     * @param key storage key
     * @param at  block hash
     * @return storage entry
     */
    @RpcCall("getStorage")
    CompletableFuture<StorageData> getStorage(StorageKey key, BlockHash at);

    /**
     * Returns the hash of a storage entry at a block's state.
     *
     * @param key storage key
     * @return hash of a storage entry
     */
    @RpcCall("getStorageHash")
    CompletableFuture<Hash> getStorageHash(StorageKey key);

    /**
     * Returns the hash of a storage entry at a block's state.
     *
     * @param key storage key
     * @param at  block hash
     * @return hash of a storage entry
     */
    @RpcCall("getStorageHash")
    CompletableFuture<Hash> getStorageHash(StorageKey key, BlockHash at);

    /**
     * Returns the size of a storage entry at a block's state.
     *
     * @param key storage key
     * @return size of a storage entry
     */
    @RpcCall("getStorageSize")
    CompletableFuture<Long> getStorageSize(StorageKey key);

    /**
     * Returns the size of a storage entry at a block's state.
     *
     * @param key storage key
     * @param at  block hash
     * @return size of a storage entry
     */
    @RpcCall("getStorageSize")
    CompletableFuture<Long> getStorageSize(StorageKey key, BlockHash at);

    /**
     * Query historical storage entries (by key) starting from a block given as the second parameter.
     * NOTE: This first returned result contains the initial state of storage for all keys.
     * Subsequent values in the vector represent changes to the previous state (diffs).
     *
     * @param keys      storage keys
     * @param fromBlock hash of a starting block
     * @return historical storage entries
     */
    @RpcCall("queryStorage")
    CompletableFuture<List<StorageChangeSet>> queryStorage(List<StorageKey> keys, BlockHash fromBlock);

    /**
     * Query historical storage entries (by key) starting from a block given as the second parameter.
     * NOTE: This first returned result contains the initial state of storage for all keys.
     * Subsequent values in the vector represent changes to the previous state (diffs).
     *
     * @param keys      storage keys
     * @param fromBlock hash of a starting block
     * @param toBlock   hash of an ending block
     * @return historical storage entries
     */
    @RpcCall("queryStorage")
    CompletableFuture<List<StorageChangeSet>> queryStorage(List<StorageKey> keys,
                                                           BlockHash fromBlock,
                                                           BlockHash toBlock);

    /**
     * Query storage entries (by key) starting at block hash given as the second parameter.
     *
     * @param keys storage keys
     * @return storage entries
     */
    @RpcCall("queryStorageAt")
    CompletableFuture<List<StorageChangeSet>> queryStorageAt(List<StorageKey> keys);

    /**
     * Query storage entries (by key) starting at block hash given as the second parameter.
     *
     * @param keys storage keys
     * @param at   block hash
     * @return storage entries
     */
    @RpcCall("queryStorageAt")
    CompletableFuture<List<StorageChangeSet>> queryStorageAt(List<StorageKey> keys, BlockHash at);

    /**
     * Subscribes to storage changes for the provided keys.
     *
     * @param keys     storage keys
     * @param callback callback handler
     * @return unsubscribe delegate
     */
    @RpcSubscription(type = "storage", subscribeMethod = "subscribeStorage", unsubscribeMethod = "unsubscribeStorage")
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> subscribeStorage(List<StorageKey> keys,
                                                                             BiConsumer<Exception, StorageChangeSet> callback);
}
