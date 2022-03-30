package com.strategyobject.substrateclient.storage;

import com.google.common.annotations.Beta;
import com.strategyobject.substrateclient.rpc.Rpc;
import com.strategyobject.substrateclient.rpc.types.BlockHash;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.types.tuples.Pair;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Beta
@RequiredArgsConstructor(staticName = "with")
public class StorageValueImpl<V> implements StorageValue<V> {
    private final StorageNMap<V> underlying;

    private StorageValueImpl(@NonNull Rpc rpc,
                             @NonNull ScaleReader<V> scaleReader,
                             @NonNull StorageKeyProvider storageKeyProvider) {
        underlying = StorageNMapImpl.with(rpc, scaleReader, storageKeyProvider);
    }

    public static <V> StorageValue<V> with(@NonNull Rpc rpc,
                                           @NonNull ScaleReader<V> scaleReader,
                                           @NonNull StorageKeyProvider storageKeyProvider) {
        return new StorageValueImpl<>(rpc, scaleReader, storageKeyProvider);
    }

    @Override
    public CompletableFuture<V> get() {
        return underlying.get();
    }

    @Override
    public CompletableFuture<V> at(@NonNull BlockHash block) {
        return underlying.at(block);
    }

    @Override
    public CompletableFuture<List<Pair<BlockHash, List<V>>>> history() {
        return underlying.history();
    }

    @Override
    public QueryableKey<V> query() {
        return underlying.query();
    }
}
