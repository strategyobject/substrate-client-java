package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcSubscription;
import com.strategyobject.substrateclient.rpc.types.BlockHash;
import com.strategyobject.substrateclient.rpc.types.Header;
import com.strategyobject.substrateclient.rpc.types.SignedBlock;
import com.strategyobject.substrateclient.scale.annotations.Scale;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@RpcInterface("chain")
public interface Chain {
    @RpcCall("getFinalizedHead")
    @Scale
    CompletableFuture<BlockHash> getFinalizedHead();

    @RpcSubscription(type = "newHead", subscribeMethod = "subscribeNewHead", unsubscribeMethod = "unsubscribeNewHead")
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> subscribeNewHeads(BiConsumer<Exception, Header> callback);

    @RpcCall("getBlockHash")
    @Scale
    CompletableFuture<BlockHash> getBlockHash(long number);

    @RpcCall("getBlock")
    CompletableFuture<SignedBlock> getBlock(@Scale BlockHash hash);
}
