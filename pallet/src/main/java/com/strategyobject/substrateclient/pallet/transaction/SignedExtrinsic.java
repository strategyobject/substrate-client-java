package com.strategyobject.substrateclient.pallet.transaction;

import com.strategyobject.substrateclient.rpc.api.ExtrinsicStatus;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface SignedExtrinsic {
    byte[] toBytes();

    CompletableFuture<Hash> send();

    CompletableFuture<Supplier<CompletableFuture<Boolean>>> track(BiConsumer<Exception, ExtrinsicStatus> callback);
}
