package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcSubscription;
import com.strategyobject.substrateclient.rpc.types.Extrinsic;
import com.strategyobject.substrateclient.rpc.types.ExtrinsicStatus;
import com.strategyobject.substrateclient.rpc.types.Hash;
import com.strategyobject.substrateclient.types.PublicKey;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@RpcInterface(section = "author")
public interface Author {
    @RpcCall(method = "hasKey")
    CompletableFuture<Boolean> hasKey(PublicKey publicKey, String keyType);

    @RpcCall(method = "insertKey")
    CompletableFuture<Void> insertKey(String keyType, String secretUri, PublicKey publicKey);

    @RpcCall(method = "submitExtrinsic")
    CompletableFuture<Hash> submitExtrinsic(Extrinsic<?, ?, ?, ?> extrinsic);

    @RpcSubscription(type = "extrinsicUpdate", subscribeMethod = "submitAndWatchExtrinsic", unsubscribeMethod = "unwatchExtrinsic")
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> submitAndWatchExtrinsic(Extrinsic<?, ?, ?, ?> extrinsic,
                                                                                    BiConsumer<Exception, ExtrinsicStatus> callback);
}
