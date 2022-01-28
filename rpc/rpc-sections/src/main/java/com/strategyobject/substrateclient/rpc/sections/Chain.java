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

@RpcInterface(section = "chain")
public interface Chain {
    @RpcCall(method = "getFinalizedHead")
    @Scale
    CompletableFuture<BlockHash> getFinalizedHead();

    @RpcSubscription(type = "newHead", subscribeMethod = "subscribeNewHead", unsubscribeMethod = "unsubscribeNewHead")
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> subscribeNewHeads(BiConsumer<Exception, Header> callback);

    @RpcCall(method = "getBlockHash")
    @Scale
    CompletableFuture<BlockHash> getBlockHash(long number);

    @RpcCall(method = "getBlock")
    CompletableFuture<SignedBlock> getBlock(@Scale BlockHash hash);
}
