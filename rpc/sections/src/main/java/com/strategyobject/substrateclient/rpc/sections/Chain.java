package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcInterface;
import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcSubscription;
import com.strategyobject.substrateclient.types.BlockHash;
import com.strategyobject.substrateclient.types.Header;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@RpcInterface(section = "chain")
public interface Chain {
    @RpcCall(method = "getFinalizedHead")
    CompletableFuture<BlockHash> getFinalizedHead();

    @RpcSubscription(type = "newHead", subscribeMethod = "subscribeNewHead", unsubscribeMethod = "unsubscribeNewHead")
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> subscribeNewHeads(BiConsumer<Exception, Header> callback);
}
