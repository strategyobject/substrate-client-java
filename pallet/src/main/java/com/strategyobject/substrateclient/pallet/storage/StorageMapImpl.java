package com.strategyobject.substrateclient.pallet.storage;

import com.google.common.annotations.Beta;
import com.strategyobject.substrateclient.common.types.tuple.Pair;
import com.strategyobject.substrateclient.rpc.api.BlockHash;
import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Beta
public class StorageMapImpl<K, V> implements StorageMap<K, V> {
    private final StorageNMap<V> underlying;

    private StorageMapImpl(@NonNull State state,
                           @NonNull ScaleReader<V> scaleReader,
                           @NonNull StorageKeyProvider storageKeyProvider) {
        underlying = StorageNMapImpl.with(state, scaleReader, storageKeyProvider);
    }

    public static <K, V> StorageMap<K, V> with(@NonNull State state,
                                               @NonNull ScaleReader<V> scaleReader,
                                               @NonNull StorageKeyProvider storageKeyProvider) {
        return new StorageMapImpl<>(state, scaleReader, storageKeyProvider);
    }

    @Override
    public CompletableFuture<V> get(@NonNull K key) {
        return underlying.get(key);
    }

    @Override
    public CompletableFuture<V> at(@NonNull BlockHash block, @NonNull K key) {
        return underlying.at(block, key);
    }

    @Override
    public CompletableFuture<List<Pair<BlockHash, List<V>>>> history(@NonNull K key) {
        return underlying.history(key);
    }
}
