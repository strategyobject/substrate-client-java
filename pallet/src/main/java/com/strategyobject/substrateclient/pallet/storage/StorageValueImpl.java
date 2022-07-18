package com.strategyobject.substrateclient.pallet.storage;

import com.google.common.annotations.Beta;
import com.strategyobject.substrateclient.common.types.tuple.Pair;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockHash;
import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Beta
@RequiredArgsConstructor(staticName = "with")
public class StorageValueImpl<V> implements StorageValue<V> {
    private final StorageNMap<V> underlying;

    private StorageValueImpl(@NonNull State state,
                             @NonNull ScaleReader<V> scaleReader,
                             @NonNull StorageKeyProvider storageKeyProvider) {
        underlying = StorageNMapImpl.with(state, scaleReader, storageKeyProvider);
    }

    public static <V> StorageValue<V> with(@NonNull State state,
                                           @NonNull ScaleReader<V> scaleReader,
                                           @NonNull StorageKeyProvider storageKeyProvider) {
        return new StorageValueImpl<>(state, scaleReader, storageKeyProvider);
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
