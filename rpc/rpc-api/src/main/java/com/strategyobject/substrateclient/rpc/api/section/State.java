package com.strategyobject.substrateclient.rpc.api.section;

import com.strategyobject.substrateclient.rpc.annotation.RpcCall;
import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;
import com.strategyobject.substrateclient.rpc.annotation.RpcSubscription;
import com.strategyobject.substrateclient.rpc.api.*;
import com.strategyobject.substrateclient.scale.annotation.Scale;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@RpcInterface("state")
public interface State {
    @RpcCall("getRuntimeVersion")
    CompletableFuture<RuntimeVersion> getRuntimeVersion();

    @RpcCall("getMetadata")
    @Scale
    CompletableFuture<Metadata> getMetadata();

    @RpcCall("getKeys")
    CompletableFuture<List<StorageKey>> getKeys(StorageKey key);

    @RpcCall("getKeys")
    CompletableFuture<List<StorageKey>> getKeys(StorageKey key, @Scale BlockHash at);

    @RpcCall("getKeysPaged")
    CompletableFuture<List<StorageKey>> getKeysPaged(StorageKey key, int count);

    @RpcCall("getKeysPaged")
    CompletableFuture<List<StorageKey>> getKeysPaged(StorageKey key, int count, StorageKey startKey);

    @RpcCall("getKeysPaged")
    CompletableFuture<List<StorageKey>> getKeysPaged(StorageKey key,
                                                     int count,
                                                     StorageKey startKey,
                                                     @Scale BlockHash at);

    @RpcCall("getStorage")
    CompletableFuture<StorageData> getStorage(StorageKey key);

    @RpcCall("getStorage")
    CompletableFuture<StorageData> getStorage(StorageKey key, @Scale BlockHash at);

    @RpcCall("getStorageHash")
    @Scale
    CompletableFuture<Hash> getStorageHash(StorageKey key);

    @RpcCall("getStorageHash")
    @Scale
    CompletableFuture<Hash> getStorageHash(StorageKey key, @Scale BlockHash at);

    @RpcCall("getStorageSize")
    CompletableFuture<Long> getStorageSize(StorageKey key);

    @RpcCall("getStorageSize")
    CompletableFuture<Long> getStorageSize(StorageKey key, @Scale BlockHash at);

    @RpcCall("queryStorage")
    CompletableFuture<List<StorageChangeSet>> queryStorage(List<StorageKey> keys, @Scale BlockHash fromBlock);

    @RpcCall("queryStorage")
    CompletableFuture<List<StorageChangeSet>> queryStorage(List<StorageKey> keys,
                                                           @Scale BlockHash fromBlock,
                                                           @Scale BlockHash toBlock);

    @RpcCall("queryStorageAt")
    CompletableFuture<List<StorageChangeSet>> queryStorageAt(List<StorageKey> keys);

    @RpcCall("queryStorageAt")
    CompletableFuture<List<StorageChangeSet>> queryStorageAt(List<StorageKey> keys, @Scale BlockHash at);

    @RpcSubscription(type = "storage", subscribeMethod = "subscribeStorage", unsubscribeMethod = "unsubscribeStorage")
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> subscribeStorage(List<StorageKey> keys,
                                                                             BiConsumer<Exception, StorageChangeSet> callback);
}
