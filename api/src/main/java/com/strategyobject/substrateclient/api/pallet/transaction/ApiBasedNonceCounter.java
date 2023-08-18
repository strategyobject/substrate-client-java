package com.strategyobject.substrateclient.api.pallet.transaction;

import com.strategyobject.substrateclient.api.Api;
import com.strategyobject.substrateclient.pallet.transaction.NonceCounter;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.primitives.Index;
import com.strategyobject.substrateclient.rpc.api.section.System;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class ApiBasedNonceCounter implements NonceCounter {
    private final @NonNull Api api;
    @Override
    public CompletableFuture<BigInteger> getNonce(AccountId accountId) {
        return api.rpc(System.class)
                .accountNextIndex(accountId)
                .thenApplyAsync(Index::getValue);
    }
}
