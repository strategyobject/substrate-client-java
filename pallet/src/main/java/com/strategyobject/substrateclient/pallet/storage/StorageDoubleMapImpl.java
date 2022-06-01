package com.strategyobject.substrateclient.pallet.storage;

import com.google.common.annotations.Beta;
import com.strategyobject.substrateclient.rpc.api.BlockHash;
import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@Beta
@RequiredArgsConstructor(staticName = "with")
public class StorageDoubleMapImpl<F, S, V> implements StorageDoubleMap<F, S, V> {
    private final StorageNMap<V> underlying;

    private StorageDoubleMapImpl(@NonNull State state,
                                 @NonNull ScaleReader<V> scaleReader,
                                 @NonNull StorageKeyProvider storageKeyProvider) {
        underlying = StorageNMapImpl.with(state, scaleReader, storageKeyProvider);
    }

    public static <F, S, V> StorageDoubleMap<F, S, V> with(@NonNull State state,
                                                           @NonNull ScaleReader<V> scaleReader,
                                                           @NonNull StorageKeyProvider storageKeyProvider) {
        return new StorageDoubleMapImpl<>(state, scaleReader, storageKeyProvider);
    }

    @Override
    public CompletableFuture<V> get(@NonNull F first, @NonNull S second) {
        return underlying.get(first, second);
    }

    @Override
    public CompletableFuture<V> at(@NonNull BlockHash block, @NonNull F first, @NonNull S second) {
        return underlying.at(block, first, second);
    }
}
