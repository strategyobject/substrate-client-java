package com.strategyobject.substrateclient.rpc.api.section;

import com.strategyobject.substrateclient.rpc.annotation.RpcCall;
import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;
import com.strategyobject.substrateclient.rpc.api.AccountId;
import com.strategyobject.substrateclient.rpc.api.Index;

import java.util.concurrent.CompletableFuture;

@RpcInterface("system")
public interface System {
    /**
     * Returns the next valid index (aka nonce) for given account.
     * This method takes into consideration all pending transactions
     * currently in the pool and if no transactions are found in the pool
     * it fallbacks to query the index from the runtime (aka. state nonce).
     *
     * @param accountId account
     * @return the next account index available on the node
     */
    @RpcCall("accountNextIndex")
    CompletableFuture<Index> accountNextIndex(AccountId accountId);
}
