package com.strategyobject.substrateclient.rpc.api.section;

import com.strategyobject.substrateclient.rpc.annotation.RpcCall;
import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;
import com.strategyobject.substrateclient.rpc.api.AccountId;

import java.util.concurrent.CompletableFuture;

@RpcInterface("system")
public interface System {
    @RpcCall("accountNextIndex")
    CompletableFuture<Integer> accountNextIndex(AccountId accountId);
}
