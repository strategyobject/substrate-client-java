package com.strategyobject.substrateclient.rpc.api.section;

import com.strategyobject.substrateclient.crypto.PublicKey;
import com.strategyobject.substrateclient.rpc.annotation.RpcCall;
import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;
import com.strategyobject.substrateclient.rpc.annotation.RpcSubscription;
import com.strategyobject.substrateclient.rpc.api.Extrinsic;
import com.strategyobject.substrateclient.rpc.api.ExtrinsicStatus;
import com.strategyobject.substrateclient.rpc.api.Hash;
import com.strategyobject.substrateclient.scale.annotation.Scale;

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
