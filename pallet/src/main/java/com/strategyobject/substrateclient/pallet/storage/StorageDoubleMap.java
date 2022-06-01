package com.strategyobject.substrateclient.pallet.storage;

import com.google.common.annotations.Beta;
import com.strategyobject.substrateclient.rpc.api.BlockHash;
import lombok.NonNull;

import java.util.concurrent.CompletableFuture;

@Beta
public interface StorageDoubleMap<F, S, V> {
    CompletableFuture<V> get(@NonNull F first, @NonNull S second);

    CompletableFuture<V> at(@NonNull BlockHash block, @NonNull F first, @NonNull S second);
}
