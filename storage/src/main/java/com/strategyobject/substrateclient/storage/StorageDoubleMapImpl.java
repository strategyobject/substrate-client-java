package com.strategyobject.substrateclient.storage;

import com.google.common.annotations.Beta;
import com.strategyobject.substrateclient.rpc.Rpc;
import com.strategyobject.substrateclient.rpc.types.BlockHash;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@Beta
@RequiredArgsConstructor(staticName = "with")
public class StorageDoubleMapImpl<F, S, V> implements StorageDoubleMap<F, S, V> {
    private final StorageNMap<V> underlying;

    private StorageDoubleMapImpl(@NonNull Rpc rpc,
                                 @NonNull ScaleReader<V> scaleReader,
                                 @NonNull StorageKeyProvider storageKeyProvider) {
        underlying = StorageNMapImpl.with(rpc, scaleReader, storageKeyProvider);
    }

    public static <F, S, V> StorageDoubleMap<F, S, V> with(@NonNull Rpc rpc,
                                                           @NonNull ScaleReader<V> scaleReader,
                                                           @NonNull StorageKeyProvider storageKeyProvider) {
        return new StorageDoubleMapImpl<>(rpc, scaleReader, storageKeyProvider);
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
