package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;
import com.strategyobject.substrateclient.rpc.types.*;
import com.strategyobject.substrateclient.scale.annotations.Scale;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RpcInterface(section = "state")
public interface State {
    @RpcCall(method = "getRuntimeVersion")
    CompletableFuture<RuntimeVersion> getRuntimeVersion();

    @RpcCall(method = "getMetadata")
    @Scale
    CompletableFuture<Metadata> getMetadata();

    @RpcCall(method = "getKeys")
    CompletableFuture<List<StorageKey>> getKeys(StorageKey key);

    @RpcCall(method = "getStorage")
    CompletableFuture<StorageData> getStorage(StorageKey key);

    @RpcCall(method = "queryStorageAt")
    CompletableFuture<List<StorageChangeSet>> queryStorageAt(List<StorageKey> keys);
}
