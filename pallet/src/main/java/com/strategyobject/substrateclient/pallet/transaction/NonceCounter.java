package com.strategyobject.substrateclient.pallet.transaction;

import com.strategyobject.substrateclient.rpc.api.AccountId;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public interface NonceCounter {
    CompletableFuture<BigInteger> getNonce(AccountId accountId);
}
