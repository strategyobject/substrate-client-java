package com.strategyobject.substrateclient.pallet.transaction;

import com.strategyobject.substrateclient.rpc.api.ExtrinsicStatus;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor(staticName = "with")
public class SignedExtrinsicImpl implements SignedExtrinsic {
    private final Supplier<byte[]> binaryWriter;
    private final Supplier<CompletableFuture<Hash>> txSender;
    private final Function<BiConsumer<Exception, ExtrinsicStatus>, CompletableFuture<Supplier<CompletableFuture<Boolean>>>> txTracker;

    @Override
    public byte[] toBytes() {
        return binaryWriter.get();
    }

    @Override
    public CompletableFuture<Hash> send() {
        return txSender.get();
    }

    @Override
    public CompletableFuture<Supplier<CompletableFuture<Boolean>>> track(BiConsumer<Exception, ExtrinsicStatus> callback) {
        return txTracker.apply(callback);
    }
}
