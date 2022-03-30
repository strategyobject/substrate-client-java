package com.strategyobject.substrateclient.storage;

import com.google.common.annotations.Beta;
import com.strategyobject.substrateclient.rpc.types.BlockHash;
import com.strategyobject.substrateclient.types.tuples.Pair;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Beta
public interface StorageMap<K, V> {
    CompletableFuture<V> get(@NonNull K key);

    CompletableFuture<V> at(@NonNull BlockHash block, @NonNull K key);

    CompletableFuture<List<Pair<BlockHash, List<V>>>> history(@NonNull K key);
}

