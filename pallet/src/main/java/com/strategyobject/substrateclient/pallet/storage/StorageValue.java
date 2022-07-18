package com.strategyobject.substrateclient.pallet.storage;

import com.google.common.annotations.Beta;
import com.strategyobject.substrateclient.common.types.tuple.Pair;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockHash;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Beta
public interface StorageValue<V> {
    CompletableFuture<V> get();

    CompletableFuture<V> at(@NonNull BlockHash block);

    CompletableFuture<List<Pair<BlockHash, List<V>>>> history();

    QueryableKey<V> query();
}
