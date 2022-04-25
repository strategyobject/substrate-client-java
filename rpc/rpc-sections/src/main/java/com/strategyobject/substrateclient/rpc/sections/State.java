package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcSubscription;
import com.strategyobject.substrateclient.rpc.types.*;
import com.strategyobject.substrateclient.scale.annotations.Scale;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@RpcInterface(section = "state")
public interface State {
    @RpcCall(method = "getRuntimeVersion")
    CompletableFuture<RuntimeVersion> getRuntimeVersion();

    @RpcCall(method = "getMetadata")
    @Scale
    CompletableFuture<Metadata> getMetadata();

    @RpcCall(method = "getKeys")
    CompletableFuture<List<StorageKey>> getKeys(StorageKey key);

    @RpcCall(method = "getKeys")
    CompletableFuture<List<StorageKey>> getKeys(StorageKey key, @Scale BlockHash at);

    @RpcCall(method = "getKeysPaged")
    CompletableFuture<List<StorageKey>> getKeysPaged(StorageKey key, int count);

    @RpcCall(method = "getKeysPaged")
    CompletableFuture<List<StorageKey>> getKeysPaged(StorageKey key, int count, StorageKey startKey);

    @RpcCall(method = "getKeysPaged")
    CompletableFuture<List<StorageKey>> getKeysPaged(StorageKey key,
                                                     int count,
                                                     StorageKey startKey,
                                                     @Scale BlockHash at);

    @RpcCall(method = "getStorage")
    CompletableFuture<StorageData> getStorage(StorageKey key);

    @RpcCall(method = "getStorage")
    CompletableFuture<StorageData> getStorage(StorageKey key, @Scale BlockHash at);

    @RpcCall(method = "getStorageHash")
    @Scale
    CompletableFuture<Hash> getStorageHash(StorageKey key);

    @RpcCall(method = "getStorageHash")
    @Scale
    CompletableFuture<Hash> getStorageHash(StorageKey key, @Scale BlockHash at);

    @RpcCall(method = "getStorageSize")
    CompletableFuture<Long> getStorageSize(StorageKey key);

    @RpcCall(method = "getStorageSize")
    CompletableFuture<Long> getStorageSize(StorageKey key, @Scale BlockHash at);

    @RpcCall(method = "queryStorage")
    CompletableFuture<List<StorageChangeSet>> queryStorage(List<StorageKey> keys, @Scale BlockHash fromBlock);

    @RpcCall(method = "queryStorage")
    CompletableFuture<List<StorageChangeSet>> queryStorage(List<StorageKey> keys,
                                                           @Scale BlockHash fromBlock,
                                                           @Scale BlockHash toBlock);

    @RpcCall(method = "queryStorageAt")
    CompletableFuture<List<StorageChangeSet>> queryStorageAt(List<StorageKey> keys);

    @RpcCall(method = "queryStorageAt")
    CompletableFuture<List<StorageChangeSet>> queryStorageAt(List<StorageKey> keys, @Scale BlockHash at);

    @RpcSubscription(type = "storage", subscribeMethod = "subscribeStorage", unsubscribeMethod = "unsubscribeStorage")
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> subscribeStorage(List<StorageKey> keys,
                                                                             BiConsumer<Exception, StorageChangeSet> callback);
}
