package com.strategyobject.substrateclient.rpc.api.section;

import com.strategyobject.substrateclient.rpc.annotation.RpcCall;
import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;
import com.strategyobject.substrateclient.rpc.annotation.RpcSubscription;
import com.strategyobject.substrateclient.rpc.api.BlockHash;
import com.strategyobject.substrateclient.rpc.api.Header;
import com.strategyobject.substrateclient.rpc.api.SignedBlock;
import com.strategyobject.substrateclient.scale.annotation.Scale;

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
