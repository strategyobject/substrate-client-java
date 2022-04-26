package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;
import com.strategyobject.substrateclient.rpc.types.AccountId;

import java.util.concurrent.CompletableFuture;

@RpcInterface("system")
public interface System {
    @RpcCall("accountNextIndex")
    CompletableFuture<Integer> accountNextIndex(AccountId accountId);
}
