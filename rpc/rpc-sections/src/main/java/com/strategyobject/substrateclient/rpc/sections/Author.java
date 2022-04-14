package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcSubscription;
import com.strategyobject.substrateclient.rpc.types.Extrinsic;
import com.strategyobject.substrateclient.rpc.types.ExtrinsicStatus;
import com.strategyobject.substrateclient.rpc.types.Hash;
import com.strategyobject.substrateclient.scale.annotations.Scale;
import com.strategyobject.substrateclient.types.PublicKey;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@RpcInterface("author")
public interface Author {
    @RpcCall("hasKey")
    CompletableFuture<Boolean> hasKey(@Scale PublicKey publicKey, String keyType);

    @RpcCall("insertKey")
    CompletableFuture<Void> insertKey(String keyType, String secretUri, @Scale PublicKey publicKey);

    @RpcCall("submitExtrinsic")
    @Scale
    CompletableFuture<Hash> submitExtrinsic(@Scale Extrinsic<?, ?, ?, ?> extrinsic);

    @RpcSubscription(type = "extrinsicUpdate", subscribeMethod = "submitAndWatchExtrinsic", unsubscribeMethod = "unwatchExtrinsic")
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> submitAndWatchExtrinsic(@Scale Extrinsic<?, ?, ?, ?> extrinsic,
                                                                                    BiConsumer<Exception, ExtrinsicStatus> callback);
}
