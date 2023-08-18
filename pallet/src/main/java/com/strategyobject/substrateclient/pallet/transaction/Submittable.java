package com.strategyobject.substrateclient.pallet.transaction;

import com.strategyobject.substrateclient.crypto.KeyRing;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash;

import java.util.concurrent.CompletableFuture;

public interface Submittable {
    CompletableFuture<SignedExtrinsic> sign(KeyRing keyRing);

    CompletableFuture<Hash> signAndSend(KeyRing keyRing);
}
