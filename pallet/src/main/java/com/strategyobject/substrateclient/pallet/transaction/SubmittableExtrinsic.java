package com.strategyobject.substrateclient.pallet.transaction;

import com.strategyobject.substrateclient.crypto.KeyRing;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockHash;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash;
import com.strategyobject.substrateclient.rpc.api.section.Author;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor(staticName = "with")
public class SubmittableExtrinsic implements Submittable {
    private final int specVersion;
    private final int txVersion;
    private final @NonNull NonceCounter nonceCounter;
    private final @NonNull Author author;
    private final @NonNull CompletableFuture<BlockHash> genesisHash;

    @Override
    public CompletableFuture<SignedExtrinsic> sign(KeyRing keyRing) {
        throw new NotImplementedException();
    }

    @Override
    public CompletableFuture<Hash> signAndSend(KeyRing keyRing) {
        throw new NotImplementedException();
    }
}
